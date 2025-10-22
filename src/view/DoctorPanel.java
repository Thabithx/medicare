package view;

import javax.swing.*;
import java.awt.*;

public class DoctorPanel extends JPanel {
    public DoctorPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Manage Doctors", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTextArea dummy = new JTextArea("Doctor management feature coming soon...");
        dummy.setEditable(false);
        dummy.setBackground(new Color(245, 246, 250));
        dummy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(dummy, BorderLayout.CENTER);
    }
}
