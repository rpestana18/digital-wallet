package com.rodrigopestana.walletserviceassignment.repository;

import com.rodrigopestana.walletserviceassignment.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByCustomerId(Long customerId);
}
