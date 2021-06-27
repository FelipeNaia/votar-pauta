package com.br.naia.votarpauta.controller;

import com.br.naia.votarpauta.controller.inputdata.AbrirSessaoInputData;
import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.service.PautaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pauta")
public class PautaController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PautaService pautaService;

    @PutMapping
    public PautaDTO cadastrar(@RequestBody CadastrarPautaInputData cadastrarPautaInputData){
        return pautaService.cadastrar(cadastrarPautaInputData);
    }

    @PostMapping("/{pautaId}/abrir")
    public PautaDTO abrirSessao(@RequestBody AbrirSessaoInputData abrirSessaoInputData, @PathVariable Long pautaId){
        abrirSessaoInputData.setPautaId(pautaId);
        return pautaService.abrirSessao(abrirSessaoInputData);
    }

}
