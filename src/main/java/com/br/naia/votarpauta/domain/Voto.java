package com.br.naia.votarpauta.domain.voto;

import com.br.naia.votarpauta.domain.pauta.Pauta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "voto")
public class Voto {

    @Id
    private String id;
    private String cpf;
    private VotoValor votoValor;
    private Pauta pauta;
}
