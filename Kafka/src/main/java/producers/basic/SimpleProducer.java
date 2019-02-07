package producers.basic;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class SimpleProducer {
    private Properties kafkaProps = new Properties();
    private KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);
}
