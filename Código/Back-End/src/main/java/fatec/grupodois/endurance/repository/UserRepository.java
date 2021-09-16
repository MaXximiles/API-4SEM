package fatec.grupodois.endurance.repository;

import fatec.grupodois.endurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
<<<<<<< HEAD:Código/Back-End/src/main/java/fatec/grupodois/endurance/repository/UsuarioRepository.java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u from Usuario u WHERE u.usuarioEmail = ?1")
    Optional<Usuario> findByEmail(String usuarioEmail);
=======
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u WHERE u.userEmail = ?1")
    Optional<User> findByEmail(String userEmail);
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f:Código/Back-End/src/main/java/fatec/grupodois/endurance/repository/UserRepository.java
}
