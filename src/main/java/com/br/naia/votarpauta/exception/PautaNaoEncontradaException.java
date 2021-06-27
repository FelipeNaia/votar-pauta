package com.br.naia.votarpauta.exception;

public class PautaNaoEncontradaException extends RuntimeException {

    public PautaNaoEncontradaException() {
        super("A pauta informada não foi encontrada");
    }

}
