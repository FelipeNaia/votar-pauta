package com.br.naia.votarpauta.controller;

import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.service.PautaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pauta")
public class PautaController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PautaService pautaService;

    @PutMapping
    public PautaDTO cadastrar(CadastrarPautaInputData cadastrarPautaInputData){
        return pautaService.cadastrar(cadastrarPautaInputData);
    }

}
