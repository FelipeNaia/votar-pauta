package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.constants.VotoValor;
import com.br.naia.votarpauta.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    public Long contabilizarVotosSim(Long pautaId) {
        return votoRepository.countAllByPauta_IdAndVotoValor(pautaId, VotoValor.SIM);
    }

    public Long contabilizarVotosNao(Long pautaId) {
        return votoRepository.countAllByPauta_IdAndVotoValor(pautaId, VotoValor.SIM);
    }
}
