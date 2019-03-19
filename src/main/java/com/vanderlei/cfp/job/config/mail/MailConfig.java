package com.vanderlei.cfp.job.config.mail;

import com.vanderlei.cfp.job.email.EmailService;
import com.vanderlei.cfp.job.email.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Profile("oauth-security")
@PropertySource("classpath:env/mail.properties")
public class MailConfig {

  @Autowired private Environment env;

  @Bean
  public EmailService emailService() {
    return new SmtpEmailService();
  }

  @Bean
  public JavaMailSender mailSender() {
    Properties props = new Properties();
    props.setProperty("mail.mime.charset", "utf-8");
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.connectiontimeout", 10000);

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setJavaMailProperties(props);
    mailSender.setDefaultEncoding("utf-8");
    mailSender.setHost(env.getProperty("mail.smtp.host"));
    mailSender.setPort(env.getProperty("mail.smtp.port", Integer.class));
    mailSender.setUsername(env.getProperty("mail.smtp.username"));
    mailSender.setPassword(env.getProperty("mail.smtp.password"));

    return mailSender;
  }
}
