package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);

    BankResponse login(LoginRequest loginRequest);

    BankResponse createAdditionalAccount(AccountRequest accountRequest);

    UserDetailsResponse AccountEnquiry(EnquiryRequest enquiryRequest);

    AccountDetailsResponse getAccountsDetails(EnquiryRequest enquiryRequest);
}
