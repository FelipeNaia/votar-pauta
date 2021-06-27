package com.br.naia.votarpauta.entity;

import com.br.naia.votarpauta.enumeration.PautaStatus;
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
@SequenceGenerator(name = "SEQ_PAUTA", sequenceName = "VOTAR_PAUTA.SEQ_PAUTA", allocationSize = 1)
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PAUTA")
    @Column(name = "pa_id")
    private Long id;

    @Column(name = "pa_nome")
    private String nome;

    @Column(name = "pa_status", length = 64)
    private PautaStatus status;

    @Column(name = "pa_votos_sim")
    private Long votosSim;

    @Column(name = "pa_votos_nao")
    private Long votosNao;

    @Column(name = "pa_abertura")
    private LocalDateTime abertura;

    @Column(name = "pa_fechamento")
    private LocalDateTime fechamento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "Voto")
    private List<Voto> votos;

}
