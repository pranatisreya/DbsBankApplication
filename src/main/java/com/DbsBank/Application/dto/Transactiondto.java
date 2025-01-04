package com.DbsBank.Application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactiondto {

    private String transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
}
