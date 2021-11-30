package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.exception.CnpjExistsException;
import fatec.grupodois.endurance.exception.DescricaoExistsException;
import fatec.grupodois.endurance.exception.EmailExistsException;
import fatec.grupodois.endurance.exception.FornecedorNotFoundException;
import fatec.grupodois.endurance.repository.FornecedorRepository;
import fatec.grupodois.endurance.service.EmailService;
import fatec.grupodois.endurance.service.FornecedorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static fatec.grupodois.endurance.constant.FornecedorImplConstant.*;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final EmailService emailService;

    @Autowired
    public FornecedorServiceImpl(FornecedorRepository fornecedorRepository, EmailService emailService) {
        this.fornecedorRepository = fornecedorRepository;
        this.emailService = emailService;
    }

    @Override
    public Fornecedor addFornecedor(String descricao, String cnpj, String email, String observacao) throws FornecedorNotFoundException, CnpjExistsException, EmailExistsException, DescricaoExistsException {

        checkCnpj(cnpj, true);
        checkEmail(email, true);
        checkDescricao(descricao, true);

        Fornecedor fornecedor = Fornecedor
                .builder()
                .descricao(descricao)
                .cnpj(cnpj)
                .email(email)
                .observacao(observacao).build();

        fornecedor.setDescricao(descricao);
        fornecedor.setCnpj(cnpj);
        fornecedor.setEmail(email);
        fornecedor.setObservacao(observacao);

        return fornecedorRepository.save(fornecedor);
    }

    @Override
    public void deleteFornecedorById(Long id) { fornecedorRepository.deleteById(id); }

    @Override
    public List<Fornecedor> findAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    @Override
    public Fornecedor fetchFornecedorById(Long id) throws FornecedorNotFoundException {

        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);

        if(fornecedor.isEmpty()) {
            throw new FornecedorNotFoundException(
                    FORNECEDOR_NOT_FOUND_GENERIC
                            + ID
                            + id
            );
        }

        return fornecedor.get();
    }

    @Override
    public Fornecedor fetchFornecedorByCnpj(String cnpj) throws FornecedorNotFoundException, CnpjExistsException {

        return checkCnpj(cnpj, false);
    }

    @Override
    public Fornecedor fetchFornecedorByEmail(String email) throws FornecedorNotFoundException, EmailExistsException {

        return checkEmail(email, false);
    }

    @Override
    public Fornecedor fetchFornecedorByDescricao(String descricao) throws FornecedorNotFoundException, DescricaoExistsException {

        return checkDescricao(descricao, false);
    }

    @Override
    public Fornecedor updateFornecedor(String emailAtual, String cnpj, String email, String observacao, String descricao)
            throws FornecedorNotFoundException, DescricaoExistsException, EmailExistsException {

        Fornecedor fornecedorDb = fornecedorRepository.findFornecedorByEmail(email).get();

        if(StringUtils.isNotEmpty(StringUtils.trim(descricao)) &&
                !StringUtils.equalsIgnoreCase(descricao, fornecedorDb.getDescricao())) {

            checkDescricao(descricao, true);
            fornecedorDb.setDescricao(descricao);
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(email)) &&
                !StringUtils.equalsIgnoreCase(email, fornecedorDb.getEmail())) {

            checkEmail(email, true);
            fornecedorDb.setEmail(email);
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(observacao)) &&
                !StringUtils.equalsIgnoreCase(observacao, fornecedorDb.getObservacao())) {

            fornecedorDb.setObservacao(observacao);
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(cnpj)) &&
                !StringUtils.equalsIgnoreCase(cnpj, fornecedorDb.getCnpj())) {

            fornecedorDb.setCnpj(cnpj);
        }

        return fornecedorRepository.save(fornecedorDb);
    }

    @Override
    public List<Evento> fetchFornecedorMap(String cnpj) {

        Fornecedor fornecedor = fornecedorRepository.findFornecedorByCnpj(cnpj).get();

        return fornecedorRepository.getFornecedorMap(fornecedor.getId());
    }

    private Fornecedor checkCnpj(String cnpj, boolean inserir) throws FornecedorNotFoundException, CnpjExistsException {

        boolean ans = fornecedorRepository.existsByCnpj(cnpj);

        if(ans && inserir){
            throw new CnpjExistsException(CNPJ_EXISTS);
        }
        else if(ans) {
            return fornecedorRepository.findFornecedorByCnpj(cnpj).get();
        }
        else if (inserir){
            return null;
        }
        else {
            throw new FornecedorNotFoundException(FORNECEDOR_NOT_FOUND_GENERIC + CNPJ + cnpj);
        }
    }

    private Fornecedor checkEmail(String email, boolean inserir) throws FornecedorNotFoundException, EmailExistsException {

        boolean ans = fornecedorRepository.existsByEmail(email);

        if(ans && inserir) {

            throw new EmailExistsException(EMAIL_EXISTS);

        } else if(ans) {

            return fornecedorRepository.findFornecedorByCnpj(email).get();

        } else if (inserir) {

            return null;

        } else {

            throw new FornecedorNotFoundException(FORNECEDOR_NOT_FOUND_GENERIC
                    + EMAIL
                    + email);
        }
    }

    private Fornecedor checkDescricao(String descricao, boolean inserir) throws FornecedorNotFoundException, DescricaoExistsException {

        boolean ans = fornecedorRepository.existsByDescricao(descricao);

        if(ans && inserir) {

            throw new DescricaoExistsException(DESCRICAO_EXISTS);

        } else if(ans) {

            return fornecedorRepository.findFornecedorByDescricao(descricao).get();

        } else if (inserir) {

            return null;

        } else {

            throw new FornecedorNotFoundException(FORNECEDOR_NOT_FOUND_GENERIC
                    + DESCRICAO
                    + descricao);
        }
    }
}
