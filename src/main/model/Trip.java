package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an trip.json having a name, a list of participants and a list of items entertained
public class Trip extends ParticipantListFunction implements Writable {
    private final String name;                     // trip.json name
    private final List<Item> itemList;             // items entertained at this trip.json

    /*
     * REQUIRES: tripName has a non-zero length
     * EFFECTS: name on trip.json is set to tripName; participantList and itemList
     *          are initiated to empty ArrayLists
     */
    public Trip(String tripName) {
        name = tripName;
        itemList = new ArrayList<>();
    }

    // getters
    public String getTripName() {
        return name;
    }

    public List<Person> getParticipantList() {
        return participantList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    // REQUIRES: Item item does not exist in itemList
    // MODIFIES: this
    // EFFECTS: add an item to the item list
    public void addItem(Item item) {
        itemList.add(item);
    }

    // EFFECTS: return true if a person with given name exists in participants list, false otherwise
    public boolean findPerson(String name) {
        for (Person p : participantList) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: String name is different from the names of Person in participantList
    // EFFECTS: return person with given name if exist, null otherwise
    public Person getPerson(String name) {
        for (Person p: participantList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("participantList", participantToJson(participantList));
        json.put("itemList", itemToJson());
        return json;
    }

    // EFFECTS: return items of this trip as a JSON array
    public JSONArray itemToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Item i: itemList) {
            jsonArray.put(i.toJson());
        }
        return jsonArray;
    }
}
