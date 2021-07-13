package com.br.naia.votarpauta.domain.voto;

import com.br.naia.votarpauta.domain.pauta.Pauta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VOTO", schema = "VOTAR_PAUTA")
public class Voto {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CPF")
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "VALOR", length = 64)
    private VotoValor votoValor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAUTA", referencedColumnName = "ID")
    private Pauta pauta;
}
