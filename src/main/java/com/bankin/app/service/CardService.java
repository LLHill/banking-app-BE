package com.bankin.app.service;

import com.bankin.app.config.CardConfig;
import com.bankin.app.dto.AbstractCard;
import com.bankin.app.dto.req.CardReq;
import com.bankin.app.dto.resp.CardResp;
import com.bankin.app.entity.Account;
import com.bankin.app.entity.card.CreditCard;
import com.bankin.app.enums.CardStatus;
import com.bankin.app.enums.CardType;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.AccountRepository;
import com.bankin.app.repository.CreditCardRepository;
import com.bankin.app.utils.CardGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.DateUtils;

import javax.transaction.Transactional;

import java.util.Date;

import static com.bankin.app.constant.CardConstant.CREDIT;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CardService {

    @Autowired
    CardConfig cardConfig;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    private static final String INIT_BALANCE = "0";

    public CardResp createCard(CardReq cardReq, long userId) throws ServiceException {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
        AbstractCard absCard = new AbstractCard();
        absCard.setCardNumber(CardGenerator.generateCardNumber());
        absCard.setPIN(cardReq.getPIN());
        absCard.setValidFrom(new Date());
        absCard.setLinkedAccount(account);
        absCard.setStatus(CardStatus.INACTIVE);
        absCard.setGoodThru(DateUtils.addYears(absCard.getValidFrom(),cardConfig.getAvailableDuration()));
        CardResp cardResp = new CardResp();
        cardResp.setCardNumber(absCard.getCardNumber());
        switch (cardReq.getCardType()){
            case CREDIT:
                cardResp.setCVV(createCredit(absCard).getCVV());
        }
        return cardResp;
    }

    private CreditCard createCredit(AbstractCard absCard){
        log.debug("Creating credit card with number: {}", absCard.getCardNumber());
        CreditCard card = new CreditCard(absCard);
        card.setBalance(INIT_BALANCE);
        card.setType(CardType.CREDIT_CARD);
        card.setStatus(CardStatus.ACTIVE);
        card.setCVV(CardGenerator.generateCVV());
        log.debug("Saving credit card with number: {}", absCard.getCardNumber());
        return creditCardRepository.save(card);
    }
}
