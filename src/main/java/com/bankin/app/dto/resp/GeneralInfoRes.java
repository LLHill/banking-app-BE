package com.bankin.app.dto.resp;

import com.bankin.app.dto.TransInfo;
import lombok.Data;

import java.util.List;

@Data
public class GeneralInfoRes {
    private String balance;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private List<CreditInfo> cards;
    private List<TransInfo> transInfos;
}
