package com.vanderlei.cfp.job.email;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Slf4j
public class SmtpEmailService extends AbstractEmailService {

  @Override
  public void enviarEmail(final MimeMessage msg) {
    log.info(LocalDateTime.now() + " - Enviando e-mail...");
    try {
      Transport.send(msg);
    } catch (MessagingException e) {
      throw new RuntimeException("Erro ao enviar e-mail de lançamentos vencidos!", e);
    }
    log.info(LocalDateTime.now() + " - Email enviado");
  }
}
