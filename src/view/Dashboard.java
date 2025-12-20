package view;

import java.awt.*;
import javax.swing.*;
import controller.AppointmentStatusController;
import model.AppointmentStatus;
import service.SessionManager;
import view.style.Theme;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel mainContentPanel;
    private SidebarPanel sidebarPanel;

    private AppointmentPanel appointmentPanel;
    private DoctorAppointmentsPanel doctorAppointmentsPanel;
    private PatientAppointmentsPanel patientAppointmentsPanel;
    private AdminAppointmentsPanel adminAppointmentsPanel;

    private ManagePatientsPanel managePatientsPanel;
    private ManageDoctorsPanel manageDoctorsPanel;
    private ReportPanel reportPanel;
    private NotificationPanel notificationPanel;

    private PatientProfilePanel patientProfilePanel;
    private DoctorProfilePanel doctorProfilePanel;
    private PatientDetailsPanel patientDetailsPanel;
    private DoctorDetailsPanel doctorDetailsPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Dashboard frame = new Dashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Dashboard() {
        setTitle("Medicare Plus - Professional Healthcare Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Color.GREEN);
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // === SIDEBAR ===
        sidebarPanel = new SidebarPanel(this);
        contentPane.add(sidebarPanel, BorderLayout.WEST);

        // === MAIN CONTENT ===
        mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.setBackground(Theme.BACKGROUND);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.add(mainContentPanel, BorderLayout.CENTER);

        // Dashboard Home
        DashboardHomePanel homePanel = new DashboardHomePanel(this); // Stats cards
        mainContentPanel.add(homePanel, "Dashboard");

        // Appointments
        if (SessionManager.isDoctor()) {
            doctorAppointmentsPanel = new DoctorAppointmentsPanel(this);
            mainContentPanel.add(doctorAppointmentsPanel, "Appointments");
        } else if (SessionManager.isPatient()) {
            patientAppointmentsPanel = new PatientAppointmentsPanel();
            mainContentPanel.add(patientAppointmentsPanel, "Appointments");
        } else if (SessionManager.isAdmin()) {
            adminAppointmentsPanel = new AdminAppointmentsPanel();
            mainContentPanel.add(adminAppointmentsPanel, "Appointments");
        } else {
            appointmentPanel = new AppointmentPanel();
            mainContentPanel.add(appointmentPanel, "Appointments");
        }

        // Patients
        managePatientsPanel = new ManagePatientsPanel(this);
        mainContentPanel.add(managePatientsPanel, "Patients");
        mainContentPanel.add(new AddPatientPanel(this), "AddPatient");

        // Doctors
        manageDoctorsPanel = new ManageDoctorsPanel(this);
        mainContentPanel.add(manageDoctorsPanel, "Doctors");
        mainContentPanel.add(new AddDoctorPanel(this), "AddDoctor");

        // Reports
        reportPanel = new ReportPanel();
        mainContentPanel.add(reportPanel, "Reports");

        // Notifications
        notificationPanel = new NotificationPanel();
        mainContentPanel.add(notificationPanel, "Notifications");

        // Patient Profile
        patientProfilePanel = new PatientProfilePanel();
        mainContentPanel.add(patientProfilePanel, "PatientProfile");

        // Doctor Profile
        doctorProfilePanel = new DoctorProfilePanel();
        mainContentPanel.add(doctorProfilePanel, "DoctorProfile");

        // Main Details Panels (Lazy Init in methods but added here if needed,
        // actually existing methods check null and add them)

        // Default
        switchPanel("Dashboard");
    }

    // === Switch panel ===
    public void switchPanel(String name) {
        CardLayout cl = (CardLayout) mainContentPanel.getLayout();
        cl.show(mainContentPanel, name);

        // Update Sidebar State
        if (sidebarPanel != null) {
            sidebarPanel.setSelected(name);
        }

        // Refresh Logic
        if ("Appointments".equals(name)) {
            if (appointmentPanel != null)
                appointmentPanel.loadAppointmentData();
            if (doctorAppointmentsPanel != null)
                doctorAppointmentsPanel.loadAppointments();
            if (patientAppointmentsPanel != null)
                patientAppointmentsPanel.loadAppointments();
            if (adminAppointmentsPanel != null)
                adminAppointmentsPanel.loadAppointments();
        }
        if ("Patients".equals(name))
            managePatientsPanel.loadPatients();
        if ("Doctors".equals(name))
            manageDoctorsPanel.loadDoctors();
        if ("Dashboard".equals(name)) {
        }
        if ("Notifications".equals(name))
            notificationPanel.loadNotifications();
        if ("DoctorProfile".equals(name))
            doctorProfilePanel.loadProfile();
        if ("PatientProfile".equals(name))
            patientProfilePanel.loadProfile();
    }

    // === Edit Panels ===
    public void showEditPatientPanel(int id, String first, String last, String gender,
            String dob, String blood, String phone, String email, String address) {
        EditPatientPanel panel = new EditPatientPanel(this, id, first, last, gender, dob, blood, phone, email,
                address);
        mainContentPanel.add(panel, "EditPatient");
        switchPanel("EditPatient");
    }

    public void showPatientDetails(int patientId) {
        if (patientDetailsPanel == null) {
            patientDetailsPanel = new PatientDetailsPanel(this);
            mainContentPanel.add(patientDetailsPanel, "PatientDetails");
        }
        patientDetailsPanel.loadPatientDetails(patientId);
        switchPanel("PatientDetails");
    }

    public void showDoctorDetails(int doctorId) {
        if (doctorDetailsPanel == null) {
            doctorDetailsPanel = new DoctorDetailsPanel(this);
            mainContentPanel.add(doctorDetailsPanel, "DoctorDetails");
        }
        doctorDetailsPanel.loadDoctorDetails(doctorId);
        switchPanel("DoctorDetails");
    }

    public void showEditDoctorPanel(int id, String firstName, String lastName, String gender, String address,
            String dob, String phone, String specialty, String qualification, String schedule, String timeslot) {
        EditDoctorPanel editPanel = new EditDoctorPanel(
                this, id, firstName, lastName, gender, address, dob, phone, specialty, qualification, schedule,
                timeslot);
        mainContentPanel.add(editPanel, "EditDoctor");
        switchPanel("EditDoctor");
    }

    public void openStatusUpdateDialog(AppointmentStatus appointment) {
        AppointmentStatusController controller = new AppointmentStatusController();
        StatusUpdateDialog dialog = new StatusUpdateDialog(this, appointment, controller, new StatusTrackingPanel());
        dialog.setVisible(true);
    }
}
