package com.bankin.app.service;

import com.bankin.app.dto.TotalTransPerDay;
import com.bankin.app.dto.TransInfo;
import com.bankin.app.dto.TransPerDay;
import com.bankin.app.dto.req.TransferReq;
import com.bankin.app.dto.resp.TransferResp;
import com.bankin.app.entity.Account;
import com.bankin.app.entity.transaction.Transfer;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.AccountRepository;
import com.bankin.app.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bankin.app.utils.DateConverter.strConverter;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    public List<TransInfo> getRecentTransfer(String accountNumber){
        log.debug("Getting recent transfer in account: " + accountNumber);
        List<Transfer> transferList = transferRepository.findTop3ByAccountNumberOrderByLastModifiedDateDesc(accountNumber);
        return createInfoList(transferList);
    }

    public List<TotalTransPerDay> getBalanceChange(long userId) throws ServiceException {
        log.debug("Getting account number of userId: " + userId);
        String accountNumber = accountRepository.findAccountByUserId(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
        Date dateBefore10days = DateUtils.addDays(new Date(), -10);
        log.debug("Getting transaction info within 20 days in account: " + accountNumber);
        List<Transfer> transferList = transferRepository.findByAccountNumberAndLastModifiedDateAfter(accountNumber, dateBefore10days);
        List<Transfer> receiveList = transferRepository.findByTransferredAccountAndLastModifiedDateAfter(accountNumber, dateBefore10days);
        return computeTotalTransPerDay(
                getTransPerDayInfo(transferList, dateBefore10days),
                getTransPerDayInfo(receiveList, dateBefore10days));
    }

    public TransferResp createTransfer(long userId, TransferReq transferReq) throws ServiceException {
        log.debug("Creating transfer with info: " + transferReq);
        String transferAmount = transferReq.getAmount();
        String transferMessage = transferReq.getMessage();
        String destAcc = transferReq.getTransferredAccount();
        log.info("Get source account with with user id: " + userId);
        Account source =  accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ServiceException("User does not exist ---id: " + userId));
        log.info("Get destination account with with account number: " + destAcc);
        Account destination = accountRepository.findByAccountNumber(destAcc)
                .orElseThrow(() -> new ServiceException("Account does not exist ---account number: " + destAcc));
        String newSrcBalance = computeTransfer(source.getBalance(), transferAmount, true);
        String newDestBalance = computeTransfer(destination.getBalance(), transferAmount, false);
        source.setBalance(newSrcBalance);
        destination.setBalance(newDestBalance);
        accountRepository.save(source);
        accountRepository.save(destination);
        saveTransfer(source, destination, transferMessage, transferAmount);
        return TransferResp.builder()
                .transferredAccount(destination.getAccountNumber())
                .message(transferMessage)
                .newBalance(newSrcBalance)
                .amount(transferAmount)
                .build();
    }

    private List<TransInfo> createInfoList(List<Transfer> transferList){
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

    private String computeTransfer(String srcAmount, String addedAmount, boolean isSource) throws ServiceException {
        BigDecimal srcNum = new BigDecimal(srcAmount.replaceAll(",", ""));
        BigDecimal addedNum = new BigDecimal(addedAmount.replaceAll(",", ""));
        if (!isSource){
            return String.valueOf(srcNum.add(addedNum));
        }
        if (addedNum.compareTo(srcNum) > 0) {
            throw new ServiceException("Your account does not have enough money");
        }
        return String.valueOf(srcNum.subtract(addedNum));
    }

    private void saveTransfer(Account src, Account dest, String transferMessage, String transferAmount){
        Transfer transfer = Transfer.builder()
                .transferredAccount(dest.getAccountNumber())
                .accountNumber(src.getAccountNumber())
                .message(transferMessage)
                .amount(transferAmount)
                .build();
        transferRepository.save(transfer);
    }

    private List<TransPerDay> getTransPerDayInfo(List<Transfer> transList, Date startDate){
        List<TransPerDay> transPerDays = new ArrayList<>();
        BigDecimal dayTotal = BigDecimal.valueOf(0);
        int i = 0;
        Date tomorrow = DateUtils.addDays(new Date(), 1);
        while (!DateUtils.isSameDay(startDate, tomorrow)) {
            if (i < transList.size() &&
                    DateUtils.isSameDay(transList.get(i).getLastModifiedDate(), startDate)){
                BigDecimal amount = new BigDecimal(transList.get(i).getAmount().replaceAll(",", ""));
                dayTotal = dayTotal.add(amount);
                i++;
            } else {
                String strDate = strConverter("dd/MM", startDate);
                TransPerDay transPerDay = TransPerDay.builder()
                        .transAmount(String.valueOf(dayTotal))
                        .modifiedDate(strDate).build();
                transPerDays.add(transPerDay);
                startDate = DateUtils.addDays(startDate, 1);
                dayTotal = BigDecimal.valueOf(0);
            }
        }
        return transPerDays;
    }

    private List<TotalTransPerDay> computeTotalTransPerDay (List<TransPerDay> transferList, List<TransPerDay> receiveList){
        List<TotalTransPerDay> totalTrans = new ArrayList<>();
        for (int i = 0; i < transferList.size(); i++) {
            TotalTransPerDay total = TotalTransPerDay.builder()
                    .transAmount(transferList.get(i).getTransAmount())
                    .receiveAmount(receiveList.get(i).getTransAmount())
                    .modifiedDate(transferList.get(i).getModifiedDate())
                    .build();
            totalTrans.add(total);
        }
        return totalTrans;
    }
}
