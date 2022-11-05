package com.bankin.app.entity;

import com.bankin.app.entity.card.CreditCard;
import com.bankin.app.enums.AccountStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "Account", schema = "dbo")
public class Account extends Auditable<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "linkedAccount")
    private List<CreditCard> cards;

    @Column(name = "acc_number", unique = true)
    private String accountNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "balance")
    private String balance;

    @Column(name = "interest")
    private String interestRate;
}
