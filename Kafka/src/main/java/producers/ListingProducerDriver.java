package producers;

import com.basic.ListingKey;
import com.basic.ListingKey.RoomType;
import com.basic.ListingValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import producers.proto.ListingProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ListingProducerDriver {
    private final static String data_loc = "data/listings.csv";
    private final static Map<String, RoomType> stringToRoomType = Map.of("Private room", RoomType.PRIVATE_ROOM,
            "Entire home/apt", RoomType.ENTIRE_HOME_APT,
            "Shared room", RoomType.SHARED_ROOM);

    public static void main(String[] args) throws IOException, ParseException {
        ListingProducer listingProducer = new ListingProducer("Airbnb_Listings");
        InputStream inputStream = Objects.requireNonNull(ListingProducerDriver.class.getClassLoader().getResourceAsStream(data_loc));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

            try {
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
                String cleaningFee = record.get("cleaning_fee").isEmpty() ? "0" : record.get("cleaning_fee");
                NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                float cleaningFeeVal = currencyInstance.parse(cleaningFee).floatValue();
                String extraPeople = record.get("extra_people");
                int guestsIncluded = Integer.parseInt(record.get("guests_included"));
                int numberReviews = Integer.parseInt(record.get("number_of_reviews"));
                float reviewRating = Float.parseFloat(record.get("review_scores_rating").isEmpty() ? "0" : record.get("review_scores_rating"));
                float reviewsPerMonth = Float.parseFloat(record.get("reviews_per_month").isEmpty() ? "0" : record.get("reviews_per_month"));


                ListingKey listingKey = buildKey(city, countryCode, roomType);
                ListingValue listingValue = ListingValue.newBuilder()
                        .setListingId(listing_id)
                        .setListingName(listing_name)
                        .setHostId(host_id)
                        .setStreet(street)
                        .setNeighbourhood(neighbourhood)
                        .setPropertyType(propertyType)
                        .setAccommodates(accommodates)
                        .setGuestsIncluded(guestsIncluded)
                        .setNumberReviews(numberReviews)
                        .setRating(reviewRating)
                        .setReviewsPerMonth(reviewsPerMonth)
                        .setPrice(currencyInstance.parse(price).floatValue())
                        .setCleaningFee(cleaningFeeVal)
                        .setExtraPerson(currencyInstance.parse(extraPeople).floatValue())
                        .build();


                listingProducer.produce(listingKey, listingValue);

                if (breakEarly) {
                    numRecordsToProcess -= 1;
                }

            } catch (NumberFormatException e) {
                log.warn("Error parsing record. Skipping...", e);
            }
        }
    }

    private static ListingKey buildKey(String city, String countryCode, String roomType) {
        return ListingKey.newBuilder()
                .setCity(city)
                .setCountryCode(countryCode)
                .setRoomType(stringToRoomType.getOrDefault(roomType, RoomType.UNRECOGNIZED))
                .build();
    }
}
