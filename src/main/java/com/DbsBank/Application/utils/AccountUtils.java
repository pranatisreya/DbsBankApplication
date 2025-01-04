package com.DbsBank.Application.utils;

import java.time.Year;

public class AccountUtils {

    public static final String Account_Exists_Code = "002";
    public static final String Account_Exists_Message = "User with email exists";
    public static final String Account_NOT_Exists_Code = "001";
    public static final String Account_NOT_Exists_Message = "Account does not exists";

    public static final String Account_Creation_Success = "004";
    public static final String Account_Creation_Message = "Account created successfully";
    public static final String Account_Creation_Fail = "003";
    public static final String Account_Creation_Fail_Message = "Account creation failed";

    public static final String Account_Credit_Success = "006";
    public static final String Account_Credit_Success_Message = "Account credit successful";
    public static final String Account_Credit_NOT_Success = "005";
    public static final String Account_Credit_NOT_Success_Message = "Account credit not successful";

    public static final String Account_Debit_Success = "008";
    public static final String Account_Debit_Success_Message = "Account Debit successful";
    public static final String Account_Debit_NOT_Success = "007";
    public static final String Account_Debit_NOT_Success_Message = "Account Debit not successful";


    public static final String Amount_To_Transfer_Success = "010";
    public static final String Amount_To_Transfer_Success_Message = "Sufficient Balance To Transfer";
    public static final String Amount_To_Transfer_NOT_Success = "009";
    public static final String Amount_To_Transfer_NOT_Success_Message = "Insufficient Balance to Transfer";

    public static String generateAccountNumber() {

        // Account number with year + 6-digit random number
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // Generate a random 6-digit number
        int randNumber = (int) (Math.random() * (max - min)) + min;

        String year = String.valueOf(currentYear.getValue());
        String randomNumber = String.valueOf(randNumber);

        // Concatenate year and random number to create the account number
        return year + randomNumber;
    }
}
