package com.br.naia.votarpauta.jobs;

import com.br.naia.votarpauta.service.PautaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FecharPautaJob {

    @Autowired
    private PautaService pautaService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 * * * * *")
    public void fecharPauta() {
        log.info("Rodando o job para fechar as pautas");
        pautaService.fecharPautas();
    }
}
