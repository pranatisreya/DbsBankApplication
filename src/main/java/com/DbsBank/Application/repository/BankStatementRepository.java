package com.DbsBank.Application.repository;

import com.DbsBank.Application.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankStatementRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndDateBetween(String accountNumber, LocalDate startDate, LocalDate endDate);
}

