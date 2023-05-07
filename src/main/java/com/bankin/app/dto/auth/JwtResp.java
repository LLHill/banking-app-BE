package com.bankin.app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResp {
    private long userId;
    private String token;

    public JwtResp(String token){

        this.token = token;
    }
}
