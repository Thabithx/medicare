package view;

import javax.swing.*;
import java.awt.*;

public class DashboardHomePanel extends JPanel {
    public DashboardHomePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Welcome to MediCare Plus", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(52, 73, 94));
        add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createCard("Total Patients", "120"));
        statsPanel.add(createCard("Total Doctors", "25"));
        statsPanel.add(createCard("Appointments Today", "38"));

        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        card.setLayout(new BorderLayout());
        card.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(valLabel, BorderLayout.CENTER);

        return card;
    }
}
