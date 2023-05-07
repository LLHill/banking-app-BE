package com.bankin.app.dto.resp;

import lombok.Data;

@Data
public class AccountInfo {
    private String balance;
    private String accountNumber;
    private String firstName;
    private String lastName;
}
