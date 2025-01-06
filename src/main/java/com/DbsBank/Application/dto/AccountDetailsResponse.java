package com.DbsBank.Application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailsResponse {

    private String responseCode;
    private String responseMessage;
    private List<AccountInfo> accountInfoList;
}
