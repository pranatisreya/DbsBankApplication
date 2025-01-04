package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.Transactiondto;
import com.DbsBank.Application.entity.Transaction;
import com.DbsBank.Application.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransactionServiceImple implements TransactionService {

    @Autowired
    TransactionRespository transactionRespository;

    @Override
    public void addTransaction(Transactiondto transactiondto) {

        Transaction transaction = Transaction.builder()
                .transactionType(transactiondto.getTransactionType())
                .accountNumber(transactiondto.getAccountNumber())
                .amount(transactiondto.getAmount())
                .TransactionStatus("Success")
                .build();

        transactionRespository.save(transaction);

        System.out.println("Transaction added");

    }

}
