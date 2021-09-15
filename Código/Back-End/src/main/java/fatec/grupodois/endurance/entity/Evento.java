package fatec.grupodois.endurance.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Evento implements Serializable {

    @Id
    @SequenceGenerator(
            name = "evento_sequence",
            sequenceName = "evento_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "student_sequence"
    )
    private Long eventoId;

    @NotNull
    private LocalDateTime eventoInicio;

    @NotNull
    private LocalDateTime eventoFim;

    @NotBlank(message = "Por favor escolha o local do evento")
    private String eventoLocal;

    @NotBlank(message = "Por favor especifique o tema do evento")
    private String eventoTema;

    private String eventoDescricao;
    private String eventoObservacao;
    private Long usuarioId;

    private LocalDateTime eventoCriacao = LocalDateTime.now();

    private StatusEvento eventoStatus;
}
