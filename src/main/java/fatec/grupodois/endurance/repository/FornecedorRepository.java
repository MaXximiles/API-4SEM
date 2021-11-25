package fatec.grupodois.endurance.repository;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findFornecedorByCnpj(String cnpj);
    Optional<Fornecedor> findFornecedorByEmail(String email);
    Optional<Fornecedor> findFornecedorByDescricao(String descricao);

    @Query(
            value = "SELECT * FROM evento_fornecedor_map s where s.efm_frn_id =?1",
            nativeQuery = true
    )
    List<Evento> getFornecedorMap(Long fornecedorId);

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);

    boolean existsByDescricao(String descricao);
}