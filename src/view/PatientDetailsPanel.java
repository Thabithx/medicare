package view;

import controller.PatientController;
import controller.AppointmentController;
import model.Patient;
import model.AppointmentDetails;
import utils.ImageUploadHelper;
import view.style.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PatientDetailsPanel extends JPanel {

   private static final long serialVersionUID = 1L;
   private Dashboard dashboard;
   private int patientId;
   private Patient patient;

   public PatientDetailsPanel(Dashboard dashboard) {
      this.dashboard = dashboard;
      setLayout(new BorderLayout());
      setBackground(Theme.BG_COLOR);
   }

   public void loadPatientDetails(int patientId) {
      this.patientId = patientId;

      // Get patient data
      List<Patient> patients = PatientController.getAllPatients();
      patient = patients.stream().filter(p -> p.getId() == patientId).findFirst().orElse(null);

      if (patient == null) {
         displayError("Patient not found");
         return;
      }

      buildUI();
   }

   private void buildUI() {
      removeAll();
      setBorder(Theme.createPadding(30));

      // Header
      JPanel header = createHeader();
      add(header, BorderLayout.NORTH);

      // Main content with scroll
      JPanel mainContent = new JPanel();
      mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
      mainContent.setBackground(Theme.BG_COLOR);

      // Top section: Profile pic + Stats
      JPanel topSection = createTopSection();
      mainContent.add(topSection);
      mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

      // Personal Info Card
      JPanel personalInfo = createPersonalInfoCard();
      mainContent.add(personalInfo);
      mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

      // Recent Appointments Card
      JPanel recentAppts = createRecentAppointmentsCard();
      mainContent.add(recentAppts);
      mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

      // Medical Summary Card
      JPanel medicalSummary = createMedicalSummaryCard();
      mainContent.add(medicalSummary);

      JScrollPane scroll = new JScrollPane(mainContent);
      scroll.setBorder(null);
      scroll.getVerticalScrollBar().setUnitIncrement(16);
      add(scroll, BorderLayout.CENTER);

      // Footer buttons
      JPanel footer = createFooter();
      add(footer, BorderLayout.SOUTH);

      revalidate();
      repaint();
   }

   private JPanel createHeader() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(Theme.BG_COLOR);
      panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

      String fullName = patient.getFirstName() + " " + patient.getLastName();
      JLabel title = Theme.createTitleLabel("Patient Details: " + fullName);
      title.setFont(new Font("Segoe UI", Font.BOLD, 28));

      panel.add(title, BorderLayout.WEST);
      return panel;
   }

   private JPanel createTopSection() {
      JPanel panel = new JPanel(new BorderLayout(30, 0));
      panel.setBackground(Theme.BG_COLOR);
      panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

      // Left: Profile Picture
      JPanel picPanel = createCard();
      picPanel.setPreferredSize(new Dimension(250, 250));
      picPanel.setLayout(new GridBagLayout());

      JLabel lblPic = new JLabel();
      ImageIcon icon = ImageUploadHelper.loadProfileImage(patient.getProfilePicturePath(), false, 200, 200);
      ImageIcon circular = ImageUploadHelper.createCircularImage(icon, 200);
      lblPic.setIcon(circular);
      picPanel.add(lblPic);

      // Right: Stats
      JPanel statsPanel = createStatsPanel();

      panel.add(picPanel, BorderLayout.WEST);
      panel.add(statsPanel, BorderLayout.CENTER);

      return panel;
   }

   private JPanel createStatsPanel() {
      JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
      panel.setBackground(Theme.BG_COLOR);

      // Get appointment statistics
      List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
      List<AppointmentDetails> patientAppts = allAppts.stream()
            .filter(a -> a.getPatientId() == patientId)
            .collect(Collectors.toList());

      long total = patientAppts.size();
      long pending = patientAppts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
      long upcoming = patientAppts.stream().filter(a -> "SCHEDULED".equals(a.getStatus())).count();
      long completed = patientAppts.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();

      panel.add(createStatCard("Total Appointments", String.valueOf(total), new Color(227, 242, 253)));
      panel.add(createStatCard("Pending", String.valueOf(pending), new Color(255, 243, 224)));
      panel.add(createStatCard("Upcoming", String.valueOf(upcoming), new Color(232, 245, 233)));
      panel.add(createStatCard("Completed", String.valueOf(completed), new Color(243, 229, 245)));

      return panel;
   }

   private JPanel createStatCard(String label, String value, Color bgColor) {
      JPanel card = new JPanel();
      card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
      card.setBackground(bgColor);
      card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

      JLabel lblValue = new JLabel(value);
      lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
      lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

      JLabel lblLabel = new JLabel(label);
      lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      lblLabel.setForeground(new Color(100, 100, 100));
      lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

      card.add(lblValue);
      card.add(Box.createRigidArea(new Dimension(0, 5)));
      card.add(lblLabel);

      return card;
   }

   private JPanel createPersonalInfoCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("Personal Information");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      JPanel grid = new JPanel(new GridLayout(0, 2, 20, 12));
      grid.setBackground(Color.WHITE);

      addInfoRow(grid, "Full Name:", patient.getFirstName() + " " + patient.getLastName());
      addInfoRow(grid, "Email:", patient.getEmail());
      addInfoRow(grid, "Phone:", patient.getPhone());
      addInfoRow(grid, "Gender:", patient.getGender());
      addInfoRow(grid, "Date of Birth:", patient.getDob() != null ? patient.getDob().toString() : "N/A");
      addInfoRow(grid, "Blood Group:", patient.getBloodGroup());
      addInfoRow(grid, "Address:", patient.getAddress());

      card.add(grid, BorderLayout.CENTER);
      return card;
   }

   private JPanel createRecentAppointmentsCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("Recent Appointments");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      // Get all appointments for this patient
      List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
      List<AppointmentDetails> patientAppts = allAppts.stream()
            .filter(a -> a.getPatientId() == patientId)
            .sorted((a1, a2) -> {
               int dateCompare = a2.getAppointmentDate().compareTo(a1.getAppointmentDate());
               if (dateCompare != 0)
                  return dateCompare;
               return a2.getAppointmentTime().compareTo(a1.getAppointmentTime());
            })
            .collect(Collectors.toList());

      String[] columns = { "ID", "Date", "Time", "Doctor", "Status", "Prescription", "Remarks" };
      DefaultTableModel model = new DefaultTableModel(columns, 0) {
         public boolean isCellEditable(int r, int c) {
            return false;
         }
      };

      for (AppointmentDetails appt : patientAppts) {
         model.addRow(new Object[] {
               appt.getId(),
               appt.getAppointmentDate(),
               appt.getAppointmentTime(),
               appt.getDoctorName(),
               appt.getStatus(),
               (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) ? "Yes" : "No",
               appt.getRemarks() != null ? appt.getRemarks() : ""
         });
      }

      JTable table = new JTable(model);
      Theme.applyTableStyling(table);

      JScrollPane scroll = new JScrollPane(table);
      scroll.setPreferredSize(new Dimension(0, 300));
      card.add(scroll, BorderLayout.CENTER);

      JButton btnViewPrescription = Theme.createGradientButton("View Prescription");
      btnViewPrescription.setPreferredSize(new Dimension(180, 35));
      btnViewPrescription.addActionListener(e -> {
         int row = table.getSelectedRow();
         if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment.");
            return;
         }
         int id = Integer.parseInt(table.getValueAt(row, 0).toString());
         AppointmentDetails sel = patientAppts.stream().filter(a -> a.getId() == id).findFirst().orElse(null);

         if (sel != null && sel.getPrescriptionImagePath() != null && !sel.getPrescriptionImagePath().isEmpty()) {
            viewPrescriptionImage(sel.getPrescriptionImagePath());
         } else {
            JOptionPane.showMessageDialog(this, "No prescription uploaded.");
         }
      });

      JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      actionPanel.setBackground(Color.WHITE);
      actionPanel.add(btnViewPrescription);
      card.add(actionPanel, BorderLayout.SOUTH);

      return card;
   }

   private void viewPrescriptionImage(String path) {
      java.io.File imgFile = new java.io.File(path);
      if (imgFile.exists()) {
         JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Prescription", true);
         dialog.setSize(600, 800);
         dialog.setLayout(new BorderLayout());

         JLabel imgLabel = new JLabel();
         imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

         ImageIcon icon = new ImageIcon(path);
         Image img = icon.getImage().getScaledInstance(550, 750, java.awt.Image.SCALE_SMOOTH);
         imgLabel.setIcon(new ImageIcon(img));

         dialog.add(new JScrollPane(imgLabel), BorderLayout.CENTER);
         dialog.setLocationRelativeTo(this);
         dialog.setVisible(true);
      } else {
         JOptionPane.showMessageDialog(this, "File not found.");
      }
   }

   private JPanel createMedicalSummaryCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("Medical Summary");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      JPanel content = new JPanel();
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      content.setBackground(Color.WHITE);

      addMedicalSection(content, "Current Medications:",
            patient.getMedications() != null && !patient.getMedications().isEmpty()
                  ? patient.getMedications()
                  : "None recorded");

      addMedicalSection(content, "Known Allergies:",
            patient.getAllergies() != null && !patient.getAllergies().isEmpty()
                  ? patient.getAllergies()
                  : "None recorded");

      addMedicalSection(content, "Medical History:",
            patient.getMedicalHistory() != null && !patient.getMedicalHistory().isEmpty()
                  ? patient.getMedicalHistory()
                  : "None recorded");

      card.add(content, BorderLayout.CENTER);
      return card;
   }

   private void addMedicalSection(JPanel parent, String title, String content) {
      JLabel lblTitle = new JLabel(title);
      lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
      lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

      JTextArea txtContent = new JTextArea(content);
      txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      txtContent.setLineWrap(true);
      txtContent.setWrapStyleWord(true);
      txtContent.setEditable(false);
      txtContent.setBackground(Color.WHITE);
      txtContent.setAlignmentX(Component.LEFT_ALIGNMENT);

      parent.add(lblTitle);
      parent.add(Box.createRigidArea(new Dimension(0, 5)));
      parent.add(txtContent);
      parent.add(Box.createRigidArea(new Dimension(0, 15)));
   }

   private void addInfoRow(JPanel grid, String label, String value) {
      JLabel lblLabel = new JLabel(label);
      lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

      JLabel lblValue = new JLabel(value != null ? value : "N/A");
      lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));

      grid.add(lblLabel);
      grid.add(lblValue);
   }

   private JPanel createCard() {
      JPanel card = new JPanel();
      card.setBackground(Color.WHITE);
      card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)));
      return card;
   }

   private JPanel createFooter() {
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
      panel.setBackground(Theme.BG_COLOR);
      panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

      JButton btnBack = Theme.createButton("← Back to List", Theme.INFO);
      btnBack.addActionListener(e -> {
         if (service.SessionManager.isDoctor()) {
            dashboard.switchPanel("Appointments");
         } else {
            dashboard.switchPanel("Patients");
         }
      });

      panel.add(btnBack);
      return panel;
   }

   private void displayError(String message) {
      removeAll();
      JLabel error = new JLabel(message, SwingConstants.CENTER);
      error.setFont(new Font("Segoe UI", Font.BOLD, 18));
      add(error, BorderLayout.CENTER);
      revalidate();
      repaint();
   }
}
