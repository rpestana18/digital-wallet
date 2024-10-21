import com.rodrigopestana.walletserviceassignment.model.Transaction;
import com.rodrigopestana.walletserviceassignment.model.Wallet;
import com.rodrigopestana.walletserviceassignment.repository.TransactionRepository;
import com.rodrigopestana.walletserviceassignment.repository.WalletRepository;
import com.rodrigopestana.walletserviceassignment.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class WalletServiceTest {

    private WalletService walletService;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        walletRepository = mock(WalletRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        walletService = new WalletService(walletRepository, transactionRepository);
    }

    @Test
    public void testGetWallet_WhenWalletExists() {
        Long customerId = 1234L;
        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(new Wallet()));

        boolean result = walletService.getWallet(customerId);

        assertTrue(result, "Expected to find the wallet for the given customerId");
        verify(walletRepository).findByCustomerId(customerId);
    }

    @Test
    public void testGetWallet_WhenWalletDoesNotExist() {
        Long customerId = 1234L;
        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        boolean result = walletService.getWallet(customerId);

        assertFalse(result, "Expected not to find a wallet for the given customerId");
        verify(walletRepository).findByCustomerId(customerId);
    }


    @Test
    public void testCreateWallet() {

        Long customerId = 1234L;

        when(walletRepository.save(any(Wallet.class))).thenReturn(new Wallet());
        walletService.createWallet(customerId);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    public void testRetrieveBalance() {
        Long customerId = 1L;
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100.00));

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));

        BigDecimal balance = walletService.retrieveBalance(customerId);

        assertEquals(BigDecimal.valueOf(100.00), balance);
    }

    @Test
    public void testRetrieveHistoricalBalance() {
        Long customerId = 1L;
        LocalDateTime timestamp = LocalDateTime.now();
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100.00));
        wallet.setId(1L);
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(BigDecimal.valueOf(100.00));

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));

        when(transactionRepository.findByWalletIdAndCreatedDateTime(wallet.getId(), timestamp))
                .thenReturn(transaction);

        BigDecimal historicalBalance = walletService.retrieveHistoricalBalance(customerId, timestamp.toString());
        assertEquals(BigDecimal.valueOf(100.00), historicalBalance);
    }

    @Test
    public void testDeposit() {
        Long customerId = 1L;
        BigDecimal depositAmount = BigDecimal.valueOf(50.00);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100.00));
        wallet.setId(1L);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));

        walletService.deposit(customerId, depositAmount);

        assertEquals(BigDecimal.valueOf(150.00), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testWithdraw() {
        Long customerId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(30.00);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100.00));
        wallet.setId(1L);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));

        walletService.withdraw(customerId, withdrawAmount);

        assertEquals(BigDecimal.valueOf(70.00), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testTransfer() {
        Long senderId = 1234L;
        Long receiverId = 5678L;
        BigDecimal transferAmount = BigDecimal.valueOf(20.00);

        Wallet senderWallet = new Wallet();
        senderWallet.setBalance(BigDecimal.valueOf(100.00));
        senderWallet.setId(1L);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setBalance(BigDecimal.valueOf(50.00));
        receiverWallet.setId(2L);

        when(walletRepository.findByCustomerId(senderId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByCustomerId(receiverId)).thenReturn(Optional.of(receiverWallet));

        walletService.transfer(senderId, receiverId, transferAmount);

        assertEquals(BigDecimal.valueOf(80.00), senderWallet.getBalance());
        assertEquals(BigDecimal.valueOf(70.00), receiverWallet.getBalance());

        verify(walletRepository).save(senderWallet);
        verify(walletRepository).save(receiverWallet);
    }
}
