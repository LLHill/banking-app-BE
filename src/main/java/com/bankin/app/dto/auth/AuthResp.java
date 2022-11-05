package com.bankin.app.dto.auth;

import com.bankin.app.entity.User;
import lombok.Data;

@Data
public class AuthResp {

    private Long userId;

    public AuthResp(User user){
        this.userId = user.getId();
    }
}
