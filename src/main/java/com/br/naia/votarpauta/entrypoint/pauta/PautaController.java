package com.br.naia.votarpauta.entrypoint.pauta;

import com.br.naia.votarpauta.application.service.pauta.AbrirSessaoInputData;
import com.br.naia.votarpauta.application.service.pauta.CadastrarPautaInputData;
import com.br.naia.votarpauta.application.service.pauta.PautaService;
import com.br.naia.votarpauta.domain.PautaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pauta")
public class PautaController {
    @Autowired
    private PautaService pautaService;

    @PutMapping
    public Mono<PautaDTO> cadastrar(@RequestBody CadastrarPautaInputData cadastrarPautaInputData){
        return pautaService.cadastrar(cadastrarPautaInputData);
    }

    @PostMapping("/{pautaId}/abrir")
    public Mono<PautaDTO> abrirSessao(@RequestBody AbrirSessaoInputData abrirSessaoInputData, @PathVariable String pautaId){
        abrirSessaoInputData.setPautaId(pautaId);
        return pautaService.abrirSessao(abrirSessaoInputData);
    }

}
