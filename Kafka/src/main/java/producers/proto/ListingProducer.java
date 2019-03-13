package producers.proto;

import com.basic.ListingKey;
import com.basic.ListingValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ListingProducer {
    private Properties kafkaProps = new Properties();
    private final String topic;
    private KafkaProducer<ListingKey, ListingValue> producer;

    public ListingProducer(String topic) {
        this.topic = topic;
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "producers.serializers.ListingKeySerializer");
        kafkaProps.put("value.serializer","producers.serializers.ListingValueSerializer");
        kafkaProps.put("retries", 3);
        kafkaProps.put("acks", "1");
        kafkaProps.put("linger.ms", 5);
        kafkaProps.put("client.id", "listProd");
        producer = new KafkaProducer<>(kafkaProps);
    }


    public void produce(ListingKey key, ListingValue value) {
        ProducerRecord<ListingKey, ListingValue> record = new ProducerRecord<>(this.topic, key, value);
        producer.send(record, new ListingCallBack());
    }


    private class ListingCallBack implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (Objects.nonNull(exception)) {
                log.warn("Error writing to topic " + metadata.topic()
                        + " partition " + metadata.partition()
                        + " offset " + metadata.offset()
                        + " with key size " + metadata.serializedKeySize()
                        + "with value size" + metadata.serializedValueSize(), exception);
            }

        }
    }
}
