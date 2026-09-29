package br.insper.chouse.user.Service;

import br.insper.chouse.user.model.Usuario;
import br.insper.chouse.user.repository.UsuarioRepository;
import br.insper.chouse.user.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    public void test_shouldReturnUsuarioWhenCallBuscar() {
        Usuario usuario = new Usuario("Id1", "User", "User@email.com");
        usuario.setId(1L);
        usuario.setPontuacaoTotal(10);
        usuario.setCriadoEm(LocalDateTime.of(2026, 9, 28, 15, 30));

        // mocks
        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        // chamada
        Usuario op = usuarioService.buscar(1L);

        // asserts
        Assertions.assertEquals(1L, op.getId());
        Assertions.assertEquals("Id1", op.getAuth0Id());
        Assertions.assertEquals("User", op.getNome());
        Assertions.assertEquals("User@email.com", op.getEmail());
        Assertions.assertEquals(10, op.getPontuacaoTotal());
        Assertions.assertEquals(LocalDateTime.of(2026, 9, 28, 15, 30), op.getCriadoEm());
    }
    @Test
    public void test_shouldThrowExceptionWhenBuscarIdInexistente() {
        Mockito.when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            usuarioService.buscar(99L);
        });

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallBuscarPorAuth0Id() {
        Usuario usuario = new Usuario("Id1", "User", "User@email.com");
        usuario.setId(1L);
        usuario.setPontuacaoTotal(10);
        usuario.setCriadoEm(LocalDateTime.of(2026, 9, 28, 15, 30));

        // mocks
        Mockito.when(usuarioRepository.findByAuth0Id("Id1"))
                .thenReturn(Optional.of(usuario));

        // chamada
        Usuario op = usuarioService.buscarPorAuth0Id("Id1");

        // asserts
        Assertions.assertEquals(1L, op.getId());
        Assertions.assertEquals("Id1", op.getAuth0Id());
        Assertions.assertEquals("User", op.getNome());
        Assertions.assertEquals("User@email.com", op.getEmail());
        Assertions.assertEquals(10, op.getPontuacaoTotal());
        Assertions.assertEquals(LocalDateTime.of(2026, 9, 28, 15, 30), op.getCriadoEm());
    }
    @Test
    public void test_shouldThrowExceptionWhenBuscarAuth0IdInexistente() {
        // Simula o banco não encontrando o Auth0Id
        Mockito.when(usuarioRepository.findByAuth0Id("IdInexistente"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            usuarioService.buscarPorAuth0Id("IdInexistente");
        });

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallCriar() {
        String auth0IdInput = "Id1";
        String nomeInput = " User ";
        String emailInput = " USER@email.com ";

        Usuario usuarioSalvo = new Usuario(auth0IdInput, "User", "user@email.com");
        usuarioSalvo.setId(1L);
        usuarioSalvo.setPontuacaoTotal(0);
        usuarioSalvo.setCriadoEm(LocalDateTime.of(2026, 9, 28, 15, 30));

        // mocks
        Mockito.when(usuarioRepository.existsByAuth0IdOrEmail(auth0IdInput, emailInput))
                .thenReturn(false); // Simula que o utilizador não existe


        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        // chamada
        Usuario op = usuarioService.criar(auth0IdInput, nomeInput, emailInput);

        // asserts
        Assertions.assertEquals(1L, op.getId());
        Assertions.assertEquals("Id1", op.getAuth0Id());
        Assertions.assertEquals("User", op.getNome()); // Garante que o trim() funcionou
        Assertions.assertEquals("user@email.com", op.getEmail()); // Garante que o toLowerCase() funcionou
        Assertions.assertEquals(0, op.getPontuacaoTotal());
        Assertions.assertEquals(LocalDateTime.of(2026, 9, 28, 15, 30), op.getCriadoEm());

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any(Usuario.class));
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallAtualizarPerfil() {
        Usuario usuario = new Usuario("Id1", "User Antigo", "User@email.com");
        usuario.setId(1L);
        usuario.setPontuacaoTotal(10);
        usuario.setCriadoEm(LocalDateTime.of(2026, 9, 28, 15, 30));

        // mocks
        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        Usuario op = usuarioService.atualizarPerfil(1L, " User Novo ");

        // asserts
        Assertions.assertEquals(1L, op.getId());
        Assertions.assertEquals("Id1", op.getAuth0Id());
        Assertions.assertEquals("User Novo", op.getNome());
        Assertions.assertEquals("User@email.com", op.getEmail());
        Assertions.assertEquals(10, op.getPontuacaoTotal());
        Assertions.assertEquals(LocalDateTime.of(2026, 9, 28, 15, 30), op.getCriadoEm());
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallAdicionarPontos() {
        Usuario usuario = new Usuario("Id1", "User", "User@email.com");
        usuario.setId(1L);
        usuario.setPontuacaoTotal(10); // Pontuação inicial
        usuario.setCriadoEm(LocalDateTime.of(2026, 9, 28, 15, 30));

        // mocks
        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        Usuario op = usuarioService.adicionarPontos(1L, 15);

        // asserts
        Assertions.assertEquals(1L, op.getId());
        Assertions.assertEquals("Id1", op.getAuth0Id());
        Assertions.assertEquals("User", op.getNome());
        Assertions.assertEquals("User@email.com", op.getEmail());
        Assertions.assertEquals(25, op.getPontuacaoTotal()); // Verifica se somou corretamente (10 + 15)
        Assertions.assertEquals(LocalDateTime.of(2026, 9, 28, 15, 30), op.getCriadoEm());
    }

}
