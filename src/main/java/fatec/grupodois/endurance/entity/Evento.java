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
        name = Evento.TABLE_NAME/*,
        uniqueConstraints = @UniqueConstraint(
                name = "UC_EVENTOS_EVT_TEMA",
                columnNames = Evento.COLUNA_TEMA
        )*/

)
public class Evento implements Serializable {

    public static final String ID_NAME = "EVT_ID";
    public static final String TABLE_NAME ="EVENTOS";
    public static final String SEQUENCE_NAME = "EVENTOS_SEQUENCE";
    public static final String COLUNA_INICIO = "EVT_INICIO";
    public static final String COLUNA_FIM = "EVT_FIM";
    public static final String COLUNA_LOCAL = "EVT_LOCAL";
    public static final String COLUNA_TEMA = "EVT_TEMA";
    public static final String COLUNA_DESCRICAO = "EVT_DES";
    public static final String COLUNA_OBSERVACAO = "EVT_OBS";
    public static final String COLUNA_USUARIO = "EVT_USR_ID";
    public static final String COLUNA_CRIACAO = "EVT_CRIACAO";
    public static final String COLUNA_STATUS = "EVT_STATUS";
    public static final String COLUNA_MAX_PARTICIPANTES = "EVT_MAX_PART";
    public static final String COLUNA_TOTAL_PARTICIPANTES = "EVT_TOTAL_PART";


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
    @Column(name=ID_NAME, nullable = false)
    private Long id;
    @Column(name=COLUNA_INICIO, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime inicio;
    @Column(name=COLUNA_FIM, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fim;
    @NotBlank()
    @Column(name=COLUNA_LOCAL, columnDefinition = "VARCHAR2(9)", nullable = false)
    private String local;
    @NotBlank()
    @Column(name=COLUNA_TEMA, columnDefinition = "VARCHAR2(50)", nullable = false, unique = true)
    private String tema;
    @Column(name=COLUNA_DESCRICAO, columnDefinition = "VARCHAR2(150)")
    private String descricao;
    @Column(name=COLUNA_OBSERVACAO, columnDefinition = "VARCHAR2(150)")
    private String observacao;
    @OneToOne
    @JoinColumn(
            name = COLUNA_USUARIO,
            referencedColumnName = "usr_id",
            nullable = false
    )
    private User user;
    @Column(name=COLUNA_CRIACAO, nullable = false)
    private LocalDateTime criacao = LocalDateTime.now();
    @Column(name=COLUNA_STATUS, columnDefinition = "VARCHAR2(10)", nullable = false)
    private String status;
    @Column(name=COLUNA_MAX_PARTICIPANTES, nullable = false)
    private Integer maxParticipantes;
    @Column(name=COLUNA_TOTAL_PARTICIPANTES, nullable = false)
    private Integer totalParticipantes;

    @ManyToMany
    @JoinTable(
            name="evento_usuario_part",
            joinColumns = @JoinColumn(
                    name = "eup_evt_id",
                    referencedColumnName = ID_NAME
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "eup_usr_id",
                    referencedColumnName = User.ID_NAME
            )
    )
    private List<User> participantes;

    @ManyToMany
    @JoinTable(
            name="evento_fornecedor_map",
            joinColumns = @JoinColumn(
                    name = "efm_evt_id",
                    referencedColumnName = ID_NAME
            ),
            inverseJoinColumns = @JoinColumn(
                    name="efm_frn_id",
                    referencedColumnName = Fornecedor.ID_NAME
            )
    )
    private List<Fornecedor> fornecedores;

    public boolean addParticipante(User user) {
        if(this.maxParticipantes > this.totalParticipantes) {
            participantes.add(user);
            this.totalParticipantes++;
            return true;
        }
        return false;
    }
}
