package fatec.grupodois.endurance.repository;

import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;

import static fatec.grupodois.endurance.enumeration.Role.ROLE_GUEST;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() throws UserNotFoundException, EmailNotFoundException, EmailExistException, CpfExistException, CpfNotFoundException {

        User user = User
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john_the_doe@gmail.com")
                .cpf("973.017.940-96")
                .joinDate(new Date())
                .password("123")
                .isActive(true)
                .isNotLocked(false)
                .role(ROLE_GUEST.name())
                .authorities(ROLE_GUEST.getAuthorities())
                .profileImageUrl(null)
                .build();

        entityManager.persist(user);
    }

    @Test
    @DisplayName("Id Find User")
    public void whenFindById_thenReturnUser() {
        User user = repo.findById(1L).get();
        assertEquals(user.getFirstName(), "John");
    }

    @Test
    @DisplayName("Email Find User")
    public void whenFindByEmail_thenReturnUser() {
        User user = repo.findByEmail("john_the_doe@gmail.com").get();
        assertEquals(user.getFirstName(), "John");
    }

    @Test
    @DisplayName("CPF Find User")
    public void whenFindByCpf_thenReturnUser() {
        User user = repo.findByCpf("973.017.940-96").get();
        assertEquals(user.getFirstName(), "John");
    }

}