package com.bankin.app.repository;

import com.bankin.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(long userId);
    @Query("SELECT a.accountNumber from Account a where a.user.id = ?1")
    Optional<String> findAccountByUserId(long userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
