package com.is4tech.base.service;

import com.is4tech.base.domain.Tokens;
import com.is4tech.base.dto.EmailDto;
import jakarta.mail.MessagingException;

public interface EmailService {
    public void sendEmail(EmailDto email, String token) throws MessagingException;
}
