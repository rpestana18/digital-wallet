package com.rodrigopestana.walletserviceassignment.repository;

import com.rodrigopestana.walletserviceassignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByWalletIdAndCreatedDateTime(Long walletId, LocalDateTime createdDateTime);
}
