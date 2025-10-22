package view;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private JPanel sidebar, contentPanel;
    private CardLayout cardLayout;

    private AddPatientPanel addPatientPanel;
    private EditPatientPanel editPatientPanel;

    public Dashboard() {
        setTitle("MediCare Plus - Patient Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setLayout(new GridLayout(10, 1, 0, 10));

        JLabel title = new JLabel("MediCare Plus", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sidebar.add(title);

        String[] buttons = {"Dashboard", "Patients", "Doctors", "Appointments", "Reports", "Notifications"};
        for (String btnName : buttons) {
            JButton btn = new JButton(btnName);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(99, 110, 114));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBorderPainted(false);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(178, 190, 195));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(99, 110, 114));
                }
            });

            btn.addActionListener(e -> switchPanel(btnName));
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        
        contentPanel.add(new DashboardHomePanel(), "Dashboard");
        contentPanel.add(new ManagePatientsPanel(this), "Patients");
        contentPanel.add(new DoctorPanel(), "Doctors");
        contentPanel.add(new AppointmentPanel(), "Appointments");
        contentPanel.add(new ReportPanel(), "Reports");
        contentPanel.add(new NotificationPanel(), "Notifications");

        
        addPatientPanel = new AddPatientPanel(this);
        contentPanel.add(addPatientPanel, "AddPatient");

        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    public void showEditPatientPanel(int id, String fName, String lName, String gender,
                                     String dob, String bloodGroup, String phone,
                                     String email, String address) {
        editPatientPanel = new EditPatientPanel(this, id, fName, lName, gender, dob, bloodGroup,
                                                phone, email, address);
        contentPanel.add(editPatientPanel, "EditPatient");
        switchPanel("EditPatient");
    }

    public AddPatientPanel getAddPatientPanel() {
        return addPatientPanel;
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
