package com.DbsBank.Application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_Id")
    private String transactionId;

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String TransactionStatus;

    @CreationTimestamp
    private LocalDate date;

}
