package br.insper.chouse.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTests {

    @Test
    void deveAtualizarPerfilESomarPontos() {
        Usuario usuario = new Usuario("auth0|123", "Nome antigo", "usuario@chouse.com");

        usuario.atualizarPerfil("Novo nome");
        usuario.adicionarPontos(10);

        assertEquals("Novo nome", usuario.getNome());
        assertEquals(10, usuario.getPontuacaoTotal());
    }

    @Test
    void naoDeveAceitarPontuacaoNegativa() {
        Usuario usuario = new Usuario("auth0|123", "Nome", "usuario@chouse.com");

        assertThrows(IllegalArgumentException.class, () -> usuario.adicionarPontos(-1));
    }
}
