package com.DbsBank.Application.controller;

import com.DbsBank.Application.dto.TransactionRequest;
import com.DbsBank.Application.dto.Transactiondto;
import com.DbsBank.Application.entity.Transaction;
import com.DbsBank.Application.service.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bankapi/customer/transactions")
@AllArgsConstructor
public class TransactionController {

    private final BankStatement bankStatement;

    @PostMapping
    public List<Transactiondto> generateBankStatement(@RequestBody TransactionRequest transactionRequest) {
        List<Transaction> transactions = bankStatement.generateStatement(transactionRequest);
        // Map entities to DTOs
        return transactions.stream()
                .map(transaction -> Transactiondto.builder()
                        .transactionId(transaction.getTransactionId())
                        .transactionType(transaction.getTransactionType())
                        .amount(transaction.getAmount())
                        .accountNumber(transaction.getAccountNumber())
                        .build())
                .collect(Collectors.toList());
    }
}
