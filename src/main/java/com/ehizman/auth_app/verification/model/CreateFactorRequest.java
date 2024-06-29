package com.ehizman.auth_app.verification.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateFactorRequest {
    private String userId;
    private String userName;
}
