package com.br.naia.votarpauta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public String hello () {
        return "Hello World!";
    }

    @GetMapping("/{msg}")
    public String post(@PathVariable String msg) {
        ListenableFuture<SendResult<String, String>> topicName = kafkaTemplate.send("test", msg);

        topicName.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(format("Sent message=[%s] with offset=[%d]", msg, result.getRecordMetadata().offset()));
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info(format("Unable to send message=[%s] due to : %s", msg, ex.getMessage()));
            }
        });
        return "OK! " + msg;
    }


}
