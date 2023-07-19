package com.silkpay.techtask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.Account;
import com.silkpay.techtask.models.Transaction;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.pojo.AccountDto;
import com.silkpay.techtask.repository.AccountRepository;
import com.silkpay.techtask.repository.TransactionRepository;
import com.silkpay.techtask.repository.UserRepository;
import com.silkpay.techtask.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

public class AccountServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.getAuthenticatedUser()).thenReturn(new User());
    }

    @Test
    public void testIsOwnerOfAccount() {
        Long accountId = 1L;
        Long userId = 2L;

        Account account = new Account();
        account.setId(accountId);

        User user = new User();
        user.setId(userId);

        account.setUser(user);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertTrue(accountService.isOwnerOfAccount(accountId, userId));
        assertFalse(accountService.isOwnerOfAccount(accountId, 3L));

        verify(accountRepository, times(2)).findById(accountId);
    }


    @Test
    public void testIsOwnerOfAccount_AccountNotFound() {
        Long accountId = 1L;
        Long userId = 2L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertFalse(accountService.isOwnerOfAccount(accountId, userId));

        verify(accountRepository).findById(accountId);
    }



    @Test
    public void testCreateAccount() {
        User user = new User();
        user.setId(1L); //
        when(userService.getAuthenticatedUser()).thenReturn(user);
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(BigDecimal.valueOf(1000));
        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setBalance(accountDto.getBalance());
        savedAccount.setUser(user);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        Account createdAccount = accountService.createAccount(accountDto);
        assertNotNull(createdAccount);
        assertEquals(accountDto.getBalance(), createdAccount.getBalance());
        assertEquals(user, createdAccount.getUser());
        verify(userService).getAuthenticatedUser();
        verify(accountRepository).save(any(Account.class));
    }


    @Test
    public void testTransferMoney_Successful() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(500);

        Account senderAccount = new Account();
        senderAccount.setId(senderAccountId);
        senderAccount.setBalance(BigDecimal.valueOf(1000));

        Account receiverAccount = new Account();
        receiverAccount.setId(receiverAccountId);
        receiverAccount.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverAccount));

        accountService.transferMoney(senderAccountId, receiverAccountId, amount);

        assertEquals(BigDecimal.valueOf(500), senderAccount.getBalance());
        assertEquals(BigDecimal.valueOf(700), receiverAccount.getBalance());

        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }



    @Test
    public void testTransferMoney_ReceiverAccountNotFound() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(500);

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(new Account()));
        when(accountRepository.findById(receiverAccountId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class,
                () -> accountService.transferMoney(senderAccountId, receiverAccountId, amount));

        verify(accountRepository).findById(senderAccountId);
        verify(accountRepository).findById(receiverAccountId);
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testTransferMoney_InsufficientBalance() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1500);

        Account senderAccount = new Account();
        senderAccount.setId(senderAccountId);
        senderAccount.setBalance(BigDecimal.valueOf(1000));

        Account receiverAccount = new Account();
        receiverAccount.setId(receiverAccountId);
        receiverAccount.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverAccount));

        assertThrows(ApiRequestException.class,
                () -> accountService.transferMoney(senderAccountId, receiverAccountId, amount));

        verify(accountRepository).findById(senderAccountId);
        verify(accountRepository).findById(receiverAccountId);
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}