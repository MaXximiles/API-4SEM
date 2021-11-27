package fatec.grupodois.endurance.entity;


import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(
        name = Fornecedor.TABLE_NAME,
        uniqueConstraints = {@UniqueConstraint(
                name = "UC_FORNECEDORES_FRN_DESCRICAO",
                columnNames = Fornecedor.COLUNA_DESCRICAO
        ), @UniqueConstraint(
                name = "UC_FORNECEDORES_FRN_EMAIL",
                columnNames = Fornecedor.COLUNA_EMAIL
        ), @UniqueConstraint(
                name = "UC_FORNECEDORES_FRN_CNPJ",
                columnNames = Fornecedor.COLUNA_CNPJ
        )}

)
public class Fornecedor {

    public static final String ID_NAME = "FRN_ID";
    public static final String TABLE_NAME ="FORNECEDORES";
    public static final String SEQUENCE_NAME = "FORNECEDORES_SEQUENCE";
    public static final String COLUNA_DESCRICAO = "FRN_DES";
    public static final String COLUNA_CNPJ = "FRN_CNPJ";
    public static final String COLUNA_EMAIL = "FRN_EMAIL";
    public static final String COLUNA_OBSERVACAO = "FRN_OBS";

    @Id
    @SequenceGenerator(
            name = SEQUENCE_NAME,
            sequenceName = SEQUENCE_NAME,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = SEQUENCE_NAME
    )
    @Column(name=ID_NAME, nullable = false)
    private Long id;

    @Column(name=COLUNA_DESCRICAO, columnDefinition = "VARCHAR2(25)", unique = true, nullable = false)
    @NotBlank
    private String descricao;

    @Column(name=COLUNA_CNPJ, columnDefinition = "VARCHAR2(14)", unique = true, nullable = false)
    @CNPJ
    @NotBlank
    private String cnpj;

    @Column(name=COLUNA_EMAIL, columnDefinition = "VARCHAR2(100)", unique = true, nullable = false)
    @NotBlank
    private String email;

    @Column(name=COLUNA_OBSERVACAO, columnDefinition = "VARCHAR2(255)")
    private String observacao;
}
