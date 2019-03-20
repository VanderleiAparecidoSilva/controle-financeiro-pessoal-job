package com.vanderlei.cfp.job.email;

import com.vanderlei.cfp.job.email.templates.TemplateLancamentoVencido;

import javax.mail.internet.MimeMessage;

public interface EmailService {

  void enviarEmail(final MimeMessage msg);

  void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj);
}
