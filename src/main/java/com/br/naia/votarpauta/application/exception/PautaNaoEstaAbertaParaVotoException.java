package com.br.naia.votarpauta.application.exception;

public class PautaNaoEstaAbertaParaVotoException extends RuntimeException {

    public PautaNaoEstaAbertaParaVotoException(String message) {
        super(message);
    }
}
