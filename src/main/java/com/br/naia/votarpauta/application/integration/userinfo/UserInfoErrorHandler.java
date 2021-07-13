package com.br.naia.votarpauta.application.integration.userinfo;

import com.br.naia.votarpauta.application.exception.CpfInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class UserInfoErrorHandler implements ResponseErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(UserInfoErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new CpfInvalidoException("O CPF informado tem o formato inválido");
        } else {
            log.error(String.format("Ocorreu algum erro na integração com o user-info: (%d) %s", httpResponse.getRawStatusCode(), httpResponse.getStatusText()));
        }
    }
}