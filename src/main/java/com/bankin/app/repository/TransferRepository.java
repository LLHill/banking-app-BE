package com.bankin.app.repository;

import com.bankin.app.entity.transaction.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findTop3ByAccountNumberOrderByLastModifiedDateDesc(String accountNumber);

    List<Transfer> findTop3ByTransferredAccountOrderByLastModifiedDateDesc(String accountNumber);

//    @Query("SELECT a from Transfer a where a.accountNumber = ?1 and a.lastModifiedDate > ?2")
//    List<Transfer> findByAccountNumberAndLastModifiedDateAfter(String accountNumber, Date last30days);

    List<Transfer> findByAccountNumberAndLastModifiedDateAfter(String accountNumber, Date last10days);

    List<Transfer> findByTransferredAccountAndLastModifiedDateAfter(String accountNumber, Date last10days);
}
