package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.*;
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
        @Override
        public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
                // Check if the account exists
                boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if (!isAccountExists) {
                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_NOT_Exists_Code)
                                .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                                .accountInfo(null)
                                .build();
                }

                // Find the user account to credit
                User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());

                try {
                        // Update the account balance
                        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
                        userRepository.save(userToCredit);

                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Credit_Success)
                                .responseMessage(AccountUtils.Account_Credit_Success_Message)
                                .accountInfo(AccountInfo.builder()
                                        .accountName(userToCredit.getFirstName() + " "
                                                + userToCredit.getMiddleName() + " "
                                                + userToCredit.getLastName())
                                        .accountBalance(userToCredit.getAccountBalance())
                                        .build())
                                .build();
                } catch (Exception e) {
                        // Log the error and return failure response
                        logger.error("Account credit failed for account: {}. Error: {}", creditDebitRequest.getAccountNumber(), e.getMessage());

                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Credit_NOT_Success)
                                .responseMessage(AccountUtils.Account_Credit_NOT_Success_Message)
                                .accountInfo(null)
                                .build();
                }
        }


        @Override
        public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {

                boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if (!isAccountExists) {
                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_NOT_Exists_Code)
                                .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                                .accountInfo(null)
                                .build();
                }

                User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());


                if (userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
                        return BankResponse.builder()
                                .responseCode(AccountUtils.Amount_To_Debit_NOT_Success)
                                .responseMessage(AccountUtils.Amount_To_Debit_NOT_Success_Message)
                                .accountInfo(AccountInfo.builder()
                                        .accountName(userToDebit.getFirstName() + " "
                                                + userToDebit.getMiddleName() + " "
                                                + userToDebit.getLastName())
                                        .accountBalance(userToDebit.getAccountBalance())
                                        .accountNumber(userToDebit.getAccountNumber())
                                        .build())
                                .build();
                }

                else {

                    try {

                        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                        userRepository.save(userToDebit);

                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Debit_Success)
                                .responseMessage(AccountUtils.Account_Debit_Success_Message)
                                .accountInfo(AccountInfo.builder()
                                        .accountName(userToDebit.getFirstName() + " "
                                                + userToDebit.getMiddleName() + " "
                                                + userToDebit.getLastName())
                                        .accountBalance(userToDebit.getAccountBalance())
                                        .accountNumber(userToDebit.getAccountNumber())
                                        .build())
                                .build();
                    }catch (Exception e) {
                        // Log the error and return failure response
                        logger.error("Amount Debit failed for account: {}. Error: {}", creditDebitRequest.getAccountNumber(), e.getMessage());

                        return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Debit_NOT_Success)
                                .responseMessage(AccountUtils.Account_Debit_NOT_Success_Message)
                                .accountInfo(null)
                                .build();
                    }
                }
        }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        boolean isToAccountExists = userRepository.existsByAccountNumber(transferRequest.getToAccount());
        if (!isToAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.Account_NOT_Exists_Code)
                    .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                    .accountInfo(null)
                    .build();
        }

        boolean isFromAccountExists = userRepository.existsByAccountNumber(transferRequest.getFromAccount());
        if (!isFromAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.Account_NOT_Exists_Code)
                    .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                    .accountInfo(null)
                    .build();
        }



    }

    @Override
        public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
                // account exixtence
                boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
                if (!isAccountExists) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Account_NOT_Exists_Code)
                                        .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                                        .accountInfo(null)
                                        .build();
                }

                User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
                return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Exists_Code)
                                .responseMessage(AccountUtils.Account_Exists_Message)
                                .accountInfo(AccountInfo.builder()
                                                .accountName(foundUser.getFirstName() + " " + foundUser.getMiddleName()
                                                                + " " + foundUser.getLastName())
                                                .accountBalance(foundUser.getAccountBalance())
                                                .accountNumber(foundUser.getAccountNumber())
                                                .build())
                                .build();

        }

        @Override
        public String nameEnquiry(EnquiryRequest enquiryRequest) {

                boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
                if (!isAccountExists) {
                        return AccountUtils.Account_NOT_Exists_Message;
                }
                User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
                return foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName();
        }

        private static final Logger logger = LoggerFactory.getLogger(UserServiceImple.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private EmailService emailService;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {
                // Log for incoming request
                logger.info("Creating account for user: {}", userRequest.getEmail());

                // Check if account already exists
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

                try {
                        // Attempt to save the user
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
                                                        .accountName(savedUser.getFirstName() + " "
                                                                        + savedUser.getMiddleName()
                                                                        + " " + savedUser.getLastName())
                                                        .build())
                                        .build();

                } catch (Exception e) {
                        // Handle exceptions or save failures
                        logger.error("Account creation failed for email: {}. Error: {}", userRequest.getEmail(),
                                        e.getMessage());

                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Account_Creation_Fail)
                                        .responseMessage(AccountUtils.Account_Creation_Fail_Message)
                                        .accountInfo(null)
                                        .build();
                }
        }

}
