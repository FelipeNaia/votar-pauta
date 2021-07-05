package com.br.naia.votarpauta.application.service.pauta;

import com.br.naia.votarpauta.domain.pauta.PautaDTO;
import com.br.naia.votarpauta.domain.pauta.Pauta;
import org.springframework.stereotype.Component;

public class PautaDtoTranslator {

    public static PautaDTO from(Pauta pauta) {
        return PautaDTO.builder()
                .id(pauta.getId())
                .nome(pauta.getNome())
                .status(pauta.getStatus())
                .votosSim(pauta.getVotosSim())
                .votosNao(pauta.getVotosNao())
                .abertura(pauta.getAbertura())
                .fechamento(pauta.getFechamento())
                .build();
    }
}
