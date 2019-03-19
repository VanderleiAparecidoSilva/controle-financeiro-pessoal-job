package com.vanderlei.cfp.job.gateways.repository;

import com.vanderlei.cfp.job.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

  Optional<Usuario> findByNomeAndEmail(final String nome, final String email);

  Optional<Usuario> findByEmail(final String email);
}
