package producers;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ListingProducerDriverTest {
    private final static String data_loc = "data/listings.csv";

    public static void tt() throws URISyntaxException {
        URI uri = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(data_loc)).toURI();
        Path csvPath = Paths.get(uri);
        System.out.println(csvPath);
    }

    @Test
    public void testURI() throws URISyntaxException {
        tt();
    }

}