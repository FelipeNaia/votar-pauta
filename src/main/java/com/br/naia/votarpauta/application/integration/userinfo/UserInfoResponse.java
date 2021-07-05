package com.br.naia.votarpauta.application.integration.userinfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserInfoResponse implements Serializable {
    private UserInfoStatus status;

    public enum UserInfoStatus {
        ABLE_TO_VOTE,
        UNABLE_TO_VOTE
    }
}
