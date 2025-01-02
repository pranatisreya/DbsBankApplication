package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.BankResponse;
import com.DbsBank.Application.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
