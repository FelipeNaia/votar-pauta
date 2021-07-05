package com.br.naia.votarpauta.entrypoint.voto;

import com.br.naia.votarpauta.application.service.voto.VotarInputData;
import com.br.naia.votarpauta.application.service.voto.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voto")
public class VotoController {
    @Autowired
    private VotoService votoService;

    @PutMapping
    public void votar(@RequestBody VotarInputData votarInputData){
        votoService.votar(votarInputData);
    }
}
