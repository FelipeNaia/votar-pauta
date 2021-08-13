package com.br.naia.votarpauta.application.service.voto;

import com.br.naia.votarpauta.domain.VotoValor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotarInputData {
    private String pautaId;
    private String cpf;
    private VotoValor votoValor;
}
