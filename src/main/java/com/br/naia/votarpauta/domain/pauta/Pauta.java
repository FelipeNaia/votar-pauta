package com.br.naia.votarpauta.domain.pauta;

import com.br.naia.votarpauta.domain.voto.Voto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PAUTA", schema = "VOTAR_PAUTA")
public class Pauta {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 64)
    private PautaStatus status;

    @Column(name = "VOTOS_SIM")
    private Long votosSim;

    @Column(name = "VOTOS_NAO")
    private Long votosNao;

    @Column(name = "ABERTURA")
    private LocalDateTime abertura;

    @Column(name = "FECHAMENTO")
    private LocalDateTime fechamento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pauta")
    private List<Voto> votos;

}
