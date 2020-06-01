package br.com.intelector.microservice.corebrokerkafka.configuration;

import br.com.intelector.microservice.coremicroservice.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
public class KafkaProducerConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    /*@Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                applicationProperties.getAppKafkaAdress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        return props;
    }*/

    /*@Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory(producerConfigs());
    }*/

    /*@Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }*/

}
