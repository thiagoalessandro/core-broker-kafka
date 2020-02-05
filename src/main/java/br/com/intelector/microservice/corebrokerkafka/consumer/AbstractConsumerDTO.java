package br.com.intelector.microservice.corebrokerkafka.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractConsumerDTO implements Serializable {

    @JsonProperty(value = "uuid_mensagem")
    private String uuidMensagem;

    @JsonProperty(value = "uui_transacao")
    private String uuid_transacao;

    @JsonProperty(value = "dh_evento")
    private Date dhEvento;

}
