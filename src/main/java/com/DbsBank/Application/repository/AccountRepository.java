package com.DbsBank.Application.repository;

import com.DbsBank.Application.entity.Account;
import com.DbsBank.Application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUserAndAccountType(User user, String accountType);

    Account findByAccountNumber(String accountNumber);

    // Custom query to find accounts by account number
    List<Account> findByUser_AccountNumber(String accountNumber);

    // Custom query to fetch accounts based on account type for a user
    List<Account> findByAccountTypeAndUser_AccountNumber(String accountType, String accountNumber);
}


