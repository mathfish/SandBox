package consumers.proto;

import com.basic.ListingKey;
import com.basic.ListingValue;
import org.apache.kafka.clients.consumer.Consumer;
import scala.sys.Prop;

import java.util.Properties;

public final class ListingConsumer implements Runnable{

    private ListingConsumer() {

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

    Consumer<ListingKey, ListingValue> makeListingConsumer(int id) {


        return null;
    }

    @Override
    public void run() {
        
    }
}
