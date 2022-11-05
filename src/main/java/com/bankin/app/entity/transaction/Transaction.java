package com.bankin.app.entity.transaction;

import com.bankin.app.entity.Auditable;
import com.bankin.app.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Transaction extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long transactionId;

    @Column(name="acc_number")
    protected String accountNumber;

    @Column(name="transaction_type")
    @Enumerated(EnumType.STRING)
    protected TransactionType transactionType;

    @Column(name="amount")
    protected String amount;

    public Transaction(String accountNumber, String amount){
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

}
