package com.br.naia.votarpauta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pauta")
public class Pauta {

    @Id
    private String id;
    private String nome;
    private PautaStatus status;
    private Long votosSim;
    private Long votosNao;
    private LocalDateTime abertura;
    private LocalDateTime fechamento;
    private List<Voto> votos;
}
