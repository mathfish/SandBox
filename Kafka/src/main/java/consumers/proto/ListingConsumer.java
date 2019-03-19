package consumers.proto;

import com.basic.ListingKey;
import com.basic.ListingValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;


@Slf4j
public final class ListingConsumer implements Runnable{

    private final Consumer<ListingKey, ListingValue> consumer;

    public ListingConsumer(Integer clientId) {
        Properties consumerProperties = getConsumerProperties(clientId);
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singleton("Airbnb_Listings"));
    }

    private Properties getConsumerProperties(Integer id) {
        Properties consumerProps = new Properties();
        consumerProps.put("enable.auto.commit", "false");
        consumerProps.put("bootstrap.servers","localhost:9092" );
        consumerProps.put("group.id", "ListingConsumers");
        consumerProps.put("client.id", id);
        consumerProps.put("key.deserializer", "consumers.deserializers.ListingKeyDeserializer");
        consumerProps.put("value.deserializer", "consumers.deserializers.ListingValueDeserializer");
        return consumerProps;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ConsumerRecords<ListingKey, ListingValue> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<ListingKey, ListingValue> record : records) {
                    log.info(buildMessage(record));
                }

                consumer.commitAsync((m,e) -> {
                    if (Objects.nonNull(e)) {
                        log.warn("Failure to commit message:\n " + m, e);
                    }
                });
            }

        } catch (WakeupException exception) {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }

    private String buildMessage(ConsumerRecord<ListingKey, ListingValue> record) {
        String msg = "Topic " + record.topic() +
                "- partition " + record.partition() +
                "- offset" + record.offset() +
                "\nHeader: " + record.headers() +
                "\nKey: " + record.key() +
                "\nListingId: " + record.value().getListingId() +
                ", Num Reviews: " + record.value().getNumberReviews() +
                ", Rating: " + record.value().getRating();
        return msg;
    }
}
