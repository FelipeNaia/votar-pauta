package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.PautaRepository;
import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.dto.PautaDTO;
import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.translator.PautaTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private PautaTranslator pautaTranslator;

    public PautaDTO cadastrar (CadastrarPautaInputData cadastrarPautaInputData) {
        Pauta pauta = Pauta.builder().nome(cadastrarPautaInputData.getNome()).build();
        Pauta pautaSalva = pautaRepository.save(pauta);
        return pautaTranslator.toDto(pautaSalva);
    }
}
