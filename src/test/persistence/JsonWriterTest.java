package persistence;


import model.Item;
import model.Person;
import model.Trip;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Trip trip = new Trip("banff");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriteTrip() {
        try {
            List<Trip> tripList = new ArrayList<>();
            Trip trip = new Trip("banff");
            Person lisa = new Person("lisa");
            lisa.addPaidAmount(10);
            lisa.addToPayAmount(0);
            trip.addParticipant(lisa);
            Item item = new Item("ice cream", 10);
            item.addPaidByPersonName("lisa");
            item.addParticipant(lisa);
            trip.addItem(item);
            tripList.add(trip);
            JsonWriter writer = new JsonWriter("./data/testWriterTrip.json");
            writer.open();
            writer.write(tripList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterTrip.json");
            tripList = reader.read();
            trip = tripList.get(0);
            assertEquals("banff", trip.getTripName());
            List<Person> participantList = trip.getParticipantList();
            assertEquals(1, participantList.size());
            assertEquals("lisa", participantList.get(0).getName());
            List<Item> itemList = trip.getItemList();
            assertEquals(1, itemList.size());
            assertEquals("ice cream", itemList.get(0).getTitle());
            assertEquals(10, itemList.get(0).getExpense());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
