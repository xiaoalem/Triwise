package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.List;

// Represents an item having a title, list of people, expense (in CAD),
// amount shared by each person and the name of person who paid for it
public class Item extends ParticipantListFunction implements Writable {
    private final String title;                    // item name
    private final double expense;                  // price of the item
    private String nameOfPersonPaid;               // name of the person who paid for the item

    /*
     * REQUIRES: itemTitle has a non-zero length, price >= 0
     * EFFECTS: title on item is set to itemTitle; participantList is initiated to an empty ArrayList
     *          expense on item is set to price and nameOfPersonPaid is set to null.
     */
    public Item(String itemTitle, double price) {
        title = itemTitle;
        expense = price;
        nameOfPersonPaid = null;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public List<Person> getParticipant() {
        return participantList;
    }

    public String getPaidPersonName() {
        return nameOfPersonPaid;
    }

    public double getExpense() {
        return expense;
    }

    // REQUIRES: String name has non-zero length
    // MODIFIES: this
    // EFFECTS: add paid person's name
    public void addPaidByPersonName(String name) {
        nameOfPersonPaid = name;
    }

    // MODIFIES: this and Person in participantList
    // EFFECTS: split the expense and assign the amount need to pay to the shared participants
    public void split() {
        int numberOfShare = participantList.size();
        double eachSharedAmount = Math.round(expense / numberOfShare * 100);
        eachSharedAmount /= 100;
        for (Person p: participantList) {
            if (!p.getName().equals(nameOfPersonPaid)) {
                p.addPayable(nameOfPersonPaid, eachSharedAmount);
                p.addToPayAmount(eachSharedAmount);
            } else {
                p.addPaidAmount(expense);
            }
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("participantList", participantToJson(participantList));
        json.put("expense", expense);
        json.put("nameOfPersonPaid", nameOfPersonPaid);
        return json;
    }
}
