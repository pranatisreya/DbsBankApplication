package com.DbsBank.Application.service;

import com.DbsBank.Application.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImple implements EmailService {
    @Autowired
    private JavaMailSender javamailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        if (emailDetails.getReceipient() == null || emailDetails.getSubject() == null || emailDetails.getBody() == null) {
            throw new IllegalArgumentException("Recipient, Subject, and Body must not be null");
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getReceipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getBody());

            System.out.println("Sending email to: " + emailDetails.getReceipient());
            javamailSender.send(mailMessage);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
