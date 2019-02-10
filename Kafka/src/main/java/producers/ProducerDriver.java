package producers;

import producers.basic.SimpleProducer;

import java.util.stream.Stream;

public class ProducerDriver {

    public static void main(String[] args) {
        if (args.length <= 1) {
            throw new IllegalStateException("Expected Usage: ProducerDiver ['topic', 'msg1', 'msg2', ...]");
        }

        SimpleProducer simpleProducer = new SimpleProducer(args[0]);
        simpleProducer.configure("localhost:9092");
        Stream.of(args).skip(1).map(simpleProducer::produce).forEach(r -> System.out.println(r.toString()));
    }
}
