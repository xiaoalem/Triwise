package persistence;

import model.Trip;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// represents a writer that writes JSON representation of trip.json to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: open writer; throw FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: write JSON representation of trip.json to file
//    public void write(List<String> participants) {
//        JSONObject json = toJson(participants);
//        saveToFile(json.toString(TAB));
//    }


    public void write(List<Trip> tripList) {
        JSONObject json = toJson(tripList);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // EFFECTS: return list of trip as JSON object
//    private JSONObject toJson(List<String> participants) {
//        JSONObject json = new JSONObject();
//        json.put("name", "List");
//        json.put("participants", stringToJson(participants));
//        return json;
//    }
//
//
//    private JSONArray stringToJson(List<String> participants) {
//        JSONArray jsonArray = new JSONArray();
//        for (String name: participants) {
//            jsonArray.put(name);
//        }
//        return jsonArray;
//    }

    private JSONObject toJson(List<Trip> tripList) {
        JSONObject json = new JSONObject();
        json.put("name", "tripList");
        json.put("aTripList", tripToJson(tripList));
        return json;
    }

    // EFFECTS: return trips of this trip list as a JSON array
    public JSONArray tripToJson(List<Trip> tripList) {
        JSONArray jsonArray = new JSONArray();
        for (Trip t: tripList) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }

}
