package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.exception.CnpjExistsException;
import fatec.grupodois.endurance.exception.DescricaoExistsException;
import fatec.grupodois.endurance.exception.EmailExistsException;
import fatec.grupodois.endurance.exception.FornecedorNotFoundException;

import java.util.List;

public interface FornecedorService {

    Fornecedor addFornecedor (String descricao, String cnpj, String email, String observacao) throws FornecedorNotFoundException, CnpjExistsException, EmailExistsException, DescricaoExistsException;

    void deleteFornecedorById(Long id);

    List<Fornecedor> findAllFornecedores();

    Fornecedor fetchFornecedorById(Long id) throws FornecedorNotFoundException;

    Fornecedor fetchFornecedorByCnpj(String cnpj) throws FornecedorNotFoundException, CnpjExistsException;

    Fornecedor fetchFornecedorByEmail(String email) throws FornecedorNotFoundException, EmailExistsException;

    Fornecedor fetchFornecedorByDescricao(String descricao) throws FornecedorNotFoundException, DescricaoExistsException;

    Fornecedor updateFornecedor(Long id, Fornecedor fornecedor) throws FornecedorNotFoundException, DescricaoExistsException, EmailExistsException;

    List<Evento> fetchFornecedorMap(Long id);
}
