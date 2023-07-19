package com.silkpay.techtask.repository;

import com.silkpay.techtask.models.Account;
import com.silkpay.techtask.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUser(User user);
}
