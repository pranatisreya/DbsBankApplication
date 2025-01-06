package com.DbsBank.Application.controller;

import com.DbsBank.Application.dto.*;
import com.DbsBank.Application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankapi")

@Tag(name = "Customer Account Management")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Create new Account", description = "Assigning account number")
    @ApiResponse(responseCode = "004", description = "Account created successfully")
    @PostMapping("/customer")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @PostMapping("/customer/login")
    public BankResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/customer/account")
    public BankResponse createAdditionalAccount(@RequestBody AccountRequest accountRequest) {
        return userService.createAdditionalAccount(accountRequest);
    }

    @GetMapping("/customer/getAccounts")
    public AccountDetailsResponse getAccounts(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.getAccountsDetails(enquiryRequest);
    }

    @GetMapping("/customer/balanceEnquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/customer/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/customer/creditRequest")
    public BankResponse creditRequest(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping("/customer/debitRequest")
    public BankResponse updateAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("/customer/transferRequest")
    public BankResponse transferRequest(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

}
