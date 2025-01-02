package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.AccountInfo;
import com.DbsBank.Application.dto.BankResponse;
import com.DbsBank.Application.dto.UserRequest;
import com.DbsBank.Application.entity.User;
import com.DbsBank.Application.repository.UserRepository;
import com.DbsBank.Application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImple implements UserService {
        @Autowired
        private UserRepository userRepository;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {

                // Create new user and validate with existing user with email

                if (userRepository.existsByEmail(userRequest.getEmail())) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Account_Exists_Code)
                                        .responseMessage(AccountUtils.Account_Exists_Message)
                                        .accountInfo(null)
                                        .build();

                }

                User newUser = User.builder()
                                .firstName(userRequest.getFirstName())
                                .middleName(userRequest.getMiddleName())
                                .lastName(userRequest.getLastName())
                                .phoneNumber(userRequest.getPhoneNumber())
                                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                                .email(userRequest.getEmail())
                                .age(userRequest.getAge())
                                .gender(userRequest.getGender())
                                .address(userRequest.getAddress())
                                .state(userRequest.getState())
                                .country(userRequest.getCountry())
                                .accountType(userRequest.getAccountType())
                                .accountStatus("Active") // Default account status
                                .accountNumber(AccountUtils.generateAccountNumber())
                                .accountBalance(BigDecimal.ZERO)
                                .build();

                User savedUser = userRepository.save(newUser);

                return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Creation_Success)
                                .responseMessage(AccountUtils.Account_Creation_Message)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(savedUser.getAccountBalance())
                                                .accountNumber(savedUser.getAccountNumber())
                                                .accountType(savedUser.getAccountType())
                                                .accountName(savedUser.getFirstName() + " " + savedUser.getMiddleName()
                                                                + " " + savedUser.getLastName())
                                                .build())
                                .build();

        }
}
