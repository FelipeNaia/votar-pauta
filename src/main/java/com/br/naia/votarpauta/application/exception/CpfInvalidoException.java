package com.br.naia.votarpauta.application.exception;

public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException() {
        super("O CPF informado tem o formato inválido");
    }

}
