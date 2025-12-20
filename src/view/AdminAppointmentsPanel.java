package view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import controller.AppointmentController;
import model.AppointmentDetails;
import view.style.Theme;

public class AdminAppointmentsPanel extends JPanel {

   private JTable table;
   private DefaultTableModel model;
   private TableRowSorter<DefaultTableModel> sorter;
   private JTextField searchField;
   private AppointmentController controller;

   public AdminAppointmentsPanel() {
      controller = new AppointmentController();
      setLayout(new BorderLayout(0, 20));
      setBackground(Color.WHITE);
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Header
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);

      JLabel titleLabel = new JLabel("All Appointments");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
      headerPanel.add(titleLabel, BorderLayout.WEST);

      // Search Bar
      JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      searchPanel.setBackground(Color.WHITE);
      searchField = Theme.createPlaceholderTextField(20, "Search by Patient/Doctor Name...");
      searchField.setPreferredSize(new Dimension(300, 35));
      JButton btnSearch = Theme.createGradientButton("Search");
      btnSearch.setPreferredSize(new Dimension(100, 35));

      btnSearch.addActionListener(e -> filter(searchField.getText()));

      searchPanel.add(new JLabel("Search (Patient/Doctor): "));
      searchPanel.add(searchField);
      searchPanel.add(btnSearch);
      headerPanel.add(searchPanel, BorderLayout.EAST);

      add(headerPanel, BorderLayout.NORTH);

      // Table
      String[] columns = { "ID", "Patient", "Doctor", "Date", "Time", "Status", "Prescription", "Remarks" };
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

      JButton btnEdit = Theme.createGradientButton("Edit");
      btnEdit.setPreferredSize(new Dimension(140, 35));

      JButton btnCancel = Theme.createButton("Cancel", Theme.DANGER);
      btnCancel.setPreferredSize(new Dimension(140, 35));

      JButton btnDelete = Theme.createButton("Delete", Color.DARK_GRAY);
      btnDelete.setPreferredSize(new Dimension(140, 35));

      JButton btnView = Theme.createGradientButton("View Prescription");
      btnView.setPreferredSize(new Dimension(180, 35));

      btnEdit.addActionListener(e -> editAppointment());
      btnCancel.addActionListener(e -> updateStatus("CANCELLED"));
      btnDelete.addActionListener(e -> deleteAppointment());
      btnView.addActionListener(e -> viewPrescription());

      actionsPanel.add(btnEdit);
      actionsPanel.add(btnCancel);
      actionsPanel.add(btnDelete);
      actionsPanel.add(btnView);
      add(actionsPanel, BorderLayout.SOUTH);

      loadAppointments();
   }

   public void loadAppointments() {
      model.setRowCount(0);
      List<AppointmentDetails> list = AppointmentController.getAllAppointmentDetails();

      for (AppointmentDetails appt : list) {
         model.addRow(new Object[] {
               appt.getId(),
               appt.getPatientName(),
               appt.getDoctorName(),
               appt.getAppointmentDate(),
               appt.getAppointmentTime(),
               appt.getStatus(),
               (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) ? "Yes" : "No",
               appt.getRemarks()
         });
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

   private void editAppointment() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }
      int id = (int) table.getValueAt(selectedRow, 0);
      model.DoctorAppointment appt = controller.getAppointmentById(id);
      if (appt != null) {
         Window parentWindow = SwingUtilities.getWindowAncestor(this);
         FormAppointment form = new FormAppointment(parentWindow, appt, controller);
         form.setVisible(true);
         loadAppointments();
      }
   }

   private void deleteAppointment() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
         if (controller.deleteAppointment(id)) {
            JOptionPane.showMessageDialog(this, "Appointment deleted.");
            loadAppointments();
         } else {
            JOptionPane.showMessageDialog(this, "Failed to delete.");
         }
      }
   }

   private void viewPrescription() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      String status = (String) table.getValueAt(selectedRow, 5); // Status Column
      if (!"COMPLETED".equalsIgnoreCase(status)) {
         JOptionPane.showMessageDialog(this, "Prescriptions are only available for COMPLETED appointments.");
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
}
