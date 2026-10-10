package com.frauddetection.repository;

import com.frauddetection.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByOrderByTransactionTimeDesc();

    List<Transaction> findByStatusOrderByTransactionTimeDesc(String status);

    long countByStatus(String status);

    /** Velocity check: how many transactions did this card make since a given time? */
    long countByCardHashAndTransactionTimeAfter(String cardHash, LocalDateTime since);

    long countByCardHash(String cardHash);

    /** Average amount of this card's previously approved transactions. */
    @Query("select avg(t.amount) from Transaction t where t.cardHash = :hash and t.status = 'APPROVED'")
    BigDecimal averageApprovedAmount(@Param("hash") String hash);

    /** Most recent transaction of this card (used for the location-change check). */
    Transaction findFirstByCardHashOrderByTransactionTimeDesc(String cardHash);

    @Query("select sum(t.amount) from Transaction t where t.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") String status);
}
