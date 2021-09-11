package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Usuario;
import fatec.grupodois.endurance.error.UsuarioNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsuarioService {


    void addUsuario(Usuario usuario);

    Usuario fetchUsuarioById(Long usuarioId) throws UsuarioNotFoundException;

    Usuario fetchUsuarioByEmail(String usuarioEmail) throws UsuarioNotFoundException;

    List<Usuario> fetchAllUsuarios();

    void deleteUsuario(Long usuarioID);

    void updateUsuario(Long usuarioId, Usuario usuario);
}
