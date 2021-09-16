package fatec.grupodois.endurance.entity;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {


    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "user_sequence"
    )
    @Column(updatable = false)
    private Long userId;
    @NotBlank
    @NotNull
    private String userFirstName;
    @NotBlank
    private String userLastName;
    @CPF
    @NotBlank(message = "Por favor informar um CPF válido.")
    private String userCpf;
    @NotBlank(message = "Por favor informar um RG válido.")
    private String userRg;
    @NotBlank(message = "Por favor informar e-mail.")
    private String userEmail;
    @NotBlank
    private String userPassword;
    @NotBlank
    private TipoUsuario userRole;
    private String userImageUrl;
    private Date userLastLoginDate;
    private Date userLastLoginDateDisplay;
    private Date userJoinDate;
    private String[] userRoles;
    private String[] userAuthorities;
    private boolean userIsActive;
    private boolean userIsLocked;
}
