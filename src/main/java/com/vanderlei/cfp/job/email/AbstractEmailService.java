package com.vanderlei.cfp.job.email;

import com.vanderlei.cfp.job.email.templates.TemplateLancamentoVencido;
import com.vanderlei.cfp.job.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;

@Component
public abstract class AbstractEmailService implements EmailService {

  @Autowired private JavaMailSender javaMailSender;

  @Autowired private TemplateEngine thymeleaf;

  @Value("${mail.default.sender}")
  private String sender;

  @Value("${mail.default.subject}")
  private String subject;

  @Override
  public void enviarEmailLancamentoVencido(final TemplateLancamentoVencido obj) {
    SimpleMailMessage simpleMailMessage = prepararSimpleMailMessageFromLancamento(obj);
    enviarEmail(simpleMailMessage);
  }

  @Override
  public void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj) {
    try {
      MimeMessage mimeMessage = prepararMimeMessageFromLancamento(obj);
      enviarEmail(mimeMessage);
    } catch (MessagingException e) {
      enviarEmailLancamentoVencido(obj);
    }
  }

  @Override
  public void enviarNovasenhaEmail(final Usuario obj, final String novaSenha) {
    SimpleMailMessage simpleMailMessage = prepararNovaSenhaEmail(obj, novaSenha);
    enviarEmail(simpleMailMessage);
  }

  protected SimpleMailMessage prepararSimpleMailMessageFromLancamento(
      final TemplateLancamentoVencido obj) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(sender);
    simpleMailMessage.setTo(obj.getUsuario().getEmail());
    simpleMailMessage.setCc(obj.getUsuario().getEmailCC());
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
    simpleMailMessage.setText(obj.toString());
    return simpleMailMessage;
  }

  protected MimeMessage prepararMimeMessageFromLancamento(final TemplateLancamentoVencido obj)
      throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setTo(obj.getUsuario().getEmail());
    mimeMessageHelper.setCc(obj.getUsuario().getEmailCC());
    mimeMessageHelper.setFrom(sender);
    mimeMessageHelper.setSubject(subject);
    mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
    mimeMessageHelper.setText(htmlFromTemplatePedido(obj), true);

    return mimeMessage;
  }

  protected SimpleMailMessage prepararNovaSenhaEmail(final Usuario usuario, final String newPass) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(usuario.getEmail());
    simpleMailMessage.setFrom(sender);
    simpleMailMessage.setSubject("Solicitação de nova senha");
    simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
    simpleMailMessage.setText("Nova senha: " + newPass);
    return simpleMailMessage;
  }

  protected String htmlFromTemplatePedido(TemplateLancamentoVencido obj) {
    Context context = new Context(new Locale("pt", "BR"));
    context.setVariable("lancamento", obj);
    return thymeleaf.process("email/lancamentoVencido", context);
  }
}
