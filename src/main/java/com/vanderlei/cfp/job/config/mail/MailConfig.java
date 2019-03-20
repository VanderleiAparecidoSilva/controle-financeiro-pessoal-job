package com.vanderlei.cfp.job.config.mail;

import com.vanderlei.cfp.job.email.EmailService;
import com.vanderlei.cfp.job.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("oauth-security")
public class MailConfig {

  @Bean
  public EmailService emailService() {
    return new SmtpEmailService();
  }
}
