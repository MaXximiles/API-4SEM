package fatec.grupodois.endurance.repository;

import fatec.grupodois.endurance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u from Usuario u WHERE u.usuarioEmail = ?1")
    Optional<Usuario> findByEmail(String usuarioEmail);
}
