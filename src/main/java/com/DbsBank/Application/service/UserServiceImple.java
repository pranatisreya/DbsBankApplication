package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.AccountInfo;
import com.DbsBank.Application.dto.BankResponse;
import com.DbsBank.Application.dto.EmailDetails;
import com.DbsBank.Application.dto.UserRequest;
import com.DbsBank.Application.entity.User;
import com.DbsBank.Application.repository.UserRepository;
import com.DbsBank.Application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Service
public class UserServiceImple implements UserService {

        private static final Logger logger = LoggerFactory.getLogger(UserServiceImple.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private EmailService emailService;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {

                // Log for incoming request
                logger.info("Creating account for user: {}", userRequest.getEmail());

                // Create new user and validate with existing user with email
                if (userRepository.existsByEmail(userRequest.getEmail())) {
                        logger.info("Account already exists for email: {}", userRequest.getEmail());
                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Exists_Code)
                                .responseMessage(AccountUtils.Account_Exists_Message)
                                .accountInfo(null)
                                .build();
                }

                // Log when the account is being created
                logger.info("Creating a new account for: {}", userRequest.getEmail());

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

                // Save the user
                User savedUser = userRepository.save(newUser);
                logger.info("New user saved with account number: {}", savedUser.getAccountNumber());

                // Sending email alert
                EmailDetails emailDetails = EmailDetails.builder()
                        .receipient(savedUser.getEmail())
                        .subject("Bank Account Created " + savedUser.getFirstName() + " "
                                + savedUser.getMiddleName() + " " + savedUser.getLastName())
                        .body("Your account has been successfully created. Please log in to your account for more details.")
                        .build();

                // Log before sending email
                logger.info("Sending email to: {}", savedUser.getEmail());
                emailService.sendEmail(emailDetails);

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
