package model;

import exception.DuplicateNameException;
import exception.EmptyListException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// represents a participant list
public class ParticipantListFunction {
    protected List<Person> participantList = new ArrayList<>(); // participants who attended the trip.json

    // MODIFIES: this
    // EFFECTS: add a new person to participant list
    public void addParticipant(Person p) throws DuplicateNameException {
        if (participantList.contains(p)) {
            throw new DuplicateNameException();
        }
        participantList.add(p);
    }


    // MODIFIES: this
    // EFFECTS: add a new participant list
    public void addParticipantList(List<Person> participantList) throws EmptyListException {
        if (participantList.isEmpty()) {
            throw new EmptyListException();
        }
        this.participantList = participantList;
    }

    // EFFECTS: return participants of this trip.json as a JSON array
    public JSONArray participantToJson(List<Person> participantList) {
        JSONArray jsonArray = new JSONArray();
        for (Person p: participantList) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: trip/item
    // EFFECTS: parse participant from JSON object and add it to trip/item
    public void parseParticipant(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Person person = new Person(name);
        person.addToPayAmount(jsonObject.getDouble("amountToPay"));
        person.addPaidAmount(jsonObject.getDouble("amountPaid"));
        Map<String, Object> payable = jsonObject.getJSONObject("payable").toMap();
        for (Map.Entry<String, Object> entry: payable.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            person.addPayable(key, Double.parseDouble(val.toString()));
        }
        addParticipant(person);
    }
}
