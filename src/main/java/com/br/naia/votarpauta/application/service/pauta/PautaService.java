package com.br.naia.votarpauta.application.service.pauta;

import com.br.naia.votarpauta.application.exception.PautaNaoEhNovaException;
import com.br.naia.votarpauta.application.service.voto.VotoService;
import com.br.naia.votarpauta.domain.pauta.PautaDTO;
import com.br.naia.votarpauta.domain.pauta.Pauta;
import com.br.naia.votarpauta.domain.pauta.PautaStatus;
import com.br.naia.votarpauta.application.exception.PautaNaoEncontradaException;
import com.br.naia.votarpauta.domain.pauta.PautaRepository;
import com.br.naia.votarpauta.application.kafka.sender.PublicarResultadoDaPautaTopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private PublicarResultadoDaPautaTopicSender encerrarPautaTopic;

    @Autowired
    private VotoService votoService;

    public PautaDTO cadastrar(CadastrarPautaInputData cadastrarPautaInputData) {
        validarDadosDeCadastro(cadastrarPautaInputData);
        Pauta pauta = Pauta.builder()
                .nome(cadastrarPautaInputData.getNome())
                .status(PautaStatus.NOVA)
                .build();

        Pauta pautaSalva = pautaRepository.save(pauta);
        return PautaDtoTranslator.from(pautaSalva);
    }

    public PautaDTO abrirSessao(AbrirSessaoInputData abrirSessaoInputData) {
        Pauta pauta = pautaRepository.findById(abrirSessaoInputData.getPautaId())
                .orElseThrow(() -> new PautaNaoEncontradaException(String.format("A pauta id: %d não foi encontrada", abrirSessaoInputData.getPautaId())));
        long tempoEmMinutos = Objects.isNull(abrirSessaoInputData.getTempoEmMinutos()) ? 1 : abrirSessaoInputData.getTempoEmMinutos();

        validarPautaParaAbertura(pauta);

        pauta.setAbertura(LocalDateTime.now());
        pauta.setFechamento(LocalDateTime.now().plusMinutes(tempoEmMinutos));
        pauta.setStatus(PautaStatus.ABERTA);

        return PautaDtoTranslator.from(pautaRepository.save(pauta));
    }

    public void fecharPautas() {
        List<Pauta> pautasParaFechar = pautaRepository.findAllByFechamentoBeforeAndStatus(LocalDateTime.now(), PautaStatus.ABERTA);

        pautasParaFechar.forEach(pauta -> {
            pauta.setStatus(PautaStatus.FECHADA);
            pauta.setVotosSim(votoService.contabilizarVotosSim(pauta.getId()));
            pauta.setVotosNao(votoService.contabilizarVotosNao(pauta.getId()));

            pautaRepository.save(pauta);

            encerrarPautaTopic.send(String.format("%s teve %d votos Sim e %d votos Não", pauta.getNome(), pauta.getVotosSim(), pauta.getVotosNao()));
        });
    }

    private void validarPautaParaAbertura(Pauta pauta) {
        if(!pauta.getStatus().equals(PautaStatus.NOVA)) {
            throw new PautaNaoEhNovaException("Só é possível a abertura de uma pauta nova");
        }
    }

    private void validarDadosDeCadastro(CadastrarPautaInputData cadastrarPautaInputData) {
        Assert.notNull(cadastrarPautaInputData.getNome(), "O nome da pauta deve ser informado");
    }
}
