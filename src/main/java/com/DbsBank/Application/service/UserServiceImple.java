package com.DbsBank.Application.service;

import com.DbsBank.Application.config.JwtTokenProvider;
import com.DbsBank.Application.dto.*;
import com.DbsBank.Application.entity.Account;
import com.DbsBank.Application.entity.Role;
import com.DbsBank.Application.entity.User;
import com.DbsBank.Application.repository.UserRepository;
import com.DbsBank.Application.repository.AccountRepository;
import com.DbsBank.Application.utils.AccountUtils;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImple implements UserService {

        @Autowired
        private TransactionService transactionService;

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
                        userToCredit.setAccountBalance(
                                        userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
                        userRepository.save(userToCredit);

                        Transactiondto transactiondto = Transactiondto.builder()
                                        .accountNumber(creditDebitRequest.getAccountNumber())
                                        .amount(creditDebitRequest.getAmount())
                                        .transactionType("Credit")
                                        .build();
                        transactionService.addTransaction(transactiondto);

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
                        logger.error("Account credit failed for account: {}. Error: {}",
                                        creditDebitRequest.getAccountNumber(), e.getMessage());

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
                                        .responseCode(AccountUtils.Account_Debit_NOT_Success)
                                        .responseMessage(AccountUtils.Account_Debit_NOT_Success_Message)
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

                                userToDebit.setAccountBalance(userToDebit.getAccountBalance()
                                                .subtract(creditDebitRequest.getAmount()));
                                userRepository.save(userToDebit);

                                // Log the debit transaction
                                Transactiondto transactiondto = Transactiondto.builder()
                                                .accountNumber(creditDebitRequest.getAccountNumber())
                                                .amount(creditDebitRequest.getAmount())
                                                .transactionType("Debit")
                                                .build();
                                transactionService.addTransaction(transactiondto);

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
                        } catch (Exception e) {
                                // Log the error and return failure response
                                logger.error("Amount Debit failed for account: {}. Error: {}",
                                                creditDebitRequest.getAccountNumber(), e.getMessage());

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
                                        .responseMessage("The recipient account does not exist.")
                                        .accountInfo(null)
                                        .build();
                }

                boolean isFromAccountExists = userRepository.existsByAccountNumber(transferRequest.getFromAccount());
                if (!isFromAccountExists) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Account_NOT_Exists_Code)
                                        .responseMessage("The sender account does not exist.")
                                        .accountInfo(null)
                                        .build();
                }

                User fromUser = userRepository.findByAccountNumber(transferRequest.getFromAccount());
                User toUser = userRepository.findByAccountNumber(transferRequest.getToAccount());

                // Check if the sender has enough balance
                if (fromUser.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Amount_To_Transfer_NOT_Success)
                                        .responseMessage("Insufficient balance in the sender's account.")
                                        .accountInfo(AccountInfo.builder()
                                                        .accountName(fromUser.getFirstName() + " "
                                                                        + fromUser.getMiddleName() + " "
                                                                        + fromUser.getLastName())
                                                        .accountBalance(fromUser.getAccountBalance())
                                                        .accountNumber(fromUser.getAccountNumber())
                                                        .build())
                                        .build();
                }

                try {
                        // Deduct amount from the sender's account
                        fromUser.setAccountBalance(fromUser.getAccountBalance().subtract(transferRequest.getAmount()));
                        userRepository.save(fromUser);
                        EmailDetails debitAlert = EmailDetails.builder()
                                        .subject("Debit Alert")
                                        .receipient(fromUser.getEmail())
                                        .body("The sum of " + transferRequest.getAmount()
                                                        + " has been deducted from your account")
                                        .build();
                        emailService.sendEmail(debitAlert);

                        Transactiondto transactiondto = Transactiondto.builder()
                                        .accountNumber(fromUser.getAccountNumber())
                                        .amount(transferRequest.getAmount())
                                        .transactionType("Debit")
                                        .build();
                        transactionService.addTransaction(transactiondto);

                        // Add amount to the recipient's account
                        toUser.setAccountBalance(toUser.getAccountBalance().add(transferRequest.getAmount()));
                        userRepository.save(toUser);
                        EmailDetails creditAlert = EmailDetails.builder()
                                        .subject("Credit Alert")
                                        .receipient(toUser.getEmail())
                                        .body("The sum of " + transferRequest.getAmount()
                                                        + " has been added to your account from account"
                                                        + fromUser.getAccountNumber() + "," + fromUser.getFirstName()
                                                        + "," + fromUser.getMiddleName() + "," + fromUser.getLastName()
                                                        + fromUser.getMiddleName() + fromUser.getLastName())
                                        .build();
                        emailService.sendEmail(creditAlert);

                        Transactiondto transactiondtocredit = Transactiondto.builder()
                                        .accountNumber(toUser.getAccountNumber())
                                        .amount(transferRequest.getAmount())
                                        .transactionType("Credit")
                                        .build();
                        transactionService.addTransaction(transactiondtocredit);

                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Amount_To_Transfer_Success)
                                        .responseMessage("Transfer successful.")
                                        .accountInfo(AccountInfo.builder()
                                                        .accountName(fromUser.getFirstName() + " "
                                                                        + fromUser.getMiddleName() + " "
                                                                        + fromUser.getLastName())
                                                        .accountBalance(fromUser.getAccountBalance())
                                                        .accountNumber(fromUser.getAccountNumber())
                                                        .build())
                                        .build();
                } catch (Exception e) {
                        logger.error("Transfer failed. From Account: {}, To Account: {}, Error: {}",
                                        transferRequest.getFromAccount(), transferRequest.getToAccount(),
                                        e.getMessage());

                        return BankResponse.builder()
                                        .responseCode(AccountUtils.Amount_To_Transfer_NOT_Success)
                                        .responseMessage("Transfer failed due to an unexpected error.")
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
        public UserDetailsResponse AccountEnquiry(EnquiryRequest enquiryRequest) {

                boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
                if (!isAccountExists) {
                        return UserDetailsResponse.builder()
                                .responseCode(AccountUtils.Account_NOT_Exists_Code)
                                .responseMessage(AccountUtils.Account_NOT_Exists_Message)
                                .userDetailsInfo(null)
                                .build();
                }

                // Fetch the user details
                User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

                // Build and return the response
                return UserDetailsResponse.builder()
                        .responseCode(AccountUtils.Account_Exists_Code)
                        .responseMessage(AccountUtils.Account_Exists_Message)
                        .userDetailsInfo(UserDetailsInfo.builder()
                                .firstName(foundUser.getFirstName())
                                .middleName(foundUser.getMiddleName())
                                .lastName(foundUser.getLastName())
                                .email(foundUser.getEmail())
                                .phoneNumber(foundUser.getPhoneNumber())
                                .alternatePhoneNumber(foundUser.getAlternatePhoneNumber())
                                .age(foundUser.getAge())
                                .gender(foundUser.getGender())
                                .dateofBirth(foundUser.getDateOfBirth())
                                .address(foundUser.getAddress())
                                .state(foundUser.getState())
                                .country(foundUser.getCountry())
                                .accountType(foundUser.getAccountType())
                                .accountBalance(foundUser.getAccountBalance())
                                .accountNumber(foundUser.getAccountNumber())
                                .accountStatus(foundUser.getAccountStatus())
                                .createdAt(foundUser.getCreatedAt())
                                .updatedAt(foundUser.getUpdatedAt())
                                .build())
                        .build();
        }



        private static final Logger logger = LoggerFactory.getLogger(UserServiceImple.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private EmailService emailService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public AccountDetailsResponse getAccountsDetails(EnquiryRequest enquiryRequest) {
                // 'user' table
                User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

                if (user == null) {
                        return AccountDetailsResponse.builder()
                                        .responseCode("404")
                                        .responseMessage("User not found for the given account number")
                                        .accountInfoList(null)
                                        .build();
                }

                List<AccountInfo> accountInfoDTOS = new ArrayList<>();

                accountInfoDTOS.add(AccountInfo.builder()
                                .accountType("Savings")
                                .accountBalance(user.getAccountBalance())
                                .accountNumber(user.getAccountNumber())
                                .accountName(user.getFirstName() + " " + user.getMiddleName() + " "
                                                + user.getLastName())
                                .build());

                // 'account' table
                List<Account> accounts = accountRepository.findByUser_AccountNumber(enquiryRequest.getAccountNumber());

                accounts.stream().map(account -> AccountInfo.builder()
                                .accountType(account.getAccountType())
                                .accountBalance(account.getAccountBalance())
                                .accountNumber(account.getAccountNumber())
                                .accountName(user.getFirstName() + " " + user.getMiddleName() + " "
                                                + user.getLastName())
                                .build()).forEach(accountInfoDTOS::add);

                return AccountDetailsResponse.builder()
                                .responseCode("200")
                                .responseMessage("Accounts fetched successfully")
                                .accountInfoList(accountInfoDTOS)
                                .build();
        }

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
                                .dateOfBirth(userRequest.getDateofBirth())
                                .address(userRequest.getAddress())
                                .state(userRequest.getState())
                                .country(userRequest.getCountry())
                                .accountType(userRequest.getAccountType())
                                .password(passwordEncoder.encode(userRequest.getPassword()))
                                .accountStatus("Active") // Default account status
                                .role(Role.valueOf("ROLE_USER"))
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

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtTokenProvider jwtTokenProvider;

        @Override
        public BankResponse login(LoginRequest loginRequest) {

                Authentication authentication = null;
                authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                loginRequest.getPassword()));

                EmailDetails loginAlert = EmailDetails.builder()
                                .subject("Logged In")
                                .receipient(loginRequest.getEmail())
                                .body("You are logged into your account, if You did not initiate this request please contact the bank!")
                                .build();

                emailService.sendEmail(loginAlert);

                // // Validate account number
                // boolean customerexists =
                // userRepository.existsByAccountNumber(loginRequest.getAccountNumber());

                // if (!customerexists) {
                // throw new RuntimeException("Invalid username or password");
                // }

                // User customer =
                // userRepository.findByAccountNumber(loginRequest.getAccountNumber());

                // // Validate password
                // // if (!passwordEncoder.matches(loginRequest.getPassword(),
                // // customer.getPassword())) {
                // // throw new RuntimeException("Invalid username or password");
                // // }

                // if (!(loginRequest.getPassword().equals(customer.getPassword()))) {
                // throw new RuntimeException("Invalid username or password");
                // }

                // Generate session ID
                // String sessionId = UUID.randomUUID().toString();

                // Set response
                return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Login_Successful)
                                .responseMessage(AccountUtils.Account_Login_Success_Message + " "
                                                + jwtTokenProvider.generateToken(authentication))
                                .accountInfo(null)
                                .build();

        }

        @Autowired
        private AccountRepository accountRepository;

        @Override
        public BankResponse createAdditionalAccount(AccountRequest accountRequest) {
                // Validate account type
                if (!accountRequest.getAccountType().equalsIgnoreCase("Credit")
                                && !accountRequest.getAccountType().equalsIgnoreCase("Loan")) {
                        throw new IllegalArgumentException("Invalid account type. Only 'Credit' or 'Loan' is allowed.");
                }

                boolean customerexists = userRepository.existsByAccountNumber(accountRequest.getAccountNumber());
                if (!customerexists) {
                        throw new RuntimeException("Invalid username or password User Savings account does not exist.");
                }

                User user = userRepository.findByAccountNumber(accountRequest.getAccountNumber());

                // Create the new account
                Account newAccount = Account.builder()
                                .accountType(accountRequest.getAccountType())
                                .accountNumber(accountRequest.getAccountNumber()) // Use the existing account number
                                .accountBalance(BigDecimal.valueOf(0.0))
                                .accountStatus("Active")
                                .user(user)
                                .build();

                Account savedAccount = accountRepository.save(newAccount);

                return BankResponse.builder()
                                .responseCode(AccountUtils.Account_Creation_Success)
                                .responseMessage("New " + accountRequest.getAccountType()
                                                + " account created successfully.")
                                .accountInfo(AccountInfo.builder()
                                                .accountNumber(savedAccount.getAccountNumber())
                                                .accountType(savedAccount.getAccountType())
                                                .accountBalance(savedAccount.getAccountBalance())
                                                .accountName(user.getFirstName() + " " + user.getLastName())
                                                .build())
                                .build();
        }

}
