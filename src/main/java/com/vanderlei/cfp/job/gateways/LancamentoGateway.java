package com.vanderlei.cfp.job.gateways;

import com.vanderlei.cfp.job.entities.Lancamento;
import com.vanderlei.cfp.job.entities.enums.Status;
import com.vanderlei.cfp.job.gateways.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class LancamentoGateway {

  @Autowired private LancamentoRepository repository;

  public Collection<Lancamento> buscarLancamentosVencidos(
      final Status status, final LocalDate date) {
    return repository.findByStatusAndVencimentoBeforeOrderByUsuarioNome(status, date);
  }
}
