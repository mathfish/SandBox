package consumers.proto.deserializers;

import com.basic.ListingValue;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class ListingValueDeserializer implements Deserializer<ListingValue> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //no op
    }

    @Override
    public ListingValue deserialize(String topic, byte[] data) {
        try {
            return ListingValue.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error("Unable to deserialize ListingValue data", e);
            throw new RuntimeException("Unable to deserialize ListingValute data", e);
        }
    }

    @Override
    public void close() {
        // no op
    }
}
