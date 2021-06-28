package com.br.naia.votarpauta.dto;

import com.br.naia.votarpauta.constants.VotoValor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    private Long id;
    private String cpf;
    private VotoValor votoValor;
}
