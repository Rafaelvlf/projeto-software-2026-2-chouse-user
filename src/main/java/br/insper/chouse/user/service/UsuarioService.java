package br.insper.chouse.user.service;

import br.insper.chouse.user.model.Usuario;
import br.insper.chouse.user.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario criar(String auth0Id, String nome, String email) {
        if (repository.existsByAuth0IdOrEmail(auth0Id, email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já cadastrado");
        }
        return repository.save(new Usuario(auth0Id, nome.trim(), email.trim().toLowerCase()));
    }

    @Transactional(readOnly = true)
    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorAuth0Id(String auth0Id) {
        return repository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizarPerfil(Long id, String nome) {
        Usuario usuario = buscar(id);
        usuario.atualizarPerfil(nome);
        return usuario;
    }

    @Transactional
    public Usuario adicionarPontos(Long id, int pontos) {
        Usuario usuario = buscar(id);
        usuario.adicionarPontos(pontos);
        return usuario;
    }
}
