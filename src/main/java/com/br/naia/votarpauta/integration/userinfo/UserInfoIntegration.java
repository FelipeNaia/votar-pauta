package com.br.naia.votarpauta.integration.userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class UserInfoIntegration {

    @Value(value = "${votar-pauta.user-info.url}")
    private String USER_INFO_URL;

    private static final String CPF_NAO_PODE_VOTAR = "Este CPF não pode ser usado para votar";

    private RestTemplate restTemplate;

    @Autowired
    public UserInfoIntegration(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new UserInfoErrorHandler())
                .build();
    }

    public void validarCPF(String cpf) {
        UserInfoResponse userInfoResponse = restTemplate.getForObject(USER_INFO_URL + cpf, UserInfoResponse.class);

        Assert.notNull(userInfoResponse, CPF_NAO_PODE_VOTAR);
        Assert.isTrue(UserInfoResponse.UserInfoStatus.ABLE_TO_VOTE.equals(userInfoResponse.getStatus()), CPF_NAO_PODE_VOTAR);
    }

}
