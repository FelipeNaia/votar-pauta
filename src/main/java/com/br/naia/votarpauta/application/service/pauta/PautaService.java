package com.br.naia.votarpauta.application.service.pauta;

import com.br.naia.votarpauta.application.exception.*;
import com.br.naia.votarpauta.application.integration.userinfo.UserInfoIntegration;
import com.br.naia.votarpauta.application.kafka.sender.PublicarResultadoDaPautaTopicSender;
import com.br.naia.votarpauta.application.service.voto.VotarInputData;
import com.br.naia.votarpauta.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private PublicarResultadoDaPautaTopicSender encerrarPautaTopic;

    @Autowired
    private Validator validator;

    @Autowired
    private UserInfoIntegration userInfoIntegration;

    public Mono<PautaDTO> cadastrar(CadastrarPautaInputData cadastrarPautaInputData) {
        validarDadosDeCadastro(cadastrarPautaInputData);
        Pauta pauta = Pauta.builder()
                .nome(cadastrarPautaInputData.getNome())
                .status(PautaStatus.NOVA)
                .build();

        return pautaRepository.save(pauta)
                .map(PautaDtoTranslator::from);
    }

    public Mono<PautaDTO> abrirSessao(AbrirSessaoInputData abrirSessaoInputData) {
        long tempoEmMinutos = Objects.isNull(abrirSessaoInputData.getTempoEmMinutos()) ? 1 : abrirSessaoInputData.getTempoEmMinutos();

        return pautaRepository.findById(abrirSessaoInputData.getPautaId())
                .switchIfEmpty(Mono.error(new PautaNaoEncontradaException(String.format("A pauta id: %s não foi encontrada", abrirSessaoInputData.getPautaId()))))
                .doOnNext(this::validarPautaParaAbertura)
                .flatMap(pauta -> {
                    pauta.setAbertura(LocalDateTime.now());
                    pauta.setFechamento(LocalDateTime.now().plusMinutes(tempoEmMinutos));
                    pauta.setStatus(PautaStatus.ABERTA);
                    return pautaRepository.save(pauta).map(PautaDtoTranslator::from);
                });
    }

    public void fecharPautas() {
        pautaRepository.findAllByFechamentoBeforeAndStatus(LocalDateTime.now(), PautaStatus.ABERTA)
                .flatMap(pauta -> {
                    pauta.setStatus(PautaStatus.FECHADA);
                    pauta.setVotosSim(pauta.getVotos().stream().filter(voto -> voto.getVotoValor().equals(VotoValor.SIM)).count());
                    pauta.setVotosNao(pauta.getVotos().stream().filter(voto -> voto.getVotoValor().equals(VotoValor.NAO)).count());

                    pautaRepository.save(pauta);

                    encerrarPautaTopic.send(String.format("%s teve %d votos Sim e %d votos Não", pauta.getNome(), pauta.getVotosSim(), pauta.getVotosNao()));
                    return Mono.empty();
                }).subscribe();

    }

    public Mono<Void> votar(VotarInputData votarInputData) {
        return pautaRepository.findById(votarInputData.getPautaId())
                .switchIfEmpty(Mono.error(new PautaNaoEncontradaException(String.format("A pauta id: %d não foi encontrada", votarInputData.getPautaId()))))
                .doOnNext(this::validarPautaParaVotar)
                .doOnNext(pauta -> validarVoto(votarInputData, pauta))
                .map(pauta -> {
                            Voto voto = Voto.builder()
                                    .votoValor(votarInputData.getVotoValor())
                                    .cpf(votarInputData.getCpf())
                                    .pauta(pauta)
                                    .build();
                            pauta.getVotos().add(voto);

                            return pautaRepository.save(pauta);
                        }).then();


    }

    private void validarVoto(VotarInputData votarInputData, Pauta pauta) {
        votarInputData.setCpf(votarInputData.getCpf().replaceAll("[^0-9]",""));

        if (pauta.getVotos().stream().anyMatch(voto ->  votarInputData.getCpf().equals(voto.getCpf()))) {
            throw new CpfJaVotouException("Este CPJ já foi usado para votar nesta pauta!");
        }

        if(!userInfoIntegration.cpfEhValido(votarInputData.getCpf())) {
            throw new CpfInvalidoException(String.format("O CPF %s não pode ser usado para votar", votarInputData.getCpf()));
        }
    }

    private void validarPautaParaVotar(Pauta pauta) {
        if (!pauta.getStatus().equals(PautaStatus.ABERTA) || LocalDateTime.now().isAfter(pauta.getFechamento())) {
            throw new PautaNaoEstaAbertaParaVotoException("Só é possível votar numa pauta aberta");
        }
    }

    private void validarPautaParaAbertura(Pauta pauta) {
        if(!pauta.getStatus().equals(PautaStatus.NOVA)) {
            throw new PautaNaoEhNovaException("Só é possível a abertura de uma pauta nova");
        }
    }

    private void validarDadosDeCadastro(CadastrarPautaInputData cadastrarPautaInputData) {
        Set<ConstraintViolation<CadastrarPautaInputData>> violations = validator.validate(cadastrarPautaInputData);
        if(!violations.isEmpty()) {
            String mensagensDeErro = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
            throw new DadosDeCadastroInvalidosException(mensagensDeErro);
        }
    }
}
