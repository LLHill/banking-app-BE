package com.bankin.app.dto;

import com.bankin.app.entity.Account;
import com.bankin.app.enums.CardStatus;
import com.bankin.app.enums.CardType;
import lombok.Data;

import java.util.Date;

@Data
public class AbstractCard {
    private String cardNumber;
    private String PIN;
    private CardType type;
    private CardStatus status;
    private Account linkedAccount;
    private Date validFrom;
    private Date goodThru;
}
