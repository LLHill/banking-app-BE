package com.bankin.app.entity.transaction;

import com.bankin.app.entity.Account;
import com.bankin.app.entity.Auditable;
import com.bankin.app.enums.TransactionType;
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

    @ManyToOne
    @JoinColumn(name = "linked_account", nullable = false, referencedColumnName = "acc_number")
    protected Account account;

    @Column(name="transaction_type")
    @Enumerated(EnumType.STRING)
    protected TransactionType transactionType;

    @Column(name="amount")
    protected String amount;

    public Transaction(Account accountNumber, String amount){
        this.account = accountNumber;
        this.amount = amount;
    }

}
