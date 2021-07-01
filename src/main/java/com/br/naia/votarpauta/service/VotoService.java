package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.constants.PautaStatus;
import com.br.naia.votarpauta.constants.VotoValor;
import com.br.naia.votarpauta.controller.inputdata.VotarInputData;
import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.entity.Voto;
import com.br.naia.votarpauta.exception.PautaNaoEncontradaException;
import com.br.naia.votarpauta.integration.userinfo.UserInfoIntegration;
import com.br.naia.votarpauta.repository.PautaRepository;
import com.br.naia.votarpauta.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private UserInfoIntegration userInfoIntegration;

    public Long contabilizarVotosSim(Long pautaId) {
        return votoRepository.countAllByPauta_IdAndVotoValor(pautaId, VotoValor.SIM);
    }

    public Long contabilizarVotosNao(Long pautaId) {
        return votoRepository.countAllByPauta_IdAndVotoValor(pautaId, VotoValor.NAO);
    }

    public void votar(VotarInputData votarInputData) {
        Pauta pauta = pautaRepository.findById(votarInputData.getPautaId()).orElseThrow(PautaNaoEncontradaException::new);

        validarPautaParaVotar(pauta);
        validarVoto(votarInputData);

        Voto voto = Voto.builder()
                .votoValor(votarInputData.getVotoValor())
                .cpf(votarInputData.getCpf())
                .pauta(pauta)
                .build();

        votoRepository.save(voto);
    }

    private void validarVoto(VotarInputData votarInputData) {
        votarInputData.setCpf(votarInputData.getCpf().replaceAll("[^0-9]",""));
        Assert.isTrue(!votoRepository.existsByPauta_IdAndCpf(votarInputData.getPautaId(), votarInputData.getCpf()), "Este CPJ já foi usado para votar nesta pauta!");
        userInfoIntegration.validarCPF(votarInputData.getCpf());
    }

    private void validarPautaParaVotar(Pauta pauta) {
        String deveEstarAberta = "Só é possível votar numa pauta aberta";
        Assert.isTrue(pauta.getStatus().equals(PautaStatus.ABERTA), deveEstarAberta);
        Assert.isTrue(LocalDateTime.now().isBefore(pauta.getFechamento()), deveEstarAberta);
    }
}
