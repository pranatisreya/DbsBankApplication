package com.DbsBank.Application.controller;

import com.DbsBank.Application.dto.BankResponse;
import com.DbsBank.Application.dto.CreditDebitRequest;
import com.DbsBank.Application.dto.EnquiryRequest;
import com.DbsBank.Application.dto.UserRequest;
import com.DbsBank.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/user/balanceEnquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }


    @GetMapping("/user/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/user/creditRequest")
    public BankResponse creditRequest(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping("/user/debitRequest")
    public BankResponse updateAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAccount(creditDebitRequest);
    }

}
