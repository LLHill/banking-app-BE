package com.bankin.app.entity.transaction;

import com.bankin.app.entity.Account;
import com.bankin.app.enums.TransferType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.bankin.app.enums.TransactionType.TRANSFER;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Transfer", schema = "dbo")
public class Transfer extends Transaction{

    @Column(name = "transfer_acc")
    private String transferredAccount;

    @Column(name = "message")
    private String message;

    @Builder
    public Transfer(String transferredAccount, String message, Account account, String amount) {
        super(account, amount);
        this.transferredAccount = transferredAccount;
        this.message = message;
        this.transactionType = TRANSFER;
    }
}
