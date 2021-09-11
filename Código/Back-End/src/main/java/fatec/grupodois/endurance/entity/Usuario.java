package fatec.grupodois.endurance.entity;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @SequenceGenerator(
            name = "usuario_sequence",
            sequenceName = "usuario_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "usuario_sequence"
    )
    private Long usuarioId;

    @NotBlank
    private String usuarioNome;

    @CPF
    @NotBlank(message = "Por favor informar um CPF válido.")
    private String usuarioCpf;

    @NotBlank(message = "Por favor informar um RG válido")
    private String usuarioRg;

    @NotBlank(message = "Por favor informar e-mail")
    private String usuarioEmail;

    @NotBlank
    private String usuarioSenha;

    @NotBlank
    private TipoUsuario usuarioTipo;
}
