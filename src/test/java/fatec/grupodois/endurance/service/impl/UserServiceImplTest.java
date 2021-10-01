package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.repository.UserRepository;
import fatec.grupodois.endurance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() throws UserNotFoundException, EmailNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException {


        this.user = User
                .builder()
                .firstName("Teste")
                .lastName("S")
                .email("teste@gmail.com")
                .cpf("973.017.940-96")
                .joinDate(new Date())
                .password("123")
                .isActive(true)
                .isNotLocked(false)
                .role(ROLE_GUEST.name())
                .authorities(ROLE_GUEST.getAuthorities())
                .profileImageUrl(null)
                .id(1L)
                .build();

        userRepository.save(user);

        Mockito.when(userRepository.findUserByEmail("teste@gmail.com"))
                .thenReturn(user);

        Mockito.when(userRepository.findUserById(1L))
                .thenReturn(user);

        Mockito.when(userRepository.findUserByCpf("973.017.940-96"))
                .thenReturn(user);

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);
    }

    @Test
    @DisplayName("Register User")
    void registerCustomer_Success() {

        User savedUser = userRepository.save(user);
        assertTrue(savedUser.isActive());
    }

    @Test
    @DisplayName("Get user on valid id")
    void whenValidId_thenUserShouldBeFound() throws UserNotFoundException {
        User found = userService.findUserById(1L);

        assertEquals("Teste", found.getFirstName());
    }

    @Test
    @DisplayName("Get user on valid email")
    void whenValidEmail_thenUserShouldBeFound() throws EmailNotFoundException {
        User found = userService.findUserByEmail("teste@gmail.com");

        assertEquals("teste@gmail.com", found.getEmail());
    }

    @Test
    @DisplayName("Get user on valid cpf")
    void whenValidCpf_thenUserShouldBeFound() throws EmailNotFoundException {
        User found = userService.findUserByCpf("973.017.940-96");

        assertEquals("teste@gmail.com", found.getEmail());
    }

    @Test
    @DisplayName("Dont Get User on Invalid Email")
    public void whenInvalidEmail_thenShouldThrowError() {

        Throwable exc = assertThrows(UserNotFoundException.class, () ->
                userService.fetchUserById(2L));

        assertEquals("Usuário com id 2 não encontrado.", exc.getMessage());
    }

}