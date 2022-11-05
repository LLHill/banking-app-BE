package com.bankin.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
public class UserInfoReq {

    private Long id;

    @JsonProperty(value = "birthdate")
    private Date dateOfBirth;

    @ToString.Exclude
    private String password;

    @JsonProperty(value = "firstname")
    private String firstName;

    @JsonProperty(value = "lastname")
    private String lastName;
}
