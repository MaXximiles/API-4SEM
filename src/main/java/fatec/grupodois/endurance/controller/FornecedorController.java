package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/fornecedores")

public class FornecedorController extends ExceptionHandling {

    private final FornecedorService fornecedorService;

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

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Fornecedor> fetchFornecedorById(@PathVariable("id") Long id) throws FornecedorNotFoundException {

        Fornecedor fornecedor = fornecedorService.fetchFornecedorById(id);

        return new ResponseEntity<>(fornecedor, OK);
    }
}
