package com.DbsBank.Application.dto;

import java.time.LocalDate;
// import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String email;
    private Integer age;
    private String gender;
    private LocalDate dateofBirth;
    private String address;
    private String state;
    private String country;
    private String password;
    private String accountType;

}
