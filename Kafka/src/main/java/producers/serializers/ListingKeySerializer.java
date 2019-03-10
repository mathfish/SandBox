package producers.serializers;

import org.apache.kafka.common.serialization.Serializer;
import com.basic.ListingKey;

import java.util.Map;

public class ListingKeySerializer implements Serializer<ListingKey> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //No Op
    }

    @Override
    public byte[] serialize(String topic, ListingKey data) {
        return data.toByteArray();
    }

    @Override
    public void close() {
        //No Op
    }
}
