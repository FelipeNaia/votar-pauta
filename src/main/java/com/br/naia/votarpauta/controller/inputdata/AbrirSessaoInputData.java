package com.br.naia.votarpauta.controller.inputdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbrirSessaoInputData {
    private Long tempoEmMinutos;
    private Long pautaId;
}
