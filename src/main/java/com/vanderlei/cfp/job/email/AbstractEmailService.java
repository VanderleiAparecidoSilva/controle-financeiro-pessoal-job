package com.vanderlei.cfp.job.email;

import com.vanderlei.cfp.job.email.templates.TemplateLancamentoVencido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

@Component
public abstract class AbstractEmailService implements EmailService {

  static final String username = "controlefinanceiropessoalapi@outlook.com";
  static final String password = "Cfpapi83";

  @Autowired private TemplateEngine thymeleaf;

  @Value("${mail.default.sender}")
  private String sender;

  @Value("${mail.default.subject}")
  private String subject;

  @Override
  public void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj) {
    try {
      MimeMessage mimeMessage = prepararMimeMessageFromLancamento(obj);
      enviarEmail(mimeMessage);
    } catch (MessagingException e) {
      throw new RuntimeException("Erro ao enviar e-mail de lançamentos vencidos!", e);
    }
  }

  protected MimeMessage prepararMimeMessageFromLancamento(final TemplateLancamentoVencido obj)
      throws MessagingException {
    MimeMessage mimeMessage = new MimeMessage(getSession());
    mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setTo(obj.getUsuario().getEmail());
    if (!StringUtils.isEmpty(obj.getUsuario().getEmailCC())) {
      mimeMessageHelper.setCc(obj.getUsuario().getEmailCC());
    }
    mimeMessageHelper.setFrom(sender);
    mimeMessageHelper.setSubject(subject);
    mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
    mimeMessageHelper.setText(htmlFromTemplatePedido(obj), true);

    return mimeMessage;
  }

  protected String htmlFromTemplatePedido(TemplateLancamentoVencido obj) {
    Context context = new Context(new Locale("pt", "BR"));
    context.setVariable("lancamento", obj);
    return thymeleaf.process("email/lancamentoVencido", context);
  }

  protected Session getSession() {
    Properties props = new Properties();
    props.setProperty("mail.mime.charset", "utf-8");
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.connectiontimeout", 10000);

    Session session =
        Session.getInstance(
            props,
            new javax.mail.Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });

    return session;
  }
}
