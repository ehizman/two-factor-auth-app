package com.ehizman.auth_app.user_management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppUser {
    private String id;
    private String userId;
    private String username;
    private String password;
    private boolean i2FAEnabled;
    private String _2faFactorSid;
}
