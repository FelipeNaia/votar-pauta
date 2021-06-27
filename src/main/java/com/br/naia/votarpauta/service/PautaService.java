package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.PautaRepository;
import com.br.naia.votarpauta.controller.inputdata.AbrirSessaoInputData;
import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.enumeration.PautaStatus;
import com.br.naia.votarpauta.exception.PautaNaoEncontradaException;
import com.br.naia.votarpauta.translator.PautaTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private PautaTranslator pautaTranslator;

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

    private void validarPautaParaAbertura(Pauta pauta) {
        Assert.isTrue(pauta.getStatus().equals(PautaStatus.NOVA), "Só é possível a abertura de uma pauta nova");
    }

    private void validarDadosDeCadastro(CadastrarPautaInputData cadastrarPautaInputData) {
        Assert.notNull(cadastrarPautaInputData.getNome(), "O nome da pauta deve ser informado");
    }
}
