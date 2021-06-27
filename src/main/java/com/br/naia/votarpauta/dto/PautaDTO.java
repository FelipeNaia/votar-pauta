package com.br.naia.votarpauta.dto;

import com.br.naia.votarpauta.enumeration.PautaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaDTO {
    private Long id;
    private String nome;
    private PautaStatus status;
    private Long votosSim;
    private Long votosNao;
    private LocalDateTime abertura;
    private LocalDateTime fechamento;
    private List<VotoDTO> votos;
}
