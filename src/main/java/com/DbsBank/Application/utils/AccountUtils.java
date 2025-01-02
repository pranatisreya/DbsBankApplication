package com.DbsBank.Application.utils;

import java.time.Year;

public class AccountUtils {

    public static final String Account_Exists_Code = "001";
    public static final String Account_Exists_Message = "User with email already exists";
    public static final String Account_Creation_Success = "002";
    public static final String Account_Creation_Message = "Account created successfully";
    public static final String Account_Creation_Fail = "003";

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
