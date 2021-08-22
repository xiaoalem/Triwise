package ui.tools;

import javax.swing.*;

// represent a message dialog window
public class MessageDialog {
    JFrame frame;

    // construct a message dialog
    public MessageDialog(String msg) {
        frame = new JFrame();
        JOptionPane.showMessageDialog(frame, msg);
    }
}
