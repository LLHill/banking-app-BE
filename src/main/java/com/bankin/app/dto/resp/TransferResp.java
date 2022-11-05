package com.bankin.app.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResp {
    private String transferredAccount;
    private String amount;
    private String message;
    private String newBalance;
}
