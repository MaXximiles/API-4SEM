package fatec.grupodois.endurance.entity;

import fatec.grupodois.endurance.enumeration.LocalEvento;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "EVENTOS")
public class Evento implements Serializable {

    @Id
    @SequenceGenerator(
            name = "eventos_sequence",
            sequenceName = "eventos_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "eventos_sequence"
    )
    @Column(name="evt_id", nullable = false)
    private Long id;
    @NotBlank
    @Column(name="evt_inicio", nullable = false)
    private LocalDateTime inicio;
    @NotBlank
    @Column(name="evt_fim", nullable = false)
    private LocalDateTime fim;
    @NotBlank(message = "Por favor escolha o local do evento")
    @Column(name="evt_local", nullable = false)
    private LocalEvento local;
    @NotBlank(message = "Por favor especifique o tema do evento")
    @Column(name="evt_tema")
    private String tema;
    @Column(name="evt_desc")
    private String descricao;
    @Column(name="evt_obs")
    private String observacao;
    @Column(name="evt_usr_email")
    private String userEmail;
    @Column(name="evt_criacao")
    private LocalDateTime criacao = LocalDateTime.now();
    @Column(name="evt_status", nullable = false)
    private StatusEvento status;
    @Column(name="evt_max_part", nullable = false)
    private Integer maxParticipantes;
    @Column(name="evt_total_part", nullable = false)
    private Integer totalParticipantes;
}
