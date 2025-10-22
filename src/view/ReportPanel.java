package view;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Monthly Reports", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTextArea dummy = new JTextArea("Reports on patient visits, doctor performance, and appointments.");
        dummy.setEditable(false);
        dummy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(dummy, BorderLayout.CENTER);
    }
}
