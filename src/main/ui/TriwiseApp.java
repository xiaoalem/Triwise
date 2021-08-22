package ui;

import exception.DuplicateNameException;
import model.Item;
import model.Person;
import model.Trip;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Triwise application

public class TriwiseApp extends JFrame {

    private static final String JSON_STORE = "./data/trip.json";
    private List<Trip> tripList;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // EFFECTS: runs the application
    public TriwiseApp() {
        super("Triwise");
        initializeGraphics();

        tripList = new ArrayList<>();
        input = new Scanner(System.in).useDelimiter("\n");
        System.out.println("Welcome to Triwise!");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runTriwise();
    }

    private void initializeGraphics() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    // EFFECTS: process user input
    private void runTriwise() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMainAddMenu();
            command = getCommand();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\n Goodbye!");
    }

    // MODIFIES: this
    // EFFECTS: process user command
    private void processCommand(String command) {
        switch (command) {
            case "view":
                viewTrips();
                break;
            case "add":
                tripList.add(addTrip());
                break;
            case "save":
                saveTripList();
                break;
            case "load":
                loadTripList();
                break;
            default:
                System.out.println("Unknown command. Please select from the list!");
        }
    }

    // EFFECTS: get user input
    private String getCommand() {
        return input.next().toLowerCase().trim();
    }

    // EFFECTS: displays main menu options to user
    private void displayMainAddMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> add a new trip");
        System.out.println("\tview -> view all trips");
        System.out.println("\tsave -> save trip list to file");
        System.out.println("\tload -> load trip list from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays add participants menu options to user
    private void displayAddParticipantMenu() {
        System.out.println("You can add multiple PERSONs by typing add. When finish adding, type save.");
        System.out.println("\tadd -> add a new participant");
        System.out.println("\tsave -> save and done");
    }

    // EFFECTS: displays add item menu options to user
    private void displayAddItemMenu() {
        System.out.println("\nYou can add multiple ITEMs by typing add. When finish adding, type save.");
        System.out.println("\tadd -> add an new item");
        System.out.println("\tsave -> save and done");
    }

    // EFFECTS: displays view trip menu options to user
    private void displayViewTripMenu() {
        System.out.println("\nPlease choose:");
        System.out.println("\ttrip name -> to view details of a trip from above");
        System.out.println("\tback      -> go back to add more new trips");
    }

    // EFFECTS: conduct a new trip
    private Trip addTrip() {
        System.out.println("Add a trip name");
        String tripName = getCommand();
        Trip trip = new Trip(tripName);

        boolean doneAddPerson = false;
        while (!doneAddPerson) {
            displayAddParticipantMenu();
            String commendParticipant = getCommand();
            System.out.println("# Current trip: " + tripName);
            if (commendParticipant.equals("save") && trip.getParticipantList().isEmpty()) {
                System.out.println("Please add a participant first before save.");
                addPerson(trip);
            } else if (commendParticipant.equals("save")) {
                chooseFromDisplayItemMenu(trip);
                doneAddPerson = true;
            } else if (commendParticipant.equals("add")) {
                addPerson(trip);
            } else {
                System.out.println("Please select from below!");
            }
        }
        return trip;
    }

    // MODIFIES: trip
    // EFFECTS: conduct a new person and add to the trip
    private void addPerson(Trip trip) {
        System.out.println("Add a person name (!NO DUPLICATE NAME!)");
        String personName = getCommand();
        try {
            trip.addParticipant(new Person(personName));
        } catch (DuplicateNameException e) {
            System.out.println("!Name already exist, please use another name!\n");
        }
    }

    // MODIFIES: trip
    // EFFECTS: conduct a new item and add to the trip
    private void addItem(Trip trip) {
        System.out.println("Add an item title (!NO DUPLICATE NAME!)");
        String itemTitle = getCommand();
        System.out.println("\nAdd the expense");
        double itemExpense = input.nextDouble();
        Item newItem = new Item(itemTitle,itemExpense);
        trip.addItem(newItem);

        selectSharedOrPaidBy("shared", trip, newItem);
        selectSharedOrPaidBy("paid", trip, newItem);
        newItem.split();
        //viewParticipantsListOfATrip(trip);
    }

    // EFFECTS: view all trips in the tripList
    private void viewTrips() {
        if (tripList.isEmpty()) {
            System.out.println("There is NO TRIP now. Please add a trip first!");
            tripList.add(addTrip());
        } else {
            System.out.println("Here are all the trips:");
            for (Trip t : tripList) {
                System.out.println("\t" + t.getTripName());
            }
            displayViewTripMenu();
            String command = getCommand();
            if (!command.equals("back")) {
                for (Trip t : tripList) {
                    if (t.getTripName().equals(command)) {
                        System.out.println("# Current trip: " + t.getTripName());
                        viewTripBalance(t);
                        viewItems(t);
                        chooseFromDisplayItemMenu(t);
                    }
                }
                System.out.println("No trip named " + command + " is found, please select from above");
                viewTrips();
            }
        }
    }

    // EFFECTS: view the balance of the trip
    private void viewTripBalance(Trip trip) {
        System.out.println("* Trip balance:");
        for (Person p: trip.getParticipantList()) {
            double minus = p.getAmountPaid() - p.getAmountToPay();
            if (minus < 0) {
                System.out.println("\t- " + p.getName() + " needs to pay $" + Math.abs(minus));
            } else if (minus == 0) {
                System.out.println("\t- " + p.getName() + " has $0 to pay");
            }
        }
    }

    // EFFECTS: view all items recorded in the trip
    private void viewItems(Trip trip) {
        if (trip.getItemList().isEmpty()) {
            System.out.println("# Current trip: " + trip.getTripName());
            System.out.println("There is NO ITEM now. Please add an item first");
            addItem(trip);
        } else {
            System.out.println("\n* Item list:");
            List<Item> itemList = trip.getItemList();
            for (Item i : itemList) {
                System.out.println("\n" + "\t# " + i.getTitle() + " $" + i.getExpense());
                System.out.println("\t\t-- paid by " + i.getPaidPersonName());
                System.out.print("\t\t-- shared by ");
                for (Person p: i.getParticipant()) {
                    System.out.print(p.getName() + " ");
                }
            }
        }
    }

    // EFFECTS: prompt user to select add or save
    private void chooseFromDisplayItemMenu(Trip trip) {
        displayAddItemMenu();
        String commendItem = getCommand();
        if (commendItem.equals("add")) {
            addItem(trip);
        } else if (commendItem.equals("save")) {
            viewItems(trip);
        } else {
            System.out.println("Please select from above!");
            chooseFromDisplayItemMenu(trip);
        }
    }


    // MODIFIES: item
    // EFFECTS: prompt user to select and add sharedBy and PaidBy
    private void selectSharedOrPaidBy(String select, Trip trip, Item item) {
        boolean selectDone = false;
        while (!selectDone) {
            System.out.println("# Current trip: " + trip.getTripName());
            System.out.println("Select people who " + select.toUpperCase() + " the bill, save when you are done");
            viewParticipantsListOfATrip(trip);
            String commendSelect = getCommand();
            List<Person>  itemParticipants = item.getParticipant();
            if (commendSelect.equals("save") && (!itemParticipants.isEmpty() || item.getPaidPersonName() == null)) {
                selectDone = true;
            } else if (!trip.findPerson(commendSelect)) {
                System.out.println("Please enter a valid name listed above!!");
            } else if (select.equals("shared")) {
                System.out.println("If multi-person share, add them separately or save if you are done");
                selectSharedBy(trip, item, commendSelect);
            } else if (select.equals("paid")) {
                System.out.println("Only one person who paid could be selected, it's been saved");
                selectPaidBy(trip, item, commendSelect);
                selectDone = true;
            } else {
                System.out.println("Please select a valid person before save!");
            }
        }
    }

    // EFFECTS: view all participants of the trip
    private void viewParticipantsListOfATrip(Trip trip) {
        for (Person p: trip.getParticipantList()) {
            System.out.println("\t" + p.getName());
        }
        System.out.println("save -> save and done");
    }

    // MODIFIES: item
    // EFFECTS: add people who shared the bill of an item
    private void selectSharedBy(Trip trip, Item item, String name) {
        item.addParticipant(trip.getPerson(name));
    }

    // MODIFIES: item
    // EFFECTS: add people who paid ofr the bill of an item
    private void selectPaidBy(Trip trip, Item item, String name) {
        for (Person p: trip.getParticipantList()) {
            if (p.getName().equals(name)) {
                item.addPaidByPersonName(name);
                break;
            }
        }
    }

    //EFFECTS: save the trip to file.
    private void saveTripList() {
        try {
            jsonWriter.open();
            jsonWriter.write(tripList);
            jsonWriter.close();
            System.out.println("Saved trip list to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads workroom from file
    private void loadTripList() {
        try {
            tripList = jsonReader.read();
            System.out.println("Loaded trip list from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
