package model;



import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;

// Represent a person having a name, amount need to pay and already paid (in CAD),
//                           and record of the amount owe to different person
public class Person implements Writable {
    private final String name;               // person's name
    private double amountToPay;              // total amount the person need to pay
    private double amountPaid;               // total amount the person already paid
    protected Map<String, Double> payable;   // the record of the balance the person owes to different person

    /*
     * REQUIRES: name has a non-zero length.
     * EFFECTS: name on person is set to name; toPayAmount and paidAmount are set to 0
     *          payable is set to empty HashMap.
     */
    public Person(String name) {
        this.name = name;
        amountToPay = 0;
        amountPaid = 0;
        payable = new HashMap<>();
    }

    // getters
    public String getName() {
        return this.name;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: add amount to toPayAmount
    public void addToPayAmount(double amount) {
        amountToPay += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: add amount to paidAmount
    public void addPaidAmount(double amount) {
        amountPaid += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: add the amount and the associated person to addPayable
    public void addPayable(String payableName, double amount) {
        if (payable.containsKey(payableName)) {
            double newAmount = payable.get(payableName) + amount;
            payable.put(payableName, newAmount);
        } else {
            payable.put(payableName, amount);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("amountToPay", amountToPay);
        json.put("amountPaid", amountPaid);
        json.put("payable", payableToJson());
        return json;
    }

    // EFFECTS: return payable of this item as a JSON object
    protected JSONObject payableToJson() {
        return new JSONObject(payable);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
