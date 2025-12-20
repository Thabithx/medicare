package view;

import controller.DoctorController;
import controller.AppointmentController;
import model.Doctor;
import model.AppointmentDetails;
import utils.ImageUploadHelper;
import view.style.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorDetailsPanel extends JPanel {

   private static final long serialVersionUID = 1L;
   private Dashboard dashboard;
   private int doctorId;
   private Doctor doctor;

   public DoctorDetailsPanel(Dashboard dashboard) {
      this.dashboard = dashboard;
      setLayout(new BorderLayout());
      setBackground(Theme.BG_COLOR);
   }

   public void loadDoctorDetails(int doctorId) {
      this.doctorId = doctorId;

      // Get doctor data
      List<Doctor> doctors = DoctorController.getAllDoctors();
      doctor = doctors.stream().filter(d -> d.getId() == doctorId).findFirst().orElse(null);

      if (doctor == null) {
         displayError("Doctor not found");
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

      // Professional Info Card
      JPanel professionalInfo = createProfessionalInfoCard();
      mainContent.add(professionalInfo);
      mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

      // Recent Appointments Card
      JPanel recentAppts = createAppointmentHistoryCard();
      mainContent.add(recentAppts);
      mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

      // Monthly Statistics Card
      JPanel monthlyStats = createMonthlyStatsCard();
      mainContent.add(monthlyStats);

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

      String fullName = "Dr. " + doctor.getFirstName() + " " + doctor.getLastName();
      JLabel title = Theme.createTitleLabel(fullName + " - " + doctor.getSpecialty());
      title.setFont(new Font("Segoe UI", Font.BOLD, 28));
      title.setForeground(new Color(10, 80, 255));

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
      ImageIcon icon = ImageUploadHelper.loadProfileImage(doctor.getProfilePicturePath(), true, 200, 200);
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
      List<AppointmentDetails> doctorAppts = allAppts.stream()
            .filter(a -> a.getDoctorId() == doctorId)
            .collect(Collectors.toList());

      long totalPatients = doctorAppts.stream()
            .map(AppointmentDetails::getPatientId)
            .distinct()
            .count();

      long total = doctorAppts.size();
      long pending = doctorAppts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
      long completed = doctorAppts.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();

      int completionRate = total > 0 ? (int) ((completed * 100.0) / total) : 0;

      panel.add(createStatCard("Patients Treated", String.valueOf(totalPatients), new Color(227, 242, 253)));
      panel.add(createStatCard("Total Appointments", String.valueOf(total), new Color(232, 245, 233)));
      panel.add(createStatCard("Pending Requests", String.valueOf(pending), new Color(255, 243, 224)));
      panel.add(createStatCard("Completion Rate", completionRate + "%", new Color(243, 229, 245)));

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

   private JPanel createProfessionalInfoCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("Professional Information");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setForeground(new Color(10, 80, 255));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      JPanel grid = new JPanel(new GridLayout(0, 2, 20, 12));
      grid.setBackground(Color.WHITE);

      addInfoRow(grid, "Full Name:", "Dr. " + doctor.getFirstName() + " " + doctor.getLastName());
      addInfoRow(grid, "Specialty:", doctor.getSpecialty());
      addInfoRow(grid, "Qualification:", doctor.getQualification());
      addInfoRow(grid, "Phone:", doctor.getPhone());
      addInfoRow(grid, "Gender:", doctor.getGender());
      addInfoRow(grid, "Schedule:", doctor.getSchedule());
      addInfoRow(grid, "Time Slot:", doctor.getTimeslot());
      addInfoRow(grid, "Status:", doctor.getStatus());
      addInfoRow(grid, "Address:", doctor.getAddress());

      card.add(grid, BorderLayout.CENTER);
      return card;
   }

   private JTable appointmentsTable;

   private JPanel createAppointmentHistoryCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      // Header Panel
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);
      headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

      JLabel title = new JLabel("Appointment History");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setForeground(new Color(10, 80, 255));
      headerPanel.add(title, BorderLayout.WEST);

      // Action Button
      JButton btnViewPrescription = Theme.createGradientButton("View Prescription");
      btnViewPrescription.setPreferredSize(new Dimension(180, 35));
      btnViewPrescription.addActionListener(e -> viewPrescription());

      JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
      btnPanel.setBackground(Color.WHITE);
      btnPanel.add(btnViewPrescription);
      headerPanel.add(btnPanel, BorderLayout.EAST);

      card.add(headerPanel, BorderLayout.NORTH);

      // Get all appointments for this doctor
      List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
      List<AppointmentDetails> doctorAppts = allAppts.stream()
            .filter(a -> a.getDoctorId() == doctorId)
            .sorted((a1, a2) -> {
               int dateCompare = a2.getAppointmentDate().compareTo(a1.getAppointmentDate());
               if (dateCompare != 0)
                  return dateCompare;
               return a2.getAppointmentTime().compareTo(a1.getAppointmentTime());
            })
            .collect(Collectors.toList());

      String[] columns = { "ID", "Date", "Time", "Patient", "Status", "Remarks" };
      DefaultTableModel model = new DefaultTableModel(columns, 0) {
         public boolean isCellEditable(int r, int c) {
            return false;
         }
      };

      for (AppointmentDetails appt : doctorAppts) {
         model.addRow(new Object[] {
               appt.getId(),
               appt.getAppointmentDate(),
               appt.getAppointmentTime(),
               appt.getPatientName(),
               appt.getStatus(),
               appt.getRemarks() != null ? (appt.getRemarks().length() > 30
                     ? appt.getRemarks().substring(0, 30) + "..."
                     : appt.getRemarks()) : ""
         });
      }

      appointmentsTable = new JTable(model);
      Theme.applyTableStyling(appointmentsTable);
      appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      JScrollPane scroll = new JScrollPane(appointmentsTable);
      scroll.setPreferredSize(new Dimension(0, 300));
      card.add(scroll, BorderLayout.CENTER);

      return card;
   }

   private void viewPrescription() {
      int selectedRow = appointmentsTable.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) appointmentsTable.getValueAt(selectedRow, 0);
      AppointmentController controller = new AppointmentController();
      model.DoctorAppointment appt = controller.getAppointmentById(id);

      if (appt != null) {
         if (!"COMPLETED".equalsIgnoreCase(appt.getStatus())) {
            JOptionPane.showMessageDialog(this, "Prescriptions are only available for COMPLETED appointments.");
            return;
         }

         if (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) {
            java.io.File imgFile = new java.io.File(appt.getPrescriptionImagePath());
            if (imgFile.exists()) {
               JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Prescription", true);
               dialog.setSize(600, 800);
               dialog.setLayout(new BorderLayout());

               JLabel imgLabel = new JLabel();
               imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

               ImageIcon icon = new ImageIcon(appt.getPrescriptionImagePath());
               Image img = icon.getImage().getScaledInstance(550, 750, Image.SCALE_SMOOTH);
               imgLabel.setIcon(new ImageIcon(img));

               dialog.add(new JScrollPane(imgLabel), BorderLayout.CENTER);
               dialog.setLocationRelativeTo(this);
               dialog.setVisible(true);
            } else {
               JOptionPane.showMessageDialog(this, "Prescription file not found on disk.");
            }
         } else {
            JOptionPane.showMessageDialog(this, "No prescription uploaded for this appointment.");
         }
      }
   }

   private JPanel createMonthlyStatsCard() {
      JPanel card = createCard();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("Performance Summary");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setForeground(new Color(10, 80, 255));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      // Get statistics
      List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
      List<AppointmentDetails> doctorAppts = allAppts.stream()
            .filter(a -> a.getDoctorId() == doctorId)
            .collect(Collectors.toList());

      long total = doctorAppts.size();
      long pending = doctorAppts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
      long scheduled = doctorAppts.stream().filter(a -> "SCHEDULED".equals(a.getStatus())).count();
      long completed = doctorAppts.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();
      long cancelled = doctorAppts.stream().filter(a -> "CANCELLED".equals(a.getStatus())).count();

      JPanel content = new JPanel();
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      content.setBackground(Color.WHITE);

      addStatRow(content, "Total Appointments:", String.valueOf(total));
      addStatRow(content, "Pending Requests:", String.valueOf(pending));
      addStatRow(content, "Scheduled:", String.valueOf(scheduled));
      addStatRow(content, "Completed:", String.valueOf(completed));
      addStatRow(content, "Cancelled:", String.valueOf(cancelled));

      card.add(content, BorderLayout.CENTER);
      return card;
   }

   private void addStatRow(JPanel parent, String label, String value) {
      JPanel row = new JPanel(new BorderLayout());
      row.setBackground(Color.WHITE);
      row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

      JLabel lblLabel = new JLabel(label);
      lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

      JLabel lblValue = new JLabel(value);
      lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      lblValue.setForeground(new Color(10, 80, 255));

      row.add(lblLabel, BorderLayout.WEST);
      row.add(lblValue, BorderLayout.EAST);

      parent.add(row);
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
      btnBack.addActionListener(e -> dashboard.switchPanel("Doctors"));

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
