package com.bankin.app.entity.card;

import com.bankin.app.dto.AbstractCard;
import com.bankin.app.entity.Account;
import com.bankin.app.entity.Auditable;
import com.bankin.app.enums.CardStatus;
import com.bankin.app.enums.CardType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Card extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @ToString.Exclude
    @Column(name = "PIN")
    private String PIN;

    @Enumerated(EnumType.STRING)
    private CardType type;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "linked_account_id", nullable = false)
    private Account linkedAccount;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "good_thru")
    private Date goodThru;

    public Card(AbstractCard absCard){
        this.cardNumber = absCard.getCardNumber();
        this.PIN = absCard.getPIN();
        this.status = absCard.getStatus();
        this.linkedAccount = absCard.getLinkedAccount();
        this.validFrom = absCard.getValidFrom();
        this.goodThru = absCard.getGoodThru();
    }
}
