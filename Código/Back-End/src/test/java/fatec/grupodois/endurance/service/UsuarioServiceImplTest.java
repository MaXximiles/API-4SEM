package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.TipoUsuario;
import fatec.grupodois.endurance.entity.Usuario;
import fatec.grupodois.endurance.error.UsuarioNotFoundException;
import fatec.grupodois.endurance.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class UsuarioServiceImplTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        Usuario usuario =
                Usuario.builder()
                        .usuarioNome("Teste S")
                        .usuarioRg("111111")
                        .usuarioEmail("teste@gmail.com")
                        .usuarioTipo(TipoUsuario.ADMIN)
                        .usuarioCpf("973.017.940-96")
                        .usuarioSenha("password")
                        .usuarioId(1L)
                        .build();

        Mockito.when(usuarioRepository.findByEmail("teste@gmail.com"))
                .thenReturn(java.util.Optional.ofNullable(usuario));

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(usuario));

    }

    @Test
    @DisplayName("Get usuario on valid id")
    void whenValidId_thenUsuarioShouldFound() throws UsuarioNotFoundException {
        Usuario found = usuarioService.fetchUsuarioById(1L);

        assertEquals("Teste S", found.getUsuarioNome());
    }

    @Test
    @DisplayName("Get usuario on valid email")
    void whenValidEmail_thenUsuarioShouldFound() throws UsuarioNotFoundException {
        Usuario found = usuarioService.fetchUsuarioByEmail("teste@gmail.com");

        assertEquals("teste@gmail.com", found.getUsuarioEmail());
    }

    @Test
    @DisplayName("Update usuario on valid id")
    void whenValidId_thenUsuarioShouldUpdate() throws UsuarioNotFoundException {
        Usuario found = usuarioService.fetchUsuarioById(1L);

        found.setUsuarioEmail("teste1@gmail.com");

        usuarioService.addUsuario(found);

        found = usuarioService.fetchUsuarioById(1L);

        assertEquals("teste1@gmail.com", found.getUsuarioEmail());
    }
}