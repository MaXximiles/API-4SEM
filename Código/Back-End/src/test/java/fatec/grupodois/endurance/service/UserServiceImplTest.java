package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.TipoUsuario;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.EmailNotFoundException;
import fatec.grupodois.endurance.exception.UserNotFoundException;
import fatec.grupodois.endurance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user =
                User.builder()
                        .userFirstName("Teste")
                        .userLastName("S")
                        .userRg("111111")
                        .userEmail("teste@gmail.com")
                        .userRole(TipoUsuario.ADMIN)
                        .userCpf("973.017.940-96")
                        .userPassword("password")
                        .userId(1L)
                        .build();

        Mockito.when(userRepository.findByEmail("teste@gmail.com"))
                .thenReturn(java.util.Optional.ofNullable(user));

        Mockito.when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(user));

    }

    @Test
    @DisplayName("Get user on valid id")
    void whenValidId_thenUserShouldBeFound() throws UserNotFoundException {
        User found = userService.fetchUserById(1L);

        assertEquals("Teste", found.getUserFirstName());
    }

    @Test
    @DisplayName("Get user on valid email")
    void whenValidEmail_thenUserShouldBeFound() throws EmailNotFoundException {
        User found = userService.fetchUserByEmail("teste@gmail.com");

        assertEquals("teste@gmail.com", found.getUserEmail());
    }

    @Test
    @DisplayName("Update user on valid id")
    void whenValidId_thenUserShouldUpdate() throws UserNotFoundException {
        User found = userService.fetchUserById(1L);

        found.setUserEmail("teste1@gmail.com");

        userService.addUser(found);

        found = userService.fetchUserById(1L);

        assertEquals("teste1@gmail.com", found.getUserEmail());
    }

    @Test
    @DisplayName("Dont Get User on Invalid Email")
    public void whenInvalidEmail_thenShouldThrowError() {

        Throwable exc = assertThrows(UserNotFoundException.class, () ->
                userService.fetchUserById(2L));

        assertEquals("Usuário com id 2 não encontrado.", exc.getMessage());
    }

    @Test
    @DisplayName("Dont Update User on Invalid First Name Input")
    public void whenInvalidName_thenShouldNotUpdate() throws UserNotFoundException {

        User user = User.builder()
                .userFirstName("")
                .userLastName("A")
                .userRg("1111112")
                .userEmail("teste3@gmail.com")
                .userRole(TipoUsuario.ADMIN)
                .userCpf("973.017.940-96")
                .userPassword("password")
                .userId(2L)
                .build();
        userService.updateUser(1L, user);

        User found = userService.fetchUserById(1L);

        assertEquals("Teste", found.getUserFirstName());
        assertEquals("A", found.getUserLastName());
    }
}