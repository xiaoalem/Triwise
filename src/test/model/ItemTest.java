package model;

import exception.DuplicateNameException;
import exception.EmptyListException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ItemTest {
    private Item itemTest;

    @BeforeEach
    void BeforeEach() {
        itemTest = new Item("Ticket", 250);
        itemTest.addParticipant(new Person("Rose"));
    }

    @Test
    void constructorTest() {
        assertEquals("Ticket", itemTest.getTitle());
        assertEquals(250, itemTest.getExpense());
    }

    @Test
    void addParticipantTest() {
        try {
            itemTest.addParticipant(new Person("Lisa"));
            assertEquals(2, itemTest.getParticipant().size());
            assertEquals("Rose", itemTest.getParticipant().get(0).getName());
            assertEquals("Lisa", itemTest.getParticipant().get(1).getName());
        } catch (DuplicateNameException e) {
            fail("Duplicated name added");
        }
    }

    @Test
    void addDuplicatedParticipantTest() {
        assertEquals("Rose", itemTest.getParticipant().get(0).getName());

        try {
            itemTest.addParticipant(new Person("Rose"));
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
            itemTest.addParticipantList(participantList);
            assertEquals(2, itemTest.getParticipant().size());
            List<Person> participants = itemTest.getParticipant();
            assertEquals("Lisa", participants.get(0).getName());
            assertEquals("Jennie", participants.get(1).getName());
        } catch (EmptyListException e) {
            fail("Participant list cannot be empty!");
        }
    }

    @Test
    void addEmptyParticipantsListTest() {
        List<Person> participantList = new ArrayList<>();
        try {
            itemTest.addParticipantList(participantList);
            fail("Empty list added");
        } catch (EmptyListException e) {
            // expected
        }
    }

    @Test
    void addPaidByPersonTest() {
        itemTest.addPaidByPersonName("Rose");
        assertEquals("Rose", itemTest.getPaidPersonName());
    }

    @Test
    void splitTest() {
        itemTest.addParticipant(new Person("Lisa"));
        itemTest.addParticipant(new Person("Jennie"));
        itemTest.addPaidByPersonName("Rose");
        itemTest.split();

        List<Person> participants = itemTest.getParticipant();
        Person rose = participants.get(0);
        Person lisa = participants.get(1);
        Person jennie = participants.get(2);
        assertEquals(0, rose.getAmountToPay());
        assertEquals(250, rose.getAmountPaid());
        assertEquals(83.33, lisa.getAmountToPay());
        assertEquals(0, jennie.getAmountPaid());
    }

    @Test
    void toJsonTest() {
        JSONObject jsonObject = itemTest.toJson();
        String name = jsonObject.getString("title");
        assertEquals("Ticket", name);
        double expense = jsonObject.getDouble("expense");
        assertEquals(250, expense);
    }
}
