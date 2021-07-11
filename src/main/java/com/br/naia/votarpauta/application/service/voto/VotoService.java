package com.br.naia.votarpauta.application.service.voto;

import com.br.naia.votarpauta.application.exception.CpfJaVotouException;
import com.br.naia.votarpauta.application.exception.PautaNaoEncontradaException;
import com.br.naia.votarpauta.application.exception.PautaNaoEstaAbertaParaVotoException;
import com.br.naia.votarpauta.application.integration.userinfo.UserInfoIntegration;
import com.br.naia.votarpauta.domain.pauta.Pauta;
import com.br.naia.votarpauta.domain.pauta.PautaRepository;
import com.br.naia.votarpauta.domain.pauta.PautaStatus;
import com.br.naia.votarpauta.domain.voto.Voto;
import com.br.naia.votarpauta.domain.voto.VotoRepository;
import com.br.naia.votarpauta.domain.voto.VotoValor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Pauta pauta = pautaRepository.findById(votarInputData.getPautaId())
                .orElseThrow(() -> new PautaNaoEncontradaException(String.format("A pauta id: %d não foi encontrada", votarInputData.getPautaId())));

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

        if (votoRepository.existsByPauta_IdAndCpf(votarInputData.getPautaId(), votarInputData.getCpf())) {
            throw new CpfJaVotouException("Este CPJ já foi usado para votar nesta pauta!");
        }

        userInfoIntegration.validarCPF(votarInputData.getCpf());
    }

    private void validarPautaParaVotar(Pauta pauta) {
        if (!pauta.getStatus().equals(PautaStatus.ABERTA) || LocalDateTime.now().isAfter(pauta.getFechamento())) {
            throw new PautaNaoEstaAbertaParaVotoException("Só é possível votar numa pauta aberta");
        }
    }
}
