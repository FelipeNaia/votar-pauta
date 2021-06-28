package com.br.naia.votarpauta.controller;

import com.br.naia.votarpauta.controller.inputdata.VotarInputData;
import com.br.naia.votarpauta.service.VotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voto")
public class VotoController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VotoService votoService;

    @PostMapping
    public void votar(@RequestBody VotarInputData votarInputData){
        votoService.votar(votarInputData);
    }
}
