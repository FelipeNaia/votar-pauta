package com.br.naia.votarpauta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VOTO", schema = "VOTAR_PAUTA")
@SequenceGenerator(name = "SEQ_VOTO", sequenceName = "VOTAR_PAUTA.SEQ_VOTO", allocationSize = 1)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VOTO")
    @Column(name = "VO_ID")
    private Long id;

    @Column(name = "VO_CPF")
    private String cpf;

    @Column(name = "VO_VALOR", length = 11)
    private VotoValor votoValor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID", referencedColumnName = "PA_ID")
    private Pauta pauta;

    public enum VotoValor{
        SIM, NAO
    }
}
