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
@Table(name = User.TABLE_NAME)
public class User implements Serializable {

    public static final String TABLE_NAME = "USUARIOS";
    public static final String ID_NAME = "USR_ID";
    public static final String SEQUENCE_NAME = "USUARIOS_SEQUENCE";
    public static final String COLUNA_NOME = "USR_NOME";
    public static final String COLUNA_SOBRENOME = "USR_SOBRENOME";
    public static final String COLUNA_CPF = "USR_CPF";
    public static final String COLUNA_EMAIL = "USR_EMAIL";
    public static final String COLUNA_SENHA = "USR_SENHA";
    public static final String COLUNA_IMAGEM = "USR_IMAGEM";
    public static final String COLUNA_VACINA = "USR_VACINA";
    public static final String COLUNA_ULTIMO_ACESSO = "USR_ULTIMO_ACESSO";
    public static final String COLUNA_ULTIMO_ACESSO_DISPLAY = "USR_ULTIMO_ACESSO_DISPLAY";
    public static final String COLUNA_DATA_CADASTRO = "USR_DATA_CADASTRO";
    public static final String COLUNA_TIPO = "USR_TIPO";
    public static final String COLUNA_AUTORIDADES = "USR_AUTORIDADES";
    public static final String COLUNA_ATIVO = "USR_ATIVO";
    public static final String COLUNA_BLOQUEADO = "USR_NAO_BLOQUEADO";


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
    @Column(name = ID_NAME, updatable = false)
    private Long id;
    @NotBlank
    @Column(name = COLUNA_NOME, nullable = false)
    private String firstName;
    @NotBlank
    @Column(name = COLUNA_SOBRENOME, nullable = false)
    private String lastName;
    @CPF
    @NotBlank()
    @Column(name = COLUNA_CPF, nullable = false, unique = true)
    private String cpf;
    @NotBlank()
    @Column(name = COLUNA_EMAIL, nullable = false, unique = true)
    private String email;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = COLUNA_SENHA, nullable = false)
    private String password;
    @Column(name = COLUNA_IMAGEM)
    private String profileImageUrl;
    @Column(name = COLUNA_VACINA)
    private String vaccineImage;
    @Column(name = COLUNA_ULTIMO_ACESSO)
    private Date lastLoginDate;
    @Column(name = COLUNA_ULTIMO_ACESSO_DISPLAY)
    private Date lastLoginDateDisplay;
    @Column(name = COLUNA_DATA_CADASTRO)
    private Date joinDate;
    @Column(name = COLUNA_TIPO)
    private String role;
    @Column(name = COLUNA_AUTORIDADES)
    private String[] authorities;
    @Column(name = COLUNA_ATIVO)
    private boolean isActive;
    @Column(name = COLUNA_BLOQUEADO)
    private boolean isNotLocked;
}
