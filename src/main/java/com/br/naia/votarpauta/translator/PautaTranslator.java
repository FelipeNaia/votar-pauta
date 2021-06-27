package com.br.naia.votarpauta.translator;

import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.entity.Pauta;
import org.springframework.stereotype.Component;

@Component
public class PautaTranslator {

    public PautaDTO toDto(Pauta pauta) {
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

    public Pauta fromDto(PautaDTO pautaDTO) {
        return Pauta.builder()
                .id(pautaDTO.getId())
                .nome(pautaDTO.getNome())
                .status(pautaDTO.getStatus())
                .votosSim(pautaDTO.getVotosSim())
                .votosNao(pautaDTO.getVotosNao())
                .abertura(pautaDTO.getAbertura())
                .fechamento(pautaDTO.getFechamento())
                .build();
    }
}
