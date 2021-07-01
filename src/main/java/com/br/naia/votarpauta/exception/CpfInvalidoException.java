package com.br.naia.votarpauta.exception;

public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException() {
        super("O CPF informado tem o formato inválido");
    }

}
