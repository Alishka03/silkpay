package com.silkpay.techtask.pojo;

import java.math.BigDecimal;

public class AccountDto {
    private BigDecimal balance;

    public AccountDto(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountDto() {
    }
}
