package com.vanderlei.cfp.job.email;

import com.vanderlei.cfp.job.email.templates.TemplateLancamentoVencido;
import com.vanderlei.cfp.job.entities.Usuario;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService {

  void enviarEmail(final SimpleMailMessage msg);

  void enviarEmail(final MimeMessage msg);

  void enviarEmailLancamentoVencido(final TemplateLancamentoVencido obj);

  void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj);

  void enviarNovasenhaEmail(final Usuario obj, final String novaSenha);
}
