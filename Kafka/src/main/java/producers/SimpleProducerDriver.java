package producers;

import producers.basic.SimpleProducer;

import java.util.stream.Stream;

public class ProducerDriver {

    public static void main(String[] args) {
        String errMsg = "Expected Usage: ProducerDiver ['async or sync', 'topic', 'msg1', 'msg2', ...]";
        if (args.length <= 1) {
            throw new IllegalStateException(errMsg);
        }

        SimpleProducer simpleProducer = new SimpleProducer(args[1]);
        simpleProducer.configure("localhost:9092");
        if (args[0].equals("async")) {
            Stream.of(args).skip(1).map(simpleProducer::asyncProduce).forEach(r -> System.out.println(r.toString()));
        } else if (args[0].equals("sync")) {
            Stream.of(args).skip(1).map(simpleProducer::syncProduce).forEach(r -> System.out.println(r.toString()));
        } else {
            throw new IllegalArgumentException(errMsg);
        }
    }
}
