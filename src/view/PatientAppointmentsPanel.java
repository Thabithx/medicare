package view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import controller.AppointmentController;
import model.AppointmentDetails;
import service.SessionManager;
import view.style.Theme;

public class PatientAppointmentsPanel extends JPanel {

   private JTable table;
   private DefaultTableModel model;
   private TableRowSorter<DefaultTableModel> sorter;
   private JTextField searchField;
   private AppointmentController controller;

   public PatientAppointmentsPanel() {
      controller = new AppointmentController();
      setLayout(new BorderLayout(0, 20));
      setBackground(Color.WHITE);
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Header
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);

      JLabel titleLabel = new JLabel("My Appointments");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
      headerPanel.add(titleLabel, BorderLayout.WEST);

      // Search Bar
      JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      searchPanel.setBackground(Color.WHITE);
      searchField = Theme.createStyledTextField(20);
      searchField.setPreferredSize(new Dimension(250, 35));
      JButton btnSearch = Theme.createGradientButton("Search");
      btnSearch.setPreferredSize(new Dimension(100, 35));

      btnSearch.addActionListener(e -> filter(searchField.getText()));

      searchPanel.add(new JLabel("Search Doctor: "));
      searchPanel.add(searchField);
      searchPanel.add(btnSearch);
      headerPanel.add(searchPanel, BorderLayout.EAST);

      add(headerPanel, BorderLayout.NORTH);

      // Table
      String[] columns = { "ID", "Doctor", "Date", "Time", "Status", "Prescription", "Remarks" };
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

      JButton btnNewAppointment = Theme.createGradientButton("Book Appointment");
      btnNewAppointment.setPreferredSize(new Dimension(180, 35)); // Give it enough width
      btnNewAppointment.addActionListener(e -> {
         // Fix: Get proper parent window
         Window parentWindow = SwingUtilities.getWindowAncestor(this);
         FormAppointment dialog = new FormAppointment(parentWindow, null, controller);
         dialog.setVisible(true); // Blocks until dialog closes
         // Refresh appointments table after dialog closes
         loadAppointments();
      });

      JButton btnViewPrescription = Theme.createGradientButton("View Prescription");
      btnViewPrescription.setPreferredSize(new Dimension(180, 35));
      btnViewPrescription.addActionListener(e -> viewPrescription());

      actionsPanel.add(btnNewAppointment);
      actionsPanel.add(btnViewPrescription);
      add(actionsPanel, BorderLayout.SOUTH);

      loadAppointments();
   }

   public void loadAppointments() {
      model.setRowCount(0);
      List<AppointmentDetails> list = AppointmentController.getAllAppointmentDetails();
      int currentUserId = SessionManager.getUser().getReferenceId(); // Patient ID

      for (AppointmentDetails appt : list) {
         // Filter only for this patient
         if (appt.getPatientId() == currentUserId) {
            model.addRow(new Object[] {
                  appt.getId(),
                  appt.getDoctorName(),
                  appt.getAppointmentDate(),
                  appt.getAppointmentTime(),
                  appt.getStatus(),
                  (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) ? "View"
                        : "None",
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

   private void viewPrescription() {
      int selectedRow = table.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) table.getValueAt(selectedRow, 0);
      AppointmentController controller = new AppointmentController();
      model.DoctorAppointment appt = controller.getAppointmentById(id);

      if (appt != null) {
         if (!"COMPLETED".equalsIgnoreCase(appt.getStatus())) {
            JOptionPane.showMessageDialog(this, "Prescriptions are only available for COMPLETED appointments.");
            return;
         }

         if (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) {
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
}
