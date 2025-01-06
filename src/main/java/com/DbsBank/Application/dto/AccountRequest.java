package com.DbsBank.Application.dto;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountRequest {

    private String accountNumber;
    private String accountType;
    private BigDecimal accountBalance;
}
