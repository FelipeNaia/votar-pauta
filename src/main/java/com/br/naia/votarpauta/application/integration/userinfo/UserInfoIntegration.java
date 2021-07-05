package com.br.naia.votarpauta.application.integration.userinfo;

import com.br.naia.votarpauta.application.exception.CpfJaVotouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class UserInfoIntegration {

    @Value(value = "${votar-pauta.user-info.url}")
    private String USER_INFO_URL;

    private RestTemplate restTemplate;

    @Autowired
    public UserInfoIntegration(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new UserInfoErrorHandler())
                .build();
    }

    public void validarCPF(String cpf) {
        UserInfoResponse userInfoResponse = restTemplate.getForObject(USER_INFO_URL + cpf, UserInfoResponse.class);

        if(Objects.isNull(userInfoResponse) || UserInfoResponse.UserInfoStatus.UNABLE_TO_VOTE.equals(userInfoResponse.getStatus())) {
            throw new CpfJaVotouException(String.format("O CPF %s não pode ser usado para votar", cpf));
        }
    }

}
