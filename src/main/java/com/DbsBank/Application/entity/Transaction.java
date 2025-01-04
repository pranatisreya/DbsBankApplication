package com.DbsBank.Application.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="transactions")
public class Transaction {


    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="transaction_Id")
    private String transactionId;


    private String transactionType;
    private String TransactionStatus;
    private BigDecimal amount;
    @CreationTimestamp
    private Date date;
    private String accountNumber;

}
