package fatec.grupodois.endurance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
