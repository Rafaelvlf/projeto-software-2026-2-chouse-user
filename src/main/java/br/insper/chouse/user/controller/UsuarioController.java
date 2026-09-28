package br.insper.chouse.user.controller;

import br.insper.chouse.user.model.Usuario;
import br.insper.chouse.user.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criar(@Valid @RequestBody CriarUsuarioRequest request) {
        return service.criar(request.auth0Id(), request.nome(), request.email());
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/auth0/{auth0Id}")
    public Usuario buscarPorAuth0Id(@PathVariable String auth0Id) {
        return service.buscarPorAuth0Id(auth0Id);
    }

    @PatchMapping("/{id}/perfil")
    public Usuario atualizarPerfil(@PathVariable Long id, @Valid @RequestBody AtualizarPerfilRequest request) {
        return service.atualizarPerfil(id, request.nome());
    }

    @PostMapping("/{id}/pontos")
    public Usuario adicionarPontos(@PathVariable Long id, @Valid @RequestBody AdicionarPontosRequest request) {
        return service.adicionarPontos(id, request.pontos());
    }

    public record CriarUsuarioRequest(
            @NotBlank String auth0Id,
            @NotBlank String nome,
            @NotBlank @Email String email) {
    }

    public record AtualizarPerfilRequest(@NotBlank String nome) {
    }

    public record AdicionarPontosRequest(@Positive int pontos) {
    }
}
