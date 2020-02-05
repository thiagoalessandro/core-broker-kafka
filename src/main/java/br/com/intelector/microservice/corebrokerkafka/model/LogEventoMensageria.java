package br.com.intelector.microservice.corebrokerkafka.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "tbl_log_evento_mensageria", indexes = {@Index(name = "idx_uuid", columnList = "uuid")})
@NoArgsConstructor
public class LogEventoMensageria {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "topico")
    private String topico;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dh_evento")
    private Date dhEvento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dh_processado")
    private Date dhProcessado;

    public LogEventoMensageria(String uuid, Date dhEvento, String topico) {
        this.uuid = uuid;
        this.dhEvento = dhEvento;
        this.topico = topico;
    }

    @PrePersist
    public void prePersist(){
        dhProcessado = new Date();
    }
}
