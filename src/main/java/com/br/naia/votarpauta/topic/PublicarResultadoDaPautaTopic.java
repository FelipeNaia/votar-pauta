package com.br.naia.votarpauta.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.br.naia.votarpauta.constants.Topics.PUBLICAR_RESULTADO;

@Component
public class PublicarResultadoDaPautaTopic {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String resultado) {
        kafkaTemplate.send(PUBLICAR_RESULTADO, resultado);
    }

}
