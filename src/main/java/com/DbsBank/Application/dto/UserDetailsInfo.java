package com.DbsBank.Application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsInfo {

    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String email;
    private String gender;
    private Integer age;
    private LocalDate dateofBirth;
    private String address;
    private String state;
    private String country;
    private String accountNumber;
    private String accountType;
    private BigDecimal accountBalance;
    private String accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
