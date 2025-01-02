package com.DbsBank.Application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String email;
    private String gender;
    private Integer age;
    private String address;
    private String state;
    private String country;

    private String accountNumber;
    private String accountType;

    private BigDecimal accountBalance;
    private String accountStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
//    private String password;
}
