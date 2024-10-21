package com.rodrigopestana.walletserviceassignment.controller;

import com.rodrigopestana.walletserviceassignment.dto.CustomerDto;
import com.rodrigopestana.walletserviceassignment.dto.TransferDto;
import com.rodrigopestana.walletserviceassignment.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                    @RequestBody CustomerDto customerDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }
        if (customerDto.getCustomerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerId cannot be null");

        boolean isWalletAvailable = walletService.getWallet(customerDto.getCustomerId());
        if (isWalletAvailable) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This customer has already have a wallet.");
        }

        Long walletId = walletService.createWallet(customerDto.getCustomerId());

        return ResponseEntity.ok("Wallet created with id: " + walletId);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> retrieveBalance(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                             @RequestBody CustomerDto customerDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }

        if (customerDto.getCustomerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerId cannot be null");

        BigDecimal balance = walletService.retrieveBalance(customerDto.getCustomerId());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/balance/history")
    public ResponseEntity<?> retrieveHistoricalBalance(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                                       @RequestBody CustomerDto customerDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }

        if (customerDto.getCustomerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerId cannot be null");

        if (customerDto.gettimestamp() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The timestamp cannot be null");

        BigDecimal balance = walletService.
                retrieveHistoricalBalance(customerDto.getCustomerId(), customerDto.gettimestamp());
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                     @RequestBody CustomerDto customerDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }
        if (customerDto.getCustomerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerId cannot be null");

        if (customerDto.getAmount() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The amount cannot be null");

        walletService.deposit(customerDto.getCustomerId(), customerDto.getAmount());
        return ResponseEntity.ok("Deposit confirmed to wallet of the customer identified by: "
                + customerDto.getCustomerId());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                      @RequestBody CustomerDto customerDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }
        if (customerDto.getCustomerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerId cannot be null");

        if (customerDto.getAmount() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The amount cannot be null");

        walletService.withdraw(customerDto.getCustomerId(), customerDto.getAmount());

        return ResponseEntity.ok("Withdrew Confirmed from wallet of customerId "
                + customerDto.getCustomerId());
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader(value = "x-trace-id", required = true) String traceId,
                                      @RequestBody TransferDto transferDto) {

        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("x-trace-Id must be provided");
        }
        if (transferDto.getCustomerSenderId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerSenderId cannot be null");

        if (transferDto.getCustomerReceiverId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The customerReceiverId cannot be null");

        if (transferDto.getAmount() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The amount cannot be null");

        walletService.transfer(transferDto.getCustomerSenderId(),
                transferDto.getCustomerReceiverId(), transferDto.getAmount());
        return ResponseEntity.ok("Transferred Confirmed. The amount was transferred from " +
                transferDto.getCustomerSenderId() + " to " + transferDto.getCustomerReceiverId());
    }
}

