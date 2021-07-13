package com.br.naia.votarpauta.entrypoint.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(RuntimeException e) {
        log.error("Algo deu errado: ", e);
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.CONFLICT);
    }

    @AllArgsConstructor
    @Data
    static class ErrorMessage {
        String message;
    }

}