package com.br.naia.votarpauta.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.br.naia.votarpauta.constants.Topics.PUBLICAR_RESULTADO;

@Service
public class ResultadoPautaConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = PUBLICAR_RESULTADO, containerFactory = "votarPautaKafkaListenerContainerFactory")
    public void fecharPauta(String resultado) {
        log.info(String.format("O resultado da pauta foi: %s", resultado));
    }
}
