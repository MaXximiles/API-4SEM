package fatec.grupodois.endurance.entity;

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
public class Usuario implements Serializable {


    @Id
    @SequenceGenerator(name = "usuario_sequence", sequenceName = "usuario_sequence", allocationSize = 1 )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "usuario_sequence" )
    @Column(updatable = false)
    private Long usuarioId;
    
    @NotBlank
    private String usuarioFirstName;
    
    @NotBlank
    private String usuarioLastName;
   
    @CPF
    @NotBlank(message = "Por favor informar um CPF válido.")
    private String usuarioCpf;
    @NotBlank(message = "Por favor informar um RG válido.")
    private String usuarioRg;
    @NotBlank(message = "Por favor informar e-mail.")
    private String usuarioEmail;
    @NotBlank
    private String usuarioPassword;
    //@NotBlank
    private TipoUsuario usuarioTipo;
    private String usuarioImageUrl;
    private Date usuarioLastLoginDate;
    private Date usuarioLastLoginDateDisplay;
    private Date usuarioJoinDate;
    private String[] usuarioRoles;
    private String[] usuarioAuthorities;
    private boolean usuarioIsActive;
    private boolean usuarioIsLocked;
}
