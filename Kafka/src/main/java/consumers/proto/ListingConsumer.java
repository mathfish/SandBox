package consumers.proto;

import com.basic.ListingKey;
import com.basic.ListingValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.*;


@Slf4j
public final class ListingConsumer implements Runnable{

    private final Consumer<ListingKey, ListingValue> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets;
    private int count = 0;
    private final int id;

    public ListingConsumer(Integer clientId) {
        Properties consumerProperties = getConsumerProperties(clientId);
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singleton("Airbnb_Listings"), new HandleListingConsumersReblance());
        currentOffsets = new HashMap<>();
        id = clientId;
    }

    private Properties getConsumerProperties(Integer id) {
        Properties consumerProps = new Properties();
        consumerProps.put("enable.auto.commit", "false");
        consumerProps.put("bootstrap.servers","localhost:9092" );
        consumerProps.put("group.id", "ListingProtoConsumers");
        consumerProps.put("client.id", String.valueOf(id));
        consumerProps.put("auto.offset.reset","earliest");
        consumerProps.put("key.deserializer", "consumers.proto.deserializers.ListingKeyDeserializer");
        consumerProps.put("value.deserializer", "consumers.proto.deserializers.ListingValueDeserializer");
        return consumerProps;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ConsumerRecords<ListingKey, ListingValue> records = consumer.poll(Duration.ofMillis(100));
                log.info("num records: " + records.count());
                for (ConsumerRecord<ListingKey, ListingValue> record : records) {
                    log.info("record from consumer " + getId() + ": " + buildMessage(record));
                    currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                                       new OffsetAndMetadata(record.offset() + 1, "no meta"));
                    count++;
                    if (count % 100 == 0) {
                        consumer.commitAsync(currentOffsets, (m, e) -> {
                            if (Objects.nonNull(e)) {
                                log.warn("Failure to commit message:\n " + m, e);
                            }
                        });
                    }
                }
            }

        } catch (WakeupException exception) {
            log.info("Wakeup invoked on consumer" + this.id);
        } catch (Exception exception) {
            log.error("Unexpected Error Occurred", exception);
        }
        finally {
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


    private class HandleListingConsumersReblance implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            //no op
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            log.info("Partitions After Rebalance:" +  partitions.toString());
            consumer.commitSync(currentOffsets);
        }
    }
}
