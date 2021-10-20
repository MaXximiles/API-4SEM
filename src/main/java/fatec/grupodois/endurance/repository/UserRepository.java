package fatec.grupodois.endurance.repository;

import fatec.grupodois.endurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u WHERE u.email = ?1")
    Optional<User> findByEmail(String userEmail);

    @Query("SELECT u from User u WHERE u.cpf = ?1")
    Optional<User> findByCpf(String userCpf);

    @Query("SELECT u from User u WHERE u.role = 'ROLE_ADMIN'")
    List<User> findAllAdmins();

    User findUserByEmail(String email);

    User findUserByCpf(String cpf);

    User findUserById(Long id);
}
