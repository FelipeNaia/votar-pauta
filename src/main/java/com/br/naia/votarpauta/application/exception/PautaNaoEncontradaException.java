package com.br.naia.votarpauta.application.exception;

public class PautaNaoEncontradaException extends RuntimeException {

    public PautaNaoEncontradaException() {
        super("A pauta informada não foi encontrada");
    }

}
