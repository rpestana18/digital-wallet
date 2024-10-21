package com.rodrigopestana.walletserviceassignment.service;

import com.rodrigopestana.walletserviceassignment.exception.WalletBusinessException;
import com.rodrigopestana.walletserviceassignment.model.Operation;
import com.rodrigopestana.walletserviceassignment.model.Transaction;
import com.rodrigopestana.walletserviceassignment.model.Wallet;
import com.rodrigopestana.walletserviceassignment.repository.TransactionRepository;
import com.rodrigopestana.walletserviceassignment.repository.WalletRepository;
import com.rodrigopestana.walletserviceassignment.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {

    private static final Logger logger = LogManager.getLogger(WalletService.class);
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean getWallet(Long customerId) {
        return walletRepository.findByCustomerId(customerId).isPresent();
    }

    public Long createWallet(Long customerId) {

        Wallet wallet = new Wallet();
        wallet.setCustomerId(customerId);
        wallet.setBalance(BigDecimal.ZERO);
        saveWallet(wallet);

        logger.info("Wallet created for the customerId {}.", customerId);
        logTransaction(wallet, wallet.getBalance(), Operation.CREATE_WALLET);

        return wallet.getId();
    }

    public BigDecimal retrieveBalance(Long customerId) {
        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "Wallet not found for the customerId: " + customerId));

        BigDecimal balance = wallet.getBalance();
        logTransaction(wallet, balance, Operation.RETRIEVE_BALANCE);

        logger.info("Balance retrieved for the customerId {}.", customerId);
        return balance;
    }


    public BigDecimal retrieveHistoricalBalance(Long customerId, String timestamp) {

        LocalDateTime createdDateTime = DateTimeUtil.convertStringToDateTime(timestamp);

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "Wallet not found for user: " + customerId));

        Transaction transaction = transactionRepository.
                findByWalletIdAndCreatedDateTime(wallet.getId(), createdDateTime);


        logTransaction(wallet, transaction.getAmount(), Operation.RETRIEVE_HISTORICAL_BALANCE);


        return transaction.getAmount();
    }

    public BigDecimal deposit(Long customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("The amount to deposit must be greater than zero. " +
                    "customerId: {}. Amount informed: {}", customerId, amount);
            throw new WalletBusinessException("INVALID_AMOUNT", "The amount to deposit must be greater than zero.");
        }

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "customerId not found for the wallet identified by: " + customerId));

        wallet.setBalance(wallet.getBalance().add(amount));
        saveWallet(wallet);
        logTransaction(wallet, amount, Operation.DEPOSIT);

        logger.info("Deposit for the customerId {}.", customerId);
        return wallet.getBalance();
    }

    public void withdraw(Long customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Withdrawal amount must be positive. Amount informed: {}", amount);
            throw new WalletBusinessException("INVALID_AMOUNT", "Withdrawal amount must be positive");
        }

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "Wallet not found for customerId: " + customerId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new WalletBusinessException("INSUFFICIENT_FUNDS", "Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        saveWallet(wallet);
        logTransaction(wallet, amount.negate(), Operation.WITHDRAW);

        logger.info("Withdrawal for the customerId {}.", customerId);
    }

    public void transfer(Long customerSenderId, Long customerReceiverId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Transfer amount must be positive. Amount informed: {}", amount);
            throw new WalletBusinessException("INVALID_AMOUNT", "Transfer amount must be positive");
        }

        Wallet senderWallet = walletRepository.findByCustomerId(customerSenderId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "Sender Wallet not found: " + customerSenderId));

        Wallet receiverWallet = walletRepository.findByCustomerId(customerReceiverId)
                .orElseThrow(() -> new WalletBusinessException("WALLET_NOT_FOUND",
                        "Receiver Wallet not found: " + customerReceiverId));

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient funds for transfer. Amount informed: {}", amount);
            throw new WalletBusinessException("INSUFFICIENT_FUNDS", "Insufficient funds for transfer");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        saveWallet(senderWallet);
        saveWallet(receiverWallet);

        logTransaction(senderWallet, amount.negate(), Operation.TRANSFER_SENDER);
        logTransaction(receiverWallet, amount, Operation.TRANSFER_RECEIVER);
    }


    /*private methods*/
    private void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
        logger.info("Wallet saved for the customerId: {}", wallet.getCustomerId());
    }

    private void logTransaction(Wallet wallet, BigDecimal amount, Operation operation) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setOperation(operation);
        transaction.setCreatedDateTime(LocalDateTime.now());
        logger.info("Log Transaction. Operation: {}", operation);
        transactionRepository.save(transaction);
    }
}