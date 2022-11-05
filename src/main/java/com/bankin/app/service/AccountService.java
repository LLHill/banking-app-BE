package com.bankin.app.service;

import com.bankin.app.config.CardConfig;
import com.bankin.app.dto.resp.CreditInfo;
import com.bankin.app.dto.resp.GeneralInfoRes;
import com.bankin.app.entity.Account;
import com.bankin.app.entity.User;
import com.bankin.app.entity.card.CreditCard;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.AccountRepository;
import com.bankin.app.repository.UserRepository;
import com.bankin.app.utils.AccountGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.bankin.app.enums.AccountStatus.*;
import static com.bankin.app.enums.UserStatus.CREATED;
import static com.bankin.app.enums.UserStatus.FACE_INFO;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private static final String INIT_INTEREST = "0.5";
    private static final String INIT_BALANCE = "0";

    @Autowired
    private CardConfig cardConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;


    public void createAccount(long userId) throws ServiceException{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));

        if (user.getStatus().equals(FACE_INFO)){
            log.info("Create new account for user with id: " + userId);
            user.setStatus(CREATED);
            Account account = new Account();
            account.setAccountNumber(AccountGenerator.generateSequence());
            account.setUser(user);
            account.setInterestRate(INIT_INTEREST);
            account.setBalance(INIT_BALANCE);
            account.setStatus(ACTIVE);
            userRepository.save(user);
            accountRepository.save(account);
        } else if (user.getStatus().equals(CREATED)){
            throw new ServiceException("Account is already created");
        } else {
          throw new ServiceException("User have not taken face data yet");
        }
    }

    public GeneralInfoRes getGeneralAccountInfo(long userId) throws ServiceException{
        log.info("Get general info for user with id: " + userId);
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
        GeneralInfoRes generalInfo = new GeneralInfoRes();
        generalInfo.setAccountNumber(account.getAccountNumber());
        generalInfo.setBalance(account.getBalance());
        generalInfo.setFirstName(account.getUser().getFirstName());
        generalInfo.setLastName(account.getUser().getLastName());
        generalInfo.setCards(getCreditInfos(account.getCards()));
        generalInfo.setTransInfos(transactionService.getRecentTransfer(account.getAccountNumber()));
        return generalInfo;
    }

    private List<CreditInfo> getCreditInfos (List<CreditCard> cardList){
        List<CreditInfo> cardInfos = new ArrayList<>();
        for (CreditCard card : cardList){
            CreditInfo cardInfo = new CreditInfo();
            cardInfo.setBalance(card.getBalance());
            cardInfo.setCardNumber(card.getCardNumber());
            cardInfo.setLimit(cardConfig.getCredit().getLimit());
            cardInfos.add(cardInfo);
        }
        return cardInfos;
    }
}
