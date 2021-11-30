package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.entity.HttpResponse;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/fornecedores")

public class FornecedorController extends ExceptionHandling {

    private final FornecedorService fornecedorService;
    public static final String FORNECEDOR_DELETADO = "Fornecedor deletado com sucesso";

    @Autowired
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping("/add")
    public ResponseEntity<Fornecedor> addFornecedor(@RequestParam("descricao") String descricao,
                                                    @RequestParam("cnpj") String cnpj,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("observacao") String observacao)
            throws FornecedorNotFoundException, EmailExistsException,
            CnpjExistsException, DescricaoExistsException {

        Fornecedor novoFornecedor = fornecedorService.addFornecedor(descricao, cnpj, email, observacao);

        return new ResponseEntity<>(novoFornecedor, CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFornecedor(@PathVariable("id")Long fornecedorId) {

        fornecedorService.deleteFornecedorById(fornecedorId);

        return new ResponseEntity<>(new HttpResponse(
                                    OK.value(), OK, OK.getReasonPhrase(), FORNECEDOR_DELETADO)
                                    ,OK
                                    );
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Fornecedor> fetchFornecedorById(@PathVariable("id") Long id) throws FornecedorNotFoundException {

        Fornecedor fornecedor = fornecedorService.fetchFornecedorById(id);

        return new ResponseEntity<>(fornecedor, OK);
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<List<Fornecedor>> fetchAllFornecedores() {

        List<Fornecedor> fornecedores = fornecedorService.findAllFornecedores();

        return new ResponseEntity<>(fornecedores, OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('fornecedor:update')")
    public ResponseEntity<Fornecedor> updateFornecedor(@RequestParam("emailAtual") String emailAtual,
                                                       @RequestParam("cnpj") String cnpj,
                                                       @RequestParam("email") String email,
                                                       @RequestParam("observacao") String observacao,
                                                       @RequestParam("descricao") String descricao)
            throws FornecedorNotFoundException, EmailExistsException, DescricaoExistsException {

        Fornecedor fornecedor = fornecedorService.updateFornecedor(emailAtual, cnpj, email, observacao, descricao);

        return new ResponseEntity<>(fornecedor, OK);
    }

    @GetMapping("/fetch-by-email/{email}")
    public ResponseEntity<Fornecedor> fetchByEmail(@PathVariable("email") String email) throws FornecedorNotFoundException, EmailExistsException {

        Fornecedor fornecedor = fornecedorService.fetchFornecedorByEmail(email);

        return new ResponseEntity<>(fornecedor, OK);
    }

    @GetMapping("/fetch-by-cnpj/{cnpj}")
    public ResponseEntity<Fornecedor> fetchByCnpj(@PathVariable("cnpj") String cnpj) throws FornecedorNotFoundException, CnpjExistsException {

        Fornecedor fornecedor = fornecedorService.fetchFornecedorByCnpj(cnpj);

        return new ResponseEntity<>(fornecedor, OK);
    }

    @GetMapping("/fetch-by-descricao/{descricao}")
    public ResponseEntity<Fornecedor> fetchByDescricao(@PathVariable("descricao") String descricao) throws FornecedorNotFoundException, DescricaoExistsException {

        Fornecedor fornecedor = fornecedorService.fetchFornecedorByDescricao(descricao);

        return new ResponseEntity<>(fornecedor, OK);
    }

    @GetMapping("/fetch-fornecimentos/{cnpj}")
    public ResponseEntity<List<Evento>> fetchFornecimentos(@PathVariable("cnpj") String cnpj) {

        List<Evento> fornecimentos = fornecedorService.fetchFornecedorMap(cnpj);

        return new ResponseEntity<>(fornecimentos, OK);
    }

}
