package com.DbsBank.Application.repository;

import com.DbsBank.Application.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRespository extends JpaRepository<Transaction, String> {


}
