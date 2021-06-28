package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.controller.inputdata.AbrirSessaoInputData;
import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.constants.PautaStatus;
import com.br.naia.votarpauta.exception.PautaNaoEncontradaException;
import com.br.naia.votarpauta.repository.PautaRepository;
import com.br.naia.votarpauta.topic.PublicarResultadoDaPautaTopic;
import com.br.naia.votarpauta.translator.PautaTranslator;
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
    private PautaTranslator pautaTranslator;

    @Autowired
    private PublicarResultadoDaPautaTopic encerrarPautaTopic;

    @Autowired
    private VotoService votoService;

    public PautaDTO cadastrar(CadastrarPautaInputData cadastrarPautaInputData) {
        validarDadosDeCadastro(cadastrarPautaInputData);
        Pauta pauta = Pauta.builder()
                .nome(cadastrarPautaInputData.getNome())
                .status(PautaStatus.NOVA)
                .build();

        Pauta pautaSalva = pautaRepository.save(pauta);
        return pautaTranslator.toDto(pautaSalva);
    }

    public PautaDTO abrirSessao(AbrirSessaoInputData abrirSessaoInputData) {
        Pauta pauta = pautaRepository.findById(abrirSessaoInputData.getPautaId()).orElseThrow(PautaNaoEncontradaException::new);
        long tempoEmMinutos = Objects.isNull(abrirSessaoInputData.getTempoEmMinutos()) ? 1 : abrirSessaoInputData.getTempoEmMinutos();

        validarPautaParaAbertura(pauta);

        pauta.setAbertura(LocalDateTime.now());
        pauta.setFechamento(LocalDateTime.now().plusMinutes(tempoEmMinutos));
        pauta.setStatus(PautaStatus.ABERTA);

        return pautaTranslator.toDto(pautaRepository.save(pauta));
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
        Assert.isTrue(pauta.getStatus().equals(PautaStatus.NOVA), "Só é possível a abertura de uma pauta nova");
    }

    private void validarDadosDeCadastro(CadastrarPautaInputData cadastrarPautaInputData) {
        Assert.notNull(cadastrarPautaInputData.getNome(), "O nome da pauta deve ser informado");
    }
}
