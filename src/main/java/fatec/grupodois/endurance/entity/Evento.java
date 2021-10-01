package fatec.grupodois.endurance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ToString
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
    @Column(name="evt_inicio", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime inicio;
    @Column(name="evt_fim", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fim;
    @NotBlank(message = "Por favor escolha o local do evento")
    @Column(name="evt_local", nullable = false)
    private String local;
    @NotBlank(message = "Por favor especifique o tema do evento")
    @Column(name="evt_tema")
    private String tema;
    @Column(name="evt_desc")
    private String descricao;
    @Column(name="evt_obs")
    private String observacao;
    @OneToOne
    @JoinColumn(
            name = "evt_usr_id",
            referencedColumnName = "usr_id"
    )
    private User user;
    @Column(name="evt_criacao")
    private LocalDateTime criacao = LocalDateTime.now();
    @Column(name="evt_status", nullable = false)
    private String status;
    @Column(name="evt_max_part", nullable = false)
    private Integer maxParticipantes;
    @Column(name="evt_total_part", nullable = false)
    private Integer totalParticipantes;
}
