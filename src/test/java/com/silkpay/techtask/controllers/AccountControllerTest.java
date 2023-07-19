package com.silkpay.techtask.controllers;

import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.Account;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.pojo.AccountDto;
import com.silkpay.techtask.service.AccountService;
import com.silkpay.techtask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccounts() {
        Set<Account> accounts = new HashSet<>();
        when(accountService.getAccounts()).thenReturn(accounts);

        Set<Account> result = accountController.getAccounts();

        assertEquals(accounts, result);
        verify(accountService, times(1)).getAccounts();
    }

    @Test
    public void testCreateNewAccount_Success() {
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(BigDecimal.valueOf(1000));

        when(accountService.createAccount(accountDto)).thenReturn(new Account());

        ResponseEntity<?> responseEntity = accountController.createNewAccount(accountDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        verify(accountService, times(1)).createAccount(accountDto);
    }

    @Test
    public void testCreateNewAccount_NegativeBalance() {
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(BigDecimal.valueOf(-500));

        assertThrows(ApiRequestException.class, () -> accountController.createNewAccount(accountDto));
        verify(accountService, never()).createAccount(accountDto);
    }

    @Test
    public void testTransferMoney_Success() {
        Long userId = 1L;
        Long senderAccountId = 10L;
        Long receiverAccountId = 20L;
       BigDecimal amount = BigDecimal.valueOf(500);
        // Replace with an actual user object with the given ID
        User authenticatedUser = new User();
        authenticatedUser.setId(userId);
        when(userService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        // Modify the isOwnerOfAccount method to return true for the given account and user IDs
        when(accountService.isOwnerOfAccount(senderAccountId, userId)).thenReturn(true);

        // Mock the successful transferMoney method call
        doNothing().when(accountService).transferMoney(senderAccountId, receiverAccountId, amount);

        // Call the controller method
        ResponseEntity<String> responseEntity = accountController.transferMoney(senderAccountId, receiverAccountId, amount);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Money transfer successful", responseEntity.getBody());

        // Verify that the transferMoney method was called once with the correct arguments
        verify(accountService, times(1)).transferMoney(senderAccountId, receiverAccountId, amount);
    }
}
