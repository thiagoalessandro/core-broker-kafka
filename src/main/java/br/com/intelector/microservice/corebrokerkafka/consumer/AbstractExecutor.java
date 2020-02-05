package br.com.intelector.microservice.corebrokerkafka.consumer;

import br.com.intelector.microservice.coremicroservice.utils.MessageUtils;
import br.com.totvs.tcb.api.model.LogEventoMensageria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractExecutor<T extends AbstractConsumerDTO> {

    protected static final String USUARIO_ATUALIZACAO = "EVENTO";

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageUtils messages;

    public abstract void executar(T mensagemDTO, LogEventoMensageria logEventoMensageria);
}
