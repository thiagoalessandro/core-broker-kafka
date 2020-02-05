package br.com.intelector.microservice.corebrokerkafka.consumer;

import br.com.totvs.tcb.api.consumer.executor.AbstractExecutor;
import br.com.totvs.tcb.api.model.LogEventoMensageria;
import br.com.totvs.tcb.api.service.LogEventoMensageriaService;
import br.com.totvs.tcb.api.service.exceptions.MensagemDuplicadaException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Log4j2
public abstract class AbstractConsumer<P extends AbstractExecutor, J extends AbstractConsumerDTO> {

    @Getter
    @Autowired
    private LogEventoMensageriaService logEventoMensageriaService;
    @Autowired
    private StringEncryptor encryptorBean;

    private P processador;
    private String identificadorLog;

    private LogEventoMensageria logEventoMensageria;

    private J messagemDTO;

    AbstractConsumer(P processador,
                     String identificadorLog) {
        this.processador = processador;
        this.identificadorLog = identificadorLog;
    }

    protected abstract String getTopic();

    protected abstract CountDownLatch getLatch();

    public void processaMensagem(String mensagemJson) {
        try {
            String mensagem = this.encryptorBean.decrypt(mensagemJson);
            log.info(String.format("Mensagem %s -> %s", identificadorLog, mensagem));
            messagemDTO = convertToDTO(mensagem);
            if (isMensagemDuplicada(messagemDTO.getUuid()))
                throw new MensagemDuplicadaException();
            log.info(String.format("Processando mensagem %s ", identificadorLog));
            registraLogMensagem(messagemDTO, identificadorLog);
            processador.executar(messagemDTO, logEventoMensageria);
        } catch (MensagemDuplicadaException e) {
            log.warn("Mensagem {} duplicada: ", identificadorLog, e);
        } catch (Exception e) {
            log.warn("Erro ao processar mensagem {}: ", identificadorLog, e);
        } finally {
            getLatch().countDown();
        }
    }

    public void registraLogMensagem(AbstractConsumerDTO messagemDTO, String identificador) {
        log.info(String.format("Registrando log da mensagem %s ", identificador));
        try {
            logEventoMensageria = logEventoMensageriaService.save(messagemDTO, getTopic());
        } catch (Exception e) {
            log.warn(String.format("Erro ao registrar log da mensagem %s: ", identificador), e);
        }
    }

    private boolean isMensagemDuplicada(String uuid) {
        return logEventoMensageriaService.existsByUuid(uuid);
    }

    J convertToDTO(final String mensagem) {
        J json;
        try (KafkaJsonDeserializer<J> converter = new KafkaJsonDeserializer<>()) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put(KafkaJsonDeserializer.DBZ_CLASS_PROPS, getGenericTypeClass());
            converter.configure(mapa, false);

            log.debug("Convertendo Payload da mensagem...");
            json = converter.deserialize(null, mensagem.getBytes());
        }

        if (json == null) {
            log.debug("Não existem mensagens para processar");
            return null;
        }
        return json;
    }

    private Class<J> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<J>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

}
