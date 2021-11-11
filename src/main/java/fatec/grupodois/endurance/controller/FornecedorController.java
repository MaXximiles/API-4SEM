package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.entity.HttpResponse;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
