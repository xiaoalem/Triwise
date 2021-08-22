package ui;

import exception.EmptyListException;
import model.Person;
import model.Trip;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.MessageDialog;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.*;

// Graphical User Interface
public class TriwiseGUI extends JFrame {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    private final JPanel addPanel;
    private JLabel jlInfo;

    private final DefaultListModel<Person> listOfParticipants;
    private final DefaultComboBoxModel<Character> listOfFirstLetters;

    private Trip banff = new Trip("Banff");
    private java.util.List<Trip> tripList = new ArrayList<>();

    private static final String JSON_STORE = "./data/GUI.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // make the Triwise app GUI
    public TriwiseGUI() {
        super("Triwise");
        tripList.add(banff);
        listOfParticipants = new DefaultListModel<>();
        listOfFirstLetters = new DefaultComboBoxModel<>();

        addPanel = new JPanel(false);
        addPanel.setLayout(new BorderLayout());
        JPanel displayPanel = new JPanel(false);
        displayPanel.setLayout(new BorderLayout());
        jlInfo = new JLabel("Current trip: Banff");

        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.setJMenuBar(createMenu());
        add(createTabbedPane(), BorderLayout.CENTER);
        add(jlInfo, BorderLayout.SOUTH);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        createDataListener();
    }

    // EFFECTS: generate menu bar
    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem menuItemSave = generateSaveMenu();
        JMenuItem menuItemLoad = generateLoadMenu();
        JMenuItem menuItemQuit = generateQuitMenu();

        menu.add(menuItemSave);
        menu.add(menuItemLoad);
        menu.add(menuItemQuit);
        return menuBar;
    }

    // EFFECTS: generate save menu item and save participants to file
    private JMenuItem generateSaveMenu() {
        JMenuItem menuItemSave = new JMenuItem("Save", new ImageIcon("./data/save_icon.jpg"));
        menuItemSave.addActionListener(e -> {
            try {
                jsonWriter.open();
                banff.addParticipantList(getParticipants());
                jsonWriter.write(tripList);
                jsonWriter.close();
                new MessageDialog("Save list to " + JSON_STORE + " successfully!");
            } catch (FileNotFoundException notFoundException) {
                new MessageDialog("Unable to write to file: " + JSON_STORE);
            } catch (EmptyListException exception) {
                jlInfo.setText("No data were saved before.");
            }
        });
        return menuItemSave;
    }

    // EFFECTS: generate load menu item and load participants from file
    private JMenuItem generateLoadMenu() {
        JMenuItem menuItemLoad = new JMenuItem("Load", new ImageIcon("./data/load_icon.jpg"));
        menuItemLoad.addActionListener(e -> {
            try {
                tripList = jsonReader.read();
                banff = tripList.get(0);
                setParticipants(banff.getParticipantList());
                new MessageDialog("Load list from " + JSON_STORE + " successfully!");
            } catch (IOException exception) {
                new MessageDialog("Unable to load from " + JSON_STORE);
            }
        });
        return menuItemLoad;
    }

    // EFFECTS: generate quit menu item and quit function
    private JMenuItem generateQuitMenu() {
        JMenuItem menuItemQuit = new JMenuItem("Quit", new ImageIcon("./data/quit_icon.jpg"));
        menuItemQuit.addActionListener(e -> System.exit(0));
        return menuItemQuit;
    }

    // EFFECTS: copy all elements from default list model to an array list
    private java.util.List<Person> getParticipants() {
        ArrayList<Person> ans = new ArrayList<>();
        for (int i = 0; i < listOfParticipants.getSize(); ++i) {
            ans.add(listOfParticipants.getElementAt(i));
        }
        return ans;
    }

    // MODIFIES: this
    // EFFECTS: copy all elements from a util.List to a default list model
    private void setParticipants(java.util.List<Person> participants) {
        listOfParticipants.removeAllElements();
        for (Person p: participants) {
            if (p != null) {
                listOfParticipants.addElement(p);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: get default combo box model updated when new element is added to default list model
    private void createDataListener() {
        listOfParticipants.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                Set<Character> p1set = new TreeSet<>();
                for (Object p: listOfParticipants.toArray()) {
                    String name = ((Person) p).getName();
                    Character upperCaseChar = Character.toUpperCase(name.charAt(0));
                    p1set.add(upperCaseChar);
                }
                listOfFirstLetters.removeAllElements();
                for (Character c: p1set) {
                    listOfFirstLetters.addElement(c);
                }
            }

            @Override
            public void intervalRemoved(ListDataEvent e) { }

            @Override
            public void contentsChanged(ListDataEvent e) { }
        });
    }

    // MODIFIES: this
    // EFFECTS: add tabbed panel to the frame
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon addIcon = new ImageIcon("./data/add_icon.jpg");
        ImageIcon viewIcon = new ImageIcon("./data/search_icon.jpg");

        JComponent addPane = makeAddPanel();
        tabbedPane.addTab("Add a participant", addIcon, addPane);

        JComponent viewPane = makeViewPanel();
        tabbedPane.addTab("View all participants",viewIcon, viewPane);

        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        return tabbedPane;
    }

    // MODIFIES: this
    // EFFECTS: create add participant panel
    private JComponent makeAddPanel() {
        JPanel inputPanel = new JPanel(false);
        inputPanel.setLayout(new BorderLayout());
        JTextField tf = new JTextField("Enter the name of a participant");
        JButton addButton = new JButton("add");

        inputPanel.add(tf, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        addPanel.add(inputPanel, BorderLayout.NORTH);
        addPanel.add(new JList<>(listOfParticipants), BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            jlInfo.setText("Current trip: Banff");
            jlInfo.setForeground(Color.BLACK);
            String name = tf.getText().trim();
            if (name.length() > 0 && !contain(listOfParticipants, name)) {
                listOfParticipants.addElement(new Person(name));
            } else {
                jlInfo.setText("The name may already exist or please enter a valid name with length > 0.");
                jlInfo.setForeground(Color.RED);
            }
            tf.setText("");
        });
        return addPanel;
    }

    // EFFECTS: return true if default list model contain name, false otherwise
    private boolean contain(DefaultListModel<Person> listOfParticipants, String name) {
        Object[] participants = listOfParticipants.toArray();
        for (Object o: participants) {
            Person p = (Person) o;
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: create search participant panel
    private JComponent makeViewPanel() {
        JPanel viewPanel = new JPanel(false);
        viewPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Search participants by alphabet");
        inputPanel.add(label);
        JComboBox<Character> comboBox = new JComboBox<>(listOfFirstLetters);
        DefaultListModel<String> modelMatchedParticipants = new DefaultListModel<>();
        inputPanel.add(comboBox);
        viewPanel.add(inputPanel, BorderLayout.NORTH);
        viewPanel.add(new JList<>(modelMatchedParticipants), BorderLayout.CENTER);
        comboBox.addActionListener(e -> {
            Character ch = (Character) comboBox.getSelectedItem();
            modelMatchedParticipants.removeAllElements();
            for (Object o: listOfParticipants.toArray()) {
                Person p = (Person) o;
                if (ch != null && (p.getName().toUpperCase().startsWith(ch.toString()))) {
                    modelMatchedParticipants.addElement(p.getName());
                }
            }
        });
        return viewPanel;
    }
}