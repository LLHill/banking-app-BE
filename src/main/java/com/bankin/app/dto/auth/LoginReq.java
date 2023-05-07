package com.bankin.app.dto.auth;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginReq {

    private String phone;

    @ToString.Exclude
    private String password;
}
