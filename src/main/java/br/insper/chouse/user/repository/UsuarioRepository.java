package br.insper.chouse.user.repository;

import br.insper.chouse.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByAuth0Id(String auth0Id);

    boolean existsByAuth0IdOrEmail(String auth0Id, String email);
}
