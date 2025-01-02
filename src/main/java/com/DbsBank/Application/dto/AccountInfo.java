package com.DbsBank.Application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

    private String accountNumber;
    private String accountType;
    private BigDecimal accountBalance;
    private String accountName;

}
