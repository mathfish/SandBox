package consumers.proto.deserializers;

import com.basic.ListingKey;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class ListingKeyDeserializer implements Deserializer<ListingKey> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // no op
    }

    @Override
    public ListingKey deserialize(String topic, byte[] data) {
        try {
            return ListingKey.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error("Unable to deserialize ListingKey data", e);
            throw new RuntimeException("Unable to deserialize ListingKey data", e);
        }
    }

    @Override
    public void close() {
        // no-op
    }
}
