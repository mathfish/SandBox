package consumers;

import consumers.proto.ListingConsumer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ListingConsumerDriver {


    public static void main(String[] args) {
        int numConsumers = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
        List<ListingConsumer> consumers = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
            log.info("Creating consumer id " + i);
            ListingConsumer listingConsumer = new ListingConsumer(i);
            consumers.add(listingConsumer);
            executor.submit(listingConsumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    for (ListingConsumer consumer : consumers) {
                        consumer.shutdown();
                    }
                    executor.shutdown();
                    try {
                        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        log.error("Error encountered shutting down executor", e);
                    }
                }
        ));

    }
}
