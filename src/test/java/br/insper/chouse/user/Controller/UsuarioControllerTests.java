package br.insper.chouse.user.Controller;

import br.insper.chouse.user.controller.UsuarioController;
import br.insper.chouse.user.model.Usuario;
import br.insper.chouse.user.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallBuscar() throws Exception {
        Usuario usuario = new Usuario("auth0|123", "João Silva", "joao@email.com");
        Usuario salvo = usuarioRepository.save(usuario);

        MvcResult result = mockMvc.perform(
                        get("/usuarios/" + salvo.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Usuario resposta = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Usuario.class);

        Assertions.assertEquals(salvo.getId(), resposta.getId());
        Assertions.assertEquals("auth0|123", resposta.getAuth0Id());
        Assertions.assertEquals("João Silva", resposta.getNome());
        Assertions.assertEquals("joao@email.com", resposta.getEmail());
    }

    @Test
    public void test_shouldCreateUsuario() throws Exception {
        UsuarioController.CriarUsuarioRequest request =
                new UsuarioController.CriarUsuarioRequest("auth0|999", "Maria", "maria@email.com");

        MvcResult result = mockMvc.perform(
                        post("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        Usuario resposta = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Usuario.class);

        Assertions.assertNotNull(resposta.getId());
        Assertions.assertEquals("auth0|999", resposta.getAuth0Id());
        Assertions.assertEquals("Maria", resposta.getNome());
        Assertions.assertEquals("maria@email.com", resposta.getEmail());
    }

    @Test
    public void test_shouldReturnUsuarioWhenCallBuscarPorAuth0Id() throws Exception {
        Usuario usuario = new Usuario("auth0|456", "Carlos", "carlos@email.com");
        Usuario salvo = usuarioRepository.save(usuario);

        MvcResult result = mockMvc.perform(
                        get("/usuarios/auth0/" + salvo.getAuth0Id()))
                .andExpect(status().isOk())
                .andReturn();

        Usuario resposta = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Usuario.class);

        Assertions.assertEquals(salvo.getId(), resposta.getId());
        Assertions.assertEquals("auth0|456", resposta.getAuth0Id());
        Assertions.assertEquals("Carlos", resposta.getNome());
    }

    @Test
    public void test_shouldUpdatePerfil() throws Exception {
        Usuario usuario = new Usuario("auth0|789", "Ana", "ana@email.com");
        Usuario salvo = usuarioRepository.save(usuario);

        UsuarioController.AtualizarPerfilRequest request =
                new UsuarioController.AtualizarPerfilRequest("Ana Silva");

        MvcResult result = mockMvc.perform(
                        patch("/usuarios/" + salvo.getId() + "/perfil")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Usuario resposta = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Usuario.class);

        Assertions.assertEquals(salvo.getId(), resposta.getId());
        Assertions.assertEquals("Ana Silva", resposta.getNome());
    }

    @Test
    public void test_shouldAdicionarPontos() throws Exception {
        Usuario usuario = new Usuario("auth0|000", "Pedro", "pedro@email.com");
        Usuario salvo = usuarioRepository.save(usuario);

        UsuarioController.AdicionarPontosRequest request =
                new UsuarioController.AdicionarPontosRequest(25);

        MvcResult result = mockMvc.perform(
                        post("/usuarios/" + salvo.getId() + "/pontos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Usuario resposta = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Usuario.class);

        Assertions.assertEquals(salvo.getId(), resposta.getId());
        Assertions.assertEquals(25, resposta.getPontuacaoTotal());
    }
}