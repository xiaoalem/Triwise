package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private Person testPerson;

    @BeforeEach
    void runBefore() {
        testPerson = new Person("Jennie");
    }

    @Test
    void constructorTest() {
        assertEquals("Jennie", testPerson.getName());
    }

    @Test
    void addToPayAmount() {
        testPerson.addToPayAmount(50);
        assertEquals(50.0,testPerson.getAmountToPay());
    }

    @Test
    void addPaidAmountTest() {
        testPerson.addPaidAmount(100);
        assertEquals(100.0,testPerson.getAmountPaid());
    }

    @Test
    void addPayableNonExistTest() {
        testPerson.addPayable("Lisa", 125);
        Map<String, Double> payable = testPerson.payable;
        assertEquals(1,payable.size());
        assertEquals(125.0,payable.get("Lisa"));
    }

    @Test
    void addPayableExistTest() {
        testPerson.addPayable("Lisa", 10);
        testPerson.addPayable("Lisa",25);
        Map<String, Double>  payable = testPerson.payable;
        assertEquals(1,payable.size());
        assertEquals(35.0, payable.get("Lisa"));
    }

    @Test
    void toJsonTest() {
        testPerson.addToPayAmount(10);
        testPerson.addPaidAmount(0);
        JSONObject jsonObject = testPerson.toJson();
        assertEquals("Jennie", jsonObject.getString("name"));
        assertEquals(10,jsonObject.getDouble("amountToPay"));
        assertEquals(0, jsonObject.getDouble("amountPaid"));
    }

    @Test
    void payableToJsonTest() {
        testPerson.addPayable("lisa", 10);
        JSONObject jsonObject = testPerson.payableToJson();
        Map<String, Object> payable = jsonObject.toMap();
        String key = (String) payable.keySet().toArray()[0];
        assertEquals(key, "lisa");
        double value = (double) payable.values().toArray()[0];
        assertEquals(10, value);
    }

    @Test
    void toStringTest() {
        String name = testPerson.toString();
        assertEquals("Jennie", name);
    }

    @Test
    void equalsTest() {
        Person jennieTwo = new Person("Jennie");
        assertTrue(testPerson.equals(jennieTwo));
        assertTrue(testPerson.equals(testPerson));
        assertEquals(jennieTwo.hashCode(), testPerson.hashCode());
    }

    @Test
    void notEqualsTest() {
        Person lisa = new Person("Lisa");
        assertFalse(testPerson.equals(lisa));
        assertFalse(testPerson.equals("Jennie"));
        assertFalse(testPerson.equals(null));
    }
}