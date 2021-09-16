package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Usuario;
import fatec.grupodois.endurance.error.UsuarioNotFoundException;
import fatec.grupodois.endurance.repository.UsuarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void addUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario fetchUsuarioById(Long usuarioId) throws UsuarioNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if(usuario.isEmpty()) {
            throw new UsuarioNotFoundException("Usuario " +
                    "com id " +
                    usuarioId +
                    " não encontrado.");
        }

        return usuarioRepository.findById(usuarioId).get();
    }

    @Override
    public Usuario fetchUsuarioByEmail(String usuarioEmail) throws UsuarioNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioEmail);

        if(usuario.isEmpty()) {
            throw new UsuarioNotFoundException("Usuário com email " +
                     usuarioEmail +
                    " não encontrado.");
        }

        return usuarioRepository.findByEmail(usuarioEmail).get();
    }

    @Override
    public List<Usuario> fetchAllUsuarios() {

        return usuarioRepository.findAll();
    }

    @Override
    public void deleteUsuario(Long usuarioID) {

        usuarioRepository.deleteById(usuarioID);
    }

    @Override
    public void updateUsuario(Long usuarioId, Usuario usuario) {

        Usuario usuarioDb = usuarioRepository.findById(usuarioId).get();

        if(StringUtils.isNotEmpty(usuario.getUsuarioFirstName()) &&
                StringUtils.isNotBlank(usuario.getUsuarioFirstName()) &&
                !"".equalsIgnoreCase(usuario.getUsuarioFirstName())) {

            usuarioDb.setUsuarioFirstName(usuario.getUsuarioFirstName());
        }

        if(StringUtils.isNotEmpty(usuario.getUsuarioLastName()) &&
                StringUtils.isNotBlank(usuario.getUsuarioLastName()) &&
                !"".equalsIgnoreCase(usuario.getUsuarioLastName())) {
            usuarioDb.setUsuarioLastName(usuario.getUsuarioLastName());
        }

        if(StringUtils.isNotEmpty(usuario.getUsuarioRg()) &&
                StringUtils.isNotBlank(usuario.getUsuarioRg()) &&
                !"".equalsIgnoreCase(usuario.getUsuarioRg())) {
            usuarioDb.setUsuarioRg(usuario.getUsuarioRg());
        }

        if(StringUtils.isNotEmpty(usuario.getUsuarioEmail()) &&
                StringUtils.isNotBlank(usuario.getUsuarioEmail()) &&
                !"".equalsIgnoreCase(usuario.getUsuarioEmail())) {
            usuarioDb.setUsuarioEmail(usuario.getUsuarioEmail());
        }


        usuarioRepository.save(usuarioDb);
    }


}
