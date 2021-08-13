package com.br.naia.votarpauta.entrypoint.voto;

import com.br.naia.votarpauta.application.service.pauta.PautaService;
import com.br.naia.votarpauta.application.service.voto.VotarInputData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/voto")
public class VotoController {
    @Autowired
    private PautaService pautaService;

    @PutMapping
    public Mono<Void> votar(@RequestBody VotarInputData votarInputData){
        return pautaService.votar(votarInputData);
    }
}
