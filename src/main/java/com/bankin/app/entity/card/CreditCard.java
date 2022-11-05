package com.bankin.app.entity.card;
import com.bankin.app.dto.AbstractCard;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CreditCard", schema = "dbo")
public class CreditCard extends Card {

    @Column
    private String balance;

    @Column
    private String CVV;

    public CreditCard(AbstractCard abstractCard){
        super(abstractCard);
    }

    public CreditCard() {
        super();
    }
}
