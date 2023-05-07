package com.bankin.app.repository;

import com.bankin.app.entity.Account;
import com.bankin.app.entity.transaction.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(long userId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
