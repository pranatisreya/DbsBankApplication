package com.DbsBank.Application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String accountType; // Savings, Credit, Loan

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal accountBalance;

    @Column(nullable = false)
    private String accountStatus; // Active, Closed

    @ManyToOne
    @JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber", nullable = false, insertable = false, updatable = false)
    private User user;
}
