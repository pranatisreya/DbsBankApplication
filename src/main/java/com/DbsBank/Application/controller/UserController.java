package com.DbsBank.Application.controller;

import com.DbsBank.Application.dto.*;
import com.DbsBank.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankapi")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/customer")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
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
