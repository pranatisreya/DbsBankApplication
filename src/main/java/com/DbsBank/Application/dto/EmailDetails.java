package com.DbsBank.Application.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmailDetails {

    private String receipient;
    private String sender;
    private String subject;
    private String body;
    private String attachment;
}
