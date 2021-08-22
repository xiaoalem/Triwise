package persistence;


import model.ParticipantListFunction;
import model.Item;
import model.Trip;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Represent a reader that reads trip.json from JSON data stored in file
public class JsonReader extends JSONObject {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: read tripList from file and return it;
    public List<Trip> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return addTrips(jsonObject);
    }

    // EFFECTS: reads source file as string and return it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parse tripList from JSON object and returns it
    private List<Trip> addTrips(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("aTripList");
        List<Trip> tripList = new ArrayList<>();
        for (Object json: jsonArray) {
            JSONObject nextTrip = (JSONObject) json;
            parseTrip(tripList, nextTrip);
        }
        return tripList;
    }

    // MODIFIES: tripList
    // EFFECTS: parse trip from JSON object and it to tripList
    private void parseTrip(List<Trip> tripList, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Trip trip = new Trip(name);
        addParticipants(trip, jsonObject);
        addItems(trip,jsonObject);
        tripList.add(trip);
    }

    // MODIFIES: trip
    // EFFECTS: parse participants from JSON object and add them to trip
    private void addParticipants(ParticipantListFunction object, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("participantList");
        for (Object json: jsonArray) {
            JSONObject nextParticipant = (JSONObject) json;
            object.parseParticipant(nextParticipant);
        }
    }

    // MODIFIES: trip
    // EFFECTS: parse items from JSON object and add them to trip
    private void addItems(Trip trip, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("itemList");
        for (Object json: jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            parseItem(trip, nextItem);
        }
    }

    // MODIFIES: trip
    // EFFECTS: parse item from JSON object and add it to trip.json
    private void parseItem(Trip trip, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        double expense = jsonObject.getDouble("expense");
        Item item = new Item(title, expense);
        addParticipants(item, jsonObject);
        item.addPaidByPersonName(jsonObject.getString("nameOfPersonPaid"));
        trip.addItem(item);
    }
}
