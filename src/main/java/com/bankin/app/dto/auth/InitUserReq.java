package com.bankin.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InitUserReq {

    private String email;

    @JsonProperty(value = "phonenumber")
    private String phoneNumber;
}
