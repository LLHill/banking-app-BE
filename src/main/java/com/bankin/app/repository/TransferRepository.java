package com.bankin.app.repository;

import com.bankin.app.entity.Account;
import com.bankin.app.entity.transaction.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findTop3ByAccountOrderByLastModifiedDateDesc(Account account);

    List<Transfer> findByAccountAndLastModifiedDateAfter(Account accountNumber, Date last10days);

    List<Transfer> findByTransferredAccountAndLastModifiedDateAfter(String accountNumber, Date last10days);
}
