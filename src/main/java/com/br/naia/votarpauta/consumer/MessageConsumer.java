package com.br.naia.votarpauta.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(topics = "test", containerFactory = "votarPautaKafkaListenerContainerFactory")
    public void listenGroupFoo(String message) {
        log.info(String.format("Received Message in group foo: %s", message));
    }

}
