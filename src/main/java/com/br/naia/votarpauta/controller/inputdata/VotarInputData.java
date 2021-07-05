package com.br.naia.votarpauta.controller.inputdata;

import com.br.naia.votarpauta.domain.voto.VotoValor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotarInputData {
    private Long pautaId;
    private String cpf;
    private VotoValor votoValor;
}
