package com.bankin.app.service;

import com.bankin.app.config.CardConfig;
import com.bankin.app.dto.TransInfo;
import com.bankin.app.dto.resp.AccountInfo;
import com.bankin.app.dto.resp.CreditInfo;
import com.bankin.app.dto.resp.GeneralInfoRes;
import com.bankin.app.entity.Account;
import com.bankin.app.entity.AppUser;
import com.bankin.app.entity.card.CreditCard;
import com.bankin.app.entity.transaction.Transfer;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.AccountRepository;
import com.bankin.app.repository.TransferRepository;
import com.bankin.app.repository.UserRepository;
import com.bankin.app.utils.AccountGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.bankin.app.enums.AccountStatus.*;
import static com.bankin.app.enums.UserStatus.CREATED;
import static com.bankin.app.enums.UserStatus.FACE_INFO;
import static com.bankin.app.utils.DateConverter.strConverter;

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
    private TransferRepository transRepository;


    public String createAccount(long userId) throws ServiceException{
        AppUser user = userRepository.findById(userId)
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
        return user.getPhoneNumber();
    }

    public GeneralInfoRes getGeneralAccountInfo(long userId) throws ServiceException{
        log.info("Get general info for user with id: " + userId);
        Account account = getAccountByUserId(userId);
        GeneralInfoRes generalInfo = new GeneralInfoRes();
        AccountInfo accountInfo = getAccountInfo(account);
        generalInfo.setAccount(accountInfo);
        generalInfo.setCards(getCreditInfos(account.getCards()));
        generalInfo.setTransInfos(getRecentTransfers(account));
        return generalInfo;
    }

    public void activateAccount(String accountNumber) throws ServiceException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ServiceException("Account does not exist ---account number: " + accountNumber));
        if (Objects.equals(account.getStatus(), INACTIVE)){
            account.setStatus(ACTIVE);
            accountRepository.save(account);
        }
    }

    public Account getAccountByUserId(long userId) throws ServiceException {
        log.info("Get account of user with id: " + userId);
        return accountRepository.findByUserId(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
    }

    public AccountInfo getAccountInfo(Account account){
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNumber(account.getAccountNumber());
        accountInfo.setBalance(account.getBalance());
        accountInfo.setFirstName(account.getUser().getFirstName());
        accountInfo.setLastName(account.getUser().getLastName());
        return accountInfo;
    }

    public void deactivateAccount(String accountNumber) throws ServiceException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ServiceException("Account does not exist ---account number: " + accountNumber));
        if (Objects.equals(account.getStatus(), ACTIVE)){
            account.setStatus(INACTIVE);
            accountRepository.save(account);
        }
    }


    private List<CreditInfo> getCreditInfos(List<CreditCard> cardList){
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

    private List<TransInfo> getRecentTransfers(Account account){
        List<Transfer> transferList = transRepository.findTop3ByAccountOrderByLastModifiedDateDesc(account);
        List<TransInfo> transInfos = new ArrayList<>();

        for (Transfer transfer : transferList){
            TransInfo transInfo = new TransInfo();
            transInfo.setTransferDate(strConverter("dd/MM/yyyy", transfer.getLastModifiedDate()));
            transInfo.setTransferAccount(transfer.getTransferredAccount());
            transInfo.setAmount(transfer.getAmount());
            transInfos.add(transInfo);
        }
        return transInfos;
    }
}
