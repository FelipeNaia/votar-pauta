package com.br.naia.votarpauta.entrypoint.pauta;

import com.br.naia.votarpauta.application.service.pauta.AbrirSessaoInputData;
import com.br.naia.votarpauta.application.service.pauta.CadastrarPautaInputData;
import com.br.naia.votarpauta.domain.pauta.PautaDTO;
import com.br.naia.votarpauta.application.service.pauta.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pauta")
public class PautaController {
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
