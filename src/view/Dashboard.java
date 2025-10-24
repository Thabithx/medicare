package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane; // ✅ <-- FIX: Import added

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panel_1;  // Main content area

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Dashboard frame = new Dashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Dashboard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1023, 745);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Left sidebar
        JPanel panel = new JPanel();
        panel.setBackground(new Color(254, 255, 255));
        panel.setBounds(0, 0, 233, 717);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Medicare");
        lblNewLabel.setForeground(new Color(46, 99, 125));
        lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNewLabel.setBounds(48, 0, 185, 94);
        panel.add(lblNewLabel);

        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setBackground(new Color(120, 183, 218));
        btnDashboard.setBounds(0, 106, 233, 63);
        panel.add(btnDashboard);

        JButton btnAppointments = new JButton("Appointments");
        btnAppointments.setBounds(0, 163, 233, 63);
        panel.add(btnAppointments);

        JButton btnPatients = new JButton("Patients");
        btnPatients.setBounds(0, 220, 233, 63);
        panel.add(btnPatients);

        JButton btnDoctors = new JButton("Doctors");
        btnDoctors.setBounds(0, 277, 233, 63);
        panel.add(btnDoctors);

        JButton btnReports = new JButton("Reports");
        btnReports.setBounds(0, 334, 233, 63);
        panel.add(btnReports);

        JButton btnNotifications = new JButton("Notifications");
        btnNotifications.setBounds(0, 391, 233, 63);
        panel.add(btnNotifications);

        // ✅ Right content area
        panel_1 = new JPanel();
        panel_1.setBounds(234, 0, 789, 723);
        contentPane.add(panel_1);
        panel_1.setLayout(new CardLayout(0, 0));

        // ✅ All panels
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.add(new JLabel("Dashboard Panel"));
        panel_1.add(dashboardPanel, "Dashboard");

        JPanel appointmentsPanel = new JPanel();
        appointmentsPanel.setBackground(Color.WHITE);
        appointmentsPanel.add(new JLabel("Appointments Panel"));
        panel_1.add(appointmentsPanel, "Appointments");

        ManagePatientsPanel managePatientsPanel = new ManagePatientsPanel(this);
        panel_1.add(managePatientsPanel, "Patients");
        panel_1.add(new AddPatientPanel(this), "AddPatient");

        JPanel doctorsPanel = new JPanel();
        doctorsPanel.setBackground(Color.WHITE);
        doctorsPanel.add(new JLabel("Doctors Panel"));
        panel_1.add(doctorsPanel, "Doctors");

        JPanel reportsPanel = new JPanel();
        reportsPanel.setBackground(Color.WHITE);
        reportsPanel.add(new JLabel("Reports Panel"));
        panel_1.add(reportsPanel, "Reports");

        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setBackground(Color.WHITE);
        notificationsPanel.add(new JLabel("Notifications Panel"));
        panel_1.add(notificationsPanel, "Notifications");

        // ✅ Button actions
        btnDashboard.addActionListener(e -> switchPanel("Dashboard"));
        btnAppointments.addActionListener(e -> switchPanel("Appointments"));
        btnPatients.addActionListener(e -> switchPanel("Patients"));
        btnDoctors.addActionListener(e -> switchPanel("Doctors"));
        btnReports.addActionListener(e -> switchPanel("Reports"));
        btnNotifications.addActionListener(e -> switchPanel("Notifications"));

        // Default view
        switchPanel("Dashboard");
    }

    // ✅ Method to switch between panels
    public void switchPanel(String name) {
        CardLayout cl = (CardLayout) panel_1.getLayout();
        cl.show(panel_1, name);
    }

    // Placeholder for edit navigation
    public void showEditPatientPanel(int id, String first, String last, String gender,
    String dob, String blood, String phone, String email, String address) {
		// Create the EditPatientPanel and add it to the CardLayout
		EditPatientPanel editPanel = new EditPatientPanel(
		this, id, first, last, gender, dob, blood, phone, email, address
	);
	
	panel_1.add(editPanel, "EditPatient"); // add it as a new card
		switchPanel("EditPatient");            // switch to the edit panel
	}

}
