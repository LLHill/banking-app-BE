package com.bankin.app.dto.resp;

import lombok.Data;

@Data
public class CreditInfo {

    private String cardNumber;

    private String balance;

    private String limit;
}
