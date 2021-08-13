package com.br.naia.votarpauta.domain.pauta;

import com.br.naia.votarpauta.domain.voto.Voto;
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
    private Long id;
    private String nome;
    private PautaStatus status;
    private Long votosSim;
    private Long votosNao;
    private LocalDateTime abertura;
    private LocalDateTime fechamento;
    private List<Voto> votos;
}
