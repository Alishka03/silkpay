package com.silkpay.techtask.service;

import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.Account;
import com.silkpay.techtask.models.Transaction;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.pojo.AccountDto;
import com.silkpay.techtask.repository.AccountRepository;
import com.silkpay.techtask.repository.TransactionRepository;
import com.silkpay.techtask.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService {
    final UserRepository userRepository;
    final TransactionRepository transactionRepository;
    final AccountRepository accountRepository;
    final UserService userService;

    public boolean isOwnerOfAccount(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        return account != null && account.getUser().getId().equals(userId);
    }

    public Set<Account> getAccounts() {
        User user = userService.getAuthenticatedUser();
        System.out.println(user.toString());
        System.out.println(user.getAccounts());
        return user.getAccounts();
    }
    public Account createAccount(AccountDto accountDto){
        Account account = new Account();
        account.setBalance(accountDto.getBalance());
        User user = userService.getAuthenticatedUser();
        account.setUser(user);
        user.getAccounts().add(account);
        return accountRepository.save(account);
    }
    public void transferMoney(Long senderAccountId, Long receiverAccountId, BigDecimal amount) {
        Account senderAccount = accountRepository.findById(senderAccountId).orElseThrow(() -> new ApiRequestException("Sender account not found"));
        Account receiverAccount = accountRepository.findById(receiverAccountId).orElseThrow(() -> new ApiRequestException("Receiver account not found"));

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new ApiRequestException("Insufficient balance in sender account");
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .build();

        transactionRepository.save(transaction);
    }
}
