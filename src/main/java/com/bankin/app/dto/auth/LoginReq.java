package com.bankin.app.dto.auth;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginReq {

    private String email;

    @ToString.Exclude
    private String password;
}
