package view;

import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {
    public NotificationPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Notifications", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTextArea dummy = new JTextArea("System alerts and notifications for doctors & patients will appear here.");
        dummy.setEditable(false);
        dummy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(dummy, BorderLayout.CENTER);
    }
}
