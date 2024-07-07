package com.ehizman.auth_app.user_management;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupForm {
    @NotBlank
    @Size(max = 60)
    private String username;

    @NotBlank
    @Size(max = 30)
    private String password;
}
