package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Usuario;
import fatec.grupodois.endurance.error.UsuarioNotFoundException;
import fatec.grupodois.endurance.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> fetchUsuariosList() {
        List<Usuario> usuarios = usuarioService.fetchAllUsuarios();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping("/add") 
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario) {
        usuarioService.addUsuario(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{usuarioId}")
    public ResponseEntity<Usuario> fetchUsuarioById(@PathVariable("usuarioId") Long usuarioId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioService.fetchUsuarioById(usuarioId);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/fetch/{usuarioEmail}")
    public ResponseEntity<Usuario> fetchUsuarioByEmail(@PathVariable("usuarioEmail") String usuarioEmail) throws UsuarioNotFoundException {
        Usuario usuario = usuarioService.fetchUsuarioByEmail(usuarioEmail);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{usuarioId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("usuarioId") Long usuarioID) {
        usuarioService.deleteUsuario(usuarioID);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{usuarioId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("usuarioId") Long usuarioId,
                                               @RequestBody Usuario usuario) {
        usuarioService.updateUsuario(usuarioId, usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
