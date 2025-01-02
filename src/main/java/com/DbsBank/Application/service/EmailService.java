package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
