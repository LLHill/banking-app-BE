package com.bankin.app.dto.auth;

import com.bankin.app.entity.AppUser;
import lombok.Data;

@Data
public class RegisterResp {

    private Long userId;

    public RegisterResp(AppUser user){
        this.userId = user.getId();
    }
}
