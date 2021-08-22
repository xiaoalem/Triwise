package model;

import exception.DuplicateNameException;
import exception.EmptyListException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TripTest {
    private Trip testTrip;

    @BeforeEach
    void runBefore() {
        testTrip = new Trip("Banff");
        testTrip.addParticipant(new Person("Lucy"));
    }

    @Test
    void constructorTest() {
        assertEquals("Banff", testTrip.getTripName());
    }
    @Test
    void addParticipantTest() {
        try {
            testTrip.addParticipant(new Person("Lisa"));
            assertEquals(2, testTrip.getParticipantList().size());
            assertTrue(testTrip.findPerson("Lisa"));
            assertTrue(testTrip.findPerson("Lucy"));
        } catch (DuplicateNameException e) {
            fail("Duplicated name");
        }
    }

    @Test
    void addDuplicateParticipantTest() {
        assertTrue(testTrip.findPerson("Lucy"));
        try {
            testTrip.addParticipant(new Person("Lucy"));
            fail("Duplicated name added");
        } catch (DuplicateNameException e) {
            // expected
        }
    }

    @Test
    void addParticipantsListTest() {
        List<Person> participantList = new ArrayList<>();
        participantList.add(new Person("Lisa"));
        participantList.add(new Person("Jennie"));
        try {
            testTrip.addParticipantList(participantList);
            assertEquals(2, testTrip.getParticipantList().size());
            List<Person> participants = testTrip.getParticipantList();
            assertEquals("Lisa", participants.get(0).getName());
        } catch (EmptyListException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addEmptyParticipantsListTest() {
        List<Person> participantList = new ArrayList<>();
        try {
            testTrip.addParticipantList(participantList);
            fail("Empty list added");
        } catch (EmptyListException e) {
            // expected
        }
    }

    @Test
    void addItemTest() {
        testTrip.addItem(new Item("Ice cream", 25));
        assertEquals(1,testTrip.getItemList().size());
    }

    @Test
    void findPersonFailTest() {
        assertFalse(testTrip.findPerson("Smith"));
    }

    @Test
    void getPersonTest() {
        assertEquals("Lucy", testTrip.getPerson("Lucy").getName());
    }

    @Test
    void getPersonFailTest() {
        assertNull(testTrip.getPerson("Smith"));
    }

    @Test
    void toJsonTest() {
        JSONObject jsonObject = testTrip.toJson();
        String name = jsonObject.getString("name");
        assertEquals("Banff", name);
        JSONArray jsonArray = jsonObject.getJSONArray("participantList");
        List participantList = jsonArray.toList();
        assertEquals(1,participantList.size());
//        Person testPerson = (Person) participantList.get(0);
//        assertEquals("Lucy", testPerson.getName());
    }

    @Test
    void itemToJsonTest() {
        testTrip.addItem(new Item("ticket", 100));
        JSONArray jsonArray = testTrip.itemToJson();
        List itemList = jsonArray.toList();
        assertEquals(1, itemList.size());
    }
}
