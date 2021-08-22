package persistence;

import model.Item;
import model.Person;
import model.Trip;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            List<Trip> tripList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyTripList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyTripList.json");
        try {
            List<Trip> tripList = reader.read();
            assertEquals(0, tripList.size());
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderTripList() {
        JsonReader reader = new JsonReader("./data/testReaderTrip.json");
        try {
            List<Trip> tripList = reader.read();
            assertEquals(1, tripList.size());
            Trip trip = tripList.get(0);
            assertEquals("current trip", trip.getTripName());
            List<Person> participantList = trip.getParticipantList();
            assertEquals(1, participantList.size());
            assertEquals("lisa", participantList.get(0).getName());
            List<Item> itemList = trip.getItemList();
            assertEquals(1, itemList.size());
            assertEquals("ice cream", itemList.get(0).getTitle());
            assertEquals(10, itemList.get(0).getExpense());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
