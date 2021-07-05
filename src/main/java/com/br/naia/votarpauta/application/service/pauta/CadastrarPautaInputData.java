package com.br.naia.votarpauta.application.service.pauta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarPautaInputData {
    @NotNull(message = "O nome da pauta deve ser informado")
    private String nome;
}
