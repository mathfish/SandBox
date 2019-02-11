package producers.basic;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimpleProducer {
    private Properties kafkaProps = new Properties();
    private KafkaProducer<String, String> producer;
    private String topic;

    public SimpleProducer(String topic) {
        this.topic = topic;
    }

    public void configure(String brokerList) {
        kafkaProps.put("bootstrap.servers", brokerList);
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(kafkaProps);
    }

    public RecordMetadata syncProduce(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, msg);
        RecordMetadata recordMetadata = null;
        try {
            recordMetadata = producer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return recordMetadata;
    }

    public Future<RecordMetadata> asyncProduce(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, msg);
        return producer.send(record, new SimpleCallBack());
    }

    private class SimpleCallBack implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (Objects.nonNull(exception)) {
                System.out.println("Error Writing to topic: " + metadata.topic());
                exception.printStackTrace();
            }
        }
    }
}
