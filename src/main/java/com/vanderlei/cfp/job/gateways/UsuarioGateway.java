package com.vanderlei.cfp.job.gateways;

import com.vanderlei.cfp.job.entities.Usuario;
import com.vanderlei.cfp.job.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.job.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioGateway {

  private final String msgObjectNotFoundUsuarioCodigo = "Usuario não encontrado! Codigo: ";

  private final String msgObjectNotFoundUsuarioEmail = "Usuario não encontrado! Email: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private UsuarioRepository repository;

  public Usuario buscarPorCodigo(final String id) {
    Optional<Usuario> obj = repository.findById(id);
    return obj.orElseThrow(
        () ->
            new ObjectNotFoundException(
                msgObjectNotFoundUsuarioCodigo + id + msgTipo + Usuario.class.getName()));
  }

  public Usuario buscarPorEmail(final String email, final boolean active) {
    Optional<Usuario> obj = repository.findByEmail(email);
    if (active) {
      obj = obj.filter(usuario -> usuario.getAtivo());
    }
    return obj.orElseThrow(
        () ->
            new ObjectNotFoundException(
                msgObjectNotFoundUsuarioEmail + email + msgTipo + Usuario.class.getName()));
  }

  public Optional<Usuario> buscarPorNomeEmail(final String nome, final String email) {
    return repository.findByNomeAndEmail(nome, email);
  }
}
