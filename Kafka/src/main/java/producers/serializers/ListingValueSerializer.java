package producers.serializers;

import org.apache.kafka.common.serialization.Serializer;
import com.basic.ListingValue;

import java.util.Map;

public class ListingValueSerializer implements Serializer<ListingValue> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //No Op
    }

    @Override
    public byte[] serialize(String topic, ListingValue data) {
        return data.toByteArray();
    }

    @Override
    public void close() {
        //No Op
    }
}
