package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import controller.AppointmentController;
import model.AppointmentDetails;
import service.SessionManager;
import view.style.Theme;

public class DoctorAppointmentsPanel extends JPanel {

   private JTable table;
   private DefaultTableModel model;
   private TableRowSorter<DefaultTableModel> sorter;
   private JTextField searchField;
   private AppointmentController controller;

   private Dashboard dashboard;

   public DoctorAppointmentsPanel(Dashboard dashboard) {
      this.dashboard = dashboard;
      controller = new AppointmentController();
      setLayout(new BorderLayout(0, 20));
      setBackground(Color.WHITE);
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Header
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);

      JLabel titleLabel = new JLabel("My Schedule");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
      headerPanel.add(titleLabel, BorderLayout.WEST);

      // Search Bar
      JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      searchPanel.setBackground(Color.WHITE);
      searchField = Theme.createPlaceholderTextField(20, "Search by Patient Name...");
      searchField.setPreferredSize(new Dimension(250, 35));
      JButton btnSearch = Theme.createGradientButton("Search");
      btnSearch.setPreferredSize(new Dimension(100, 35));

      btnSearch.addActionListener(e -> filter(searchField.getText()));

      searchPanel.add(new JLabel("Search Patient: "));
      searchPanel.add(searchField);
      searchPanel.add(btnSearch);
      headerPanel.add(searchPanel, BorderLayout.EAST);

      add(headerPanel, BorderLayout.NORTH);

      // Table
      String[] columns = { "ID", "Patient", "Date", "Time", "Status", "Prescription", "Remarks" };
      model = new DefaultTableModel(columns, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };

      table = new JTable(model);
      table.setRowHeight(30);
      table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
      table.getTableHeader().setBackground(new Color(240, 240, 240));
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      sorter = new TableRowSorter<>(model);
      table.setRowSorter(sorter);

      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
      add(scrollPane, BorderLayout.CENTER);

      // Actions Panel
      JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      actionsPanel.setBackground(Color.WHITE);

      JButton btnAccept = Theme.createGradientButton("Accept");
      btnAccept.setPreferredSize(new Dimension(160, 35));

      JButton btnDecline = Theme.createButton("Decline", Theme.DANGER);
      btnDecline.setPreferredSize(new Dimension(160, 35));

      JButton btnComplete = Theme.createGradientButton("Complete");
      btnComplete.setPreferredSize(new Dimension(160, 35));

      JButton btnProfile = Theme.createGradientButton("View Patient Profile");
      btnProfile.setPreferredSize(new Dimension(180, 35));

      JButton btnUpload = Theme.createGradientButton("Upload Prescription");
      btnUpload.setPreferredSize(new Dimension(180, 35));

      JButton btnView = Theme.createGradientButton("View Prescription");
      btnView.setPreferredSize(new Dimension(180, 35));

      btnAccept.addActionListener(e -> updateStatus("ACCEPTED"));
      btnDecline.addActionListener(e -> updateStatus("DECLINED"));
      btnComplete.addActionListener(e -> updateStatus("COMPLETED"));
      btnProfile.addActionListener(e -> viewPatientProfile());
      btnUpload.addActionListener(e -> attemptUploadPrescription());
      btnView.addActionListener(e -> viewPrescription());

      actionsPanel.add(btnAccept);
      actionsPanel.add(btnDecline);
      actionsPanel.add(btnComplete);
      actionsPanel.add(btnProfile);
      actionsPanel.add(btnUpload);
      actionsPanel.add(btnView);
      add(actionsPanel, BorderLayout.SOUTH);

      loadAppointments();
   }

   public void loadAppointments() {
      model.setRowCount(0);
      List<AppointmentDetails> list = AppointmentController.getAllAppointmentDetails();
      int currentUserId = SessionManager.getUser().getReferenceId(); // Doctor ID

      for (AppointmentDetails appt : list) {
         if (appt.getDoctorId() == currentUserId) {
            model.addRow(new Object[] {
                  appt.getId(),
                  appt.getPatientName(),
                  appt.getAppointmentDate(),
                  appt.getAppointmentTime(),
                  appt.getStatus(),
                  (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) ? "Yes"
                        : "No",
                  appt.getRemarks()
            });
         }
      }
   }

   private void filter(String query) {
      if (query.trim().length() == 0) {
         sorter.setRowFilter(null);
      } else {
         sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
      }
   }

   private void updateStatus(String status) {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      if (controller.updateStatus(id, status)) {
         JOptionPane.showMessageDialog(this, "Status updated to " + status);
         loadAppointments();
      } else {
         JOptionPane.showMessageDialog(this, "Failed to update status.");
      }
   }

   private void attemptUploadPrescription() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      String status = (String) table.getValueAt(selectedRow, 4); // Status Column

      if (!"COMPLETED".equalsIgnoreCase(status)) {
         JOptionPane.showMessageDialog(this, "⚠️ You must complete the appointment before uploading a prescription.");
         return;
      }

      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select Prescription Image");
      int result = fileChooser.showOpenDialog(this);

      if (result == JFileChooser.APPROVE_OPTION) {
         File selectedFile = fileChooser.getSelectedFile();

         // Define destination
         File destDir = new File("src/resources/prescriptions");
         if (!destDir.exists())
            destDir.mkdirs();

         String fileName = "presc_" + id + "_" + System.currentTimeMillis() + ".jpg";
         File destFile = new File(destDir, fileName);

         try {
            Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Revert back to uploadPrescription of controller (it sets status to COMPLETED
            // which is redundant but fine)
            if (controller.uploadPrescription(id, destFile.getAbsolutePath())) {
               JOptionPane.showMessageDialog(this, "Prescription uploaded successfully!");
               loadAppointments();
            } else {
               JOptionPane.showMessageDialog(this, "Failed to update database.");
            }
         } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
         }
      }
   }

   private void viewPrescription() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      model.DoctorAppointment appt = controller.getAppointmentById(id);

      if (appt != null && appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) {
         File imgFile = new File(appt.getPrescriptionImagePath());
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

   private void viewPatientProfile() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      model.DoctorAppointment appt = controller.getAppointmentById(id);

      if (appt != null) {
         int patientId = appt.getPatientId();
         dashboard.showPatientDetails(patientId);
      } else {
         JOptionPane.showMessageDialog(this, "Could not load patient ID.");
      }
   }
}
