package com.bankin.app.dto.req;

import com.bankin.app.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferReq {
    @JsonProperty(value = "account")
    private String transferredAccount;
    @JsonProperty(value = "amount")
    private String amount;
    @JsonProperty(value = "message")
    private String message;
}
