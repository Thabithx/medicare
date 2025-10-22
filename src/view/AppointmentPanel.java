package view;

import javax.swing.*;
import java.awt.*;

public class AppointmentPanel extends JPanel {
    public AppointmentPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Schedule & Manage Appointments", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTextArea dummy = new JTextArea("Appointment scheduling and tracking features will go here.");
        dummy.setEditable(false);
        dummy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(dummy, BorderLayout.CENTER);
    }
}
