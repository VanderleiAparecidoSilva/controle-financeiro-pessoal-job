package com.vanderlei.cfp.job.job;

import com.vanderlei.cfp.job.email.EmailService;
import com.vanderlei.cfp.job.email.templates.TemplateLancamentoVencido;
import com.vanderlei.cfp.job.entities.Lancamento;
import com.vanderlei.cfp.job.entities.Usuario;
import com.vanderlei.cfp.job.entities.enums.Status;
import com.vanderlei.cfp.job.entities.enums.Tipo;
import com.vanderlei.cfp.job.gateways.LancamentoGateway;
import com.vanderlei.cfp.job.gateways.UsuarioGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VencimentoJob implements Job {

  @Autowired private LancamentoGateway lancamentoGateway;

  @Autowired private UsuarioGateway usuarioGateway;

  @Autowired private EmailService emailService;

  @Scheduled(cron = "0 0 6 * * *", zone = TIME_ZONE)
  public void buscarLancamentosVencidosManha() {
    execute();
  }

    @Scheduled(cron = "0 0 14 * * *", zone = TIME_ZONE)
    public void buscarLancamentosVencidosTarde() {
        execute();
    }

  @Scheduled(cron = "0 0 22 * * *", zone = TIME_ZONE)
  public void buscarLancamentosVencidosNoite() {
    execute();
  }

  private void execute() {
    log.info(
        LocalDateTime.now() + " - Início da execução do job: {}",
        VencimentoJob.class.getSimpleName());

    Collection<Lancamento> lancamentos =
        lancamentoGateway.buscarLancamentosVencidos(Status.ABERTO, LocalDate.now().plusDays(1));

    Map<Usuario, List<Lancamento>> receitasPorUsuario =
        lancamentos.stream()
            .filter(lancamento -> lancamento.getTipo().equals(Tipo.RECEITA))
            .filter(lancamento -> lancamento.getUsuario().getAtivo())
            .filter(lancamento -> lancamento.getUsuario().getPermiteEmailLembrete())
            .collect(Collectors.groupingBy(Lancamento::getUsuario));

    receitasPorUsuario.forEach(
        (u, l) -> {
          final Usuario usuario = usuarioGateway.buscarPorEmail(u.getEmail(), true);
          TemplateLancamentoVencido templateLancamentoVencido =
              new TemplateLancamentoVencido(usuario, l);
          emailService.enviarEmailLancamentoVencidoHtml(templateLancamentoVencido);
        });

    Map<Usuario, List<Lancamento>> despesasPorUsuario =
        lancamentos.stream()
            .filter(lancamento -> lancamento.getTipo().equals(Tipo.DESPESA))
            .filter(lancamento -> lancamento.getUsuario().getAtivo())
            .filter(lancamento -> lancamento.getUsuario().getPermiteEmailLembrete())
            .collect(Collectors.groupingBy(Lancamento::getUsuario));

    despesasPorUsuario.forEach(
        (u, l) -> {
          final Usuario usuario = usuarioGateway.buscarPorEmail(u.getEmail(), true);
          TemplateLancamentoVencido templateLancamentoVencido =
              new TemplateLancamentoVencido(usuario, l);
          emailService.enviarEmailLancamentoVencidoHtml(templateLancamentoVencido);
        });

    log.info(
        LocalDateTime.now() + " - Término da execução do job: {}",
        VencimentoJob.class.getSimpleName());
  }
}
