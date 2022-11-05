package com.bankin.app.dto;

import com.bankin.app.enums.TransferType;
import lombok.Data;

import java.util.Date;

@Data
public class TransInfo {

    private String amount;

    private String transferAccount;

    private String transferDate;
}
