package com.bankin.app.dto.resp;

import com.bankin.app.dto.TransInfo;
import lombok.Data;

import java.util.List;

@Data
public class GeneralInfoRes {
    private AccountInfo account;
    private List<CreditInfo> cards;
    private List<TransInfo> transInfos;
}
