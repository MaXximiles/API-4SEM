package fatec.grupodois.endurance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "USUARIOS")
public class User implements Serializable {


    @Id
    @SequenceGenerator(
            name = "usuarios_sequence",
            sequenceName = "usuarios_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "user_sequence"
    )
    @Column(name = "usr_id", updatable = false)
    private Long id;
    @NotBlank
    @Column(name = "usr_nome", nullable = false)
    private String firstName;
    @NotBlank
    @Column(name = "usr_sobrenome", nullable = false)
    private String lastName;
    @CPF
    @NotBlank(message = "Por favor informar um CPF válido.")
    @Column(name = "usr_cpf", nullable = false)
    private String cpf;
    @NotBlank(message = "Por favor informar e-mail.")
    @Column(name = "usr_email", nullable = false)
    private String email;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "usr_senha", nullable = false)
    private String password;
    @Column(name = "usr_imagem")
    private String profileImageUrl;
    @Column(name = "usr_vacina")
    private String vaccineImage;
    @Column(name = "usr_ultimo_acesso")
    private Date lastLoginDate;
    @Column(name = "usr_ultimo_acesso_display")
    private Date lastLoginDateDisplay;
    @Column(name = "usr_data_cadastro")
    private Date joinDate;
    @Column(name = "usr_tipo")
    private String role;
    @Column(name = "usr_autoridades")
    private String[] authorities;
    @Column(name = "usr_ativo")
    private boolean isActive;
    @Column(name = "usr_nao_bloqueado")
    private boolean isNotLocked;
}
