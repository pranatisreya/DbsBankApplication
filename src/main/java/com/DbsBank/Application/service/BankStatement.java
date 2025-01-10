package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.TransactionRequest;
import com.DbsBank.Application.entity.Transaction;
import com.DbsBank.Application.repository.BankStatementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BankStatement {

    private final BankStatementRepository bankStatementRepository;

    public BankStatement(BankStatementRepository bankStatementRepository) {
        this.bankStatementRepository = bankStatementRepository;
    }

    public List<Transaction> generateStatement(TransactionRequest transactionRequest) {
        try {
            // Parse dates
            LocalDate start = LocalDate.parse(transactionRequest.getStartDate(), DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(transactionRequest.getEndDate(), DateTimeFormatter.ISO_DATE);
            // Use repository method to fetch results
            return bankStatementRepository.findByAccountNumberAndDateBetween(transactionRequest.getAccountNumber(), start, end);
        } catch (Exception e) {
            throw new RuntimeException("Error generating bank statement: " + e.getMessage(), e);
        }
    }
}
