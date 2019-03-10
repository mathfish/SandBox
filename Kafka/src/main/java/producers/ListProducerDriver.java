package producers;

import com.basic.ListingKey;
import com.basic.ListingKey.RoomType;
import com.basic.ListingValue;
import com.basic.ListingValue.Price;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import producers.proto.ListingProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class ListProducerDriver {
    private final static String data_loc = "data/listing.csv";
    private final static Map<String, RoomType> stringToRoomType = Map.of("Private room", RoomType.PRIVATE_ROOM);

    public static void main(String[] args) throws IOException, URISyntaxException {
        ListingProducer listingProducer = new ListingProducer("Airbnb_Listings");

        Path csvPath = Paths.get(ListProducerDriver.class.getResource(data_loc).toURI());
        BufferedReader reader = Files.newBufferedReader(csvPath);
        CSVParser csvRecords = new CSVParser(reader, CSVFormat.DEFAULT
                                                              .withFirstRecordAsHeader()
                                                              .withIgnoreHeaderCase()
                                                              .withTrim());

        boolean breakEarly = false;
        int numRecordsToProcess = -1;
        if (args.length == 1) {
            breakEarly = true;
            numRecordsToProcess = Integer.parseInt(args[0]);
        }

        for (CSVRecord record : csvRecords) {
            if (numRecordsToProcess == 0) {
                break;
            }

            int listing_id = Integer.parseInt(record.get("id"));
            String listing_name = record.get("name");
            int host_id = Integer.parseInt(record.get("host_id"));
            String street = record.get("street");
            String neighbourhood = record.get("neighbourhood");
            String city = record.get("city");
            String countryCode = record.get("country_code");
            String roomType = record.get("room_type");
            String propertyType = record.get("property_type");
            int accommodates = Integer.parseInt(record.get("accommodates"));
            String price = record.get("price");
            String cleaningFee = record.get("cleaning_fee");
            String extraPeople = record.get("extra_people");
            int guestsIncluded = Integer.parseInt(record.get("guests_included"));
            int numberReviews = Integer.parseInt(record.get("number_of_reviews"));
            float reviewRating = Float.parseFloat(record.get("review_scores_rating"));
            int reviewsPerMonth = Integer.parseInt(record.get("reviews_per_month"));

            ListingKey listingKey = buildKey(city, countryCode, roomType);
            ListingValue.Builder valueBuilder = getValueBuilder(price, cleaningFee, extraPeople);
            valueBuilder.setListingId(listing_id)
                        .setListingName(listing_name)
                        .setHostId(host_id)
                        .setStreet(street)
                        .setNeighbourhood(neighbourhood)
                        .setPropertyType(propertyType)
                        .setAccommodates(accommodates)
                        .setGuestsIncluded(guestsIncluded)
                        .setNumberReviews(numberReviews)
                        .setRating(reviewRating)
                        .setReviewsPerMonth(reviewsPerMonth);
            ListingValue listingValue = valueBuilder.build();

            listingProducer.produce(listingKey, listingValue);

            if (breakEarly) {
                numRecordsToProcess-=1;
            }
        }
    }

    private static ListingKey buildKey(String city, String countryCode, String roomType) {
        return ListingKey.newBuilder()
                         .setCity(city)
                         .setCountryCode(countryCode)
                         .setRoomType(stringToRoomType.get(roomType))
                         .build();
    }

    private static ListingValue.Builder getValueBuilder(String price, String cleaningFee, String extraPeople) {
        return ListingValue.newBuilder()
                           .setPrice(buildPrice(price))
                           .setCleaningFee(buildPrice(cleaningFee))
                           .setExtraPerson(buildPrice(extraPeople));
    }

    private static Price buildPrice(String price) {
        String delimeters = "\\$|\\.";
        String[] splits = price.split(delimeters);
        if (splits.length != 2) {
            throw new IllegalStateException("Incorrect parsing of price: " + price + " into " + Arrays.toString(splits));
        }

        return Price.newBuilder()
                    .setEuros(Integer.valueOf(splits[0]))
                    .setCents(Integer.valueOf(splits[1]))
                    .build();
    }
}
