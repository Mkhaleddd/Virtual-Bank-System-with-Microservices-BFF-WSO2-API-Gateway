package com.vbank.account_service.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    void addToBalance(@Param("amount") BigDecimal amount, @Param("accountId") UUID accountId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.id = :accountId")
    void subtractFromBalance(@Param("amount") BigDecimal amount, @Param("accountId") UUID accountId);

    @Modifying
    @Query("UPDATE Account a SET a.status = 'INACTIVE' WHERE a.status = 'ACTIVE' AND a.lastActivityAt < :threshold")
    int inactivateStaleAccounts(@Param("threshold") Instant threshold);

    List<Account> findByUserId(UUID userId);

    boolean existsByNumber(String number);


}
