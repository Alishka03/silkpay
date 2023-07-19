package com.silkpay.techtask.controllers;

import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.Account;
import com.silkpay.techtask.pojo.AccountDto;
import com.silkpay.techtask.service.AccountService;
import com.silkpay.techtask.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AccountController {
    final AccountService accountService;
    final UserService userService;
    @GetMapping("/my-accounts")
    public Set<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @PostMapping("/new-account")
    public ResponseEntity<?> createNewAccount(@RequestBody AccountDto accountDto) {
        BigDecimal s = accountDto.getBalance();
        if (s.intValue() < 0) {
            throw new ApiRequestException("Your balance can not be little than 0");
        }
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestParam(value = "senderAccountId") Long senderAccountId,
            @RequestParam(value = "receiverAccountId") Long receiverAccountId,
            @RequestParam(value = "amount") BigDecimal amount) {
        if (!accountService.isOwnerOfAccount(senderAccountId, userService.getAuthenticatedUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this transfer");
        }

        try {
            accountService.transferMoney(senderAccountId, receiverAccountId, amount);
            return ResponseEntity.ok("Money transfer successful");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Insufficient balance in sender account");
        }
    }
}
