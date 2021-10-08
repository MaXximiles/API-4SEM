package fatec.grupodois.endurance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(
        name = "EVENTOS",
        uniqueConstraints = @UniqueConstraint(
                name = "evt_tema_unique",
                columnNames = "evt_tema"
        )

)
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
    @NotBlank()
    @Column(name="evt_local", nullable = false)
    private String local;
    @NotBlank()
    @Column(name="evt_tema", nullable = false)
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

    @ManyToMany
    @JoinTable(
            name="evento_usuario_part",
            joinColumns = @JoinColumn(
                    name = "eup_evt_id",
                    referencedColumnName = "evt_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "eup_usr_id",
                    referencedColumnName = "usr_id"
            )
    )
    private List<User> participantes;

    public boolean addParticipante(User user) {
        if(this.maxParticipantes > this.totalParticipantes) {
            participantes.add(user);
            this.maxParticipantes++;
            return true;
        }
        return false;
    }
}
