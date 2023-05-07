package com.bankin.app.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountReq {

    @JsonProperty(value = "account")
    String accountNumber;
}
