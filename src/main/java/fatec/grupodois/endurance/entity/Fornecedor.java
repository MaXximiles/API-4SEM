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
        name = "FORNECEDORES",
        uniqueConstraints = {@UniqueConstraint(
                name = "frn_descricao_unique",
                columnNames = "frn_descricao"
        ), @UniqueConstraint(
                name = "frn_email_unique",
                columnNames = "frn_email"
        ), @UniqueConstraint(
                name = "frn_cnpj_unique",
                columnNames = "frn_cnpj"
        )}

)
public class Fornecedor {

    @Id
    @SequenceGenerator(
            name = "fornecedores_sequence",
            sequenceName = "fornecedores_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "fornecedores_sequence"
    )
    @Column(name="frn_id", nullable = false)
    private Long id;

    @Column(name="frn_descricao")
    @NotBlank
    private String descricao;

    @Column(name="frn_cnpj")
    @CNPJ
    @NotBlank
    private String cnpj;

    @Column(name="frn_email")
    @NotBlank
    private String email;

    @Column(name="frn_obs")
    private String observacao;
}
