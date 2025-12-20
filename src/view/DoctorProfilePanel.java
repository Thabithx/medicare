package view;

import controller.DoctorController;
import model.Doctor;
import utils.ImageUploadHelper;
import view.style.Theme;
import service.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DoctorProfilePanel extends JPanel {

   private static final long serialVersionUID = 1L;

   private JLabel lblProfilePic;
   private JTextField txtFirstName, txtLastName, txtPhone, txtAddress, txtQualification;
   private JComboBox<String> cmbGender, cmbSpecialty;
   private JComboBox<String> cmbSchedule, cmbTimeslot; // New Fields
   private JButton btnSave, btnEdit, btnUploadPic;

   private Doctor currentDoctor;
   private String profilePicturePath;

   public DoctorProfilePanel() {
      setLayout(new BorderLayout(0, 0));
      setBackground(Theme.BG_COLOR);
      setBorder(Theme.createPadding(30));

      // -- Header --
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Theme.BG_COLOR);
      headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

      JLabel title = Theme.createTitleLabel("Doctor Profile");
      title.setFont(new Font("Segoe UI", Font.BOLD, 28));
      headerPanel.add(title, BorderLayout.WEST);

      add(headerPanel, BorderLayout.NORTH);

      // -- Main Content --
      JPanel mainPanel = new JPanel(new BorderLayout(30, 0));
      mainPanel.setBackground(Theme.BG_COLOR);

      // Left: Card
      JPanel leftCard = Theme.createCardPanel();
      leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
      leftCard.setPreferredSize(new Dimension(250, 0));

      lblProfilePic = new JLabel();
      lblProfilePic.setAlignmentX(Component.CENTER_ALIGNMENT);
      // Removed border frame as requested
      lblProfilePic.setBorder(null);
      updateProfilePicture(null);

      btnUploadPic = Theme.createButton("Upload Picture", Theme.INFO);
      btnUploadPic.setAlignmentX(Component.CENTER_ALIGNMENT);
      btnUploadPic.setMaximumSize(new Dimension(180, 40));
      btnUploadPic.addActionListener(e -> uploadProfilePicture());

      leftCard.add(Box.createVerticalGlue());
      leftCard.add(lblProfilePic);
      leftCard.add(Box.createRigidArea(new Dimension(0, 20)));
      leftCard.add(btnUploadPic);
      leftCard.add(Box.createVerticalGlue());

      // Right: Form
      JPanel rightCardWrapper = Theme.createCardPanel();
      rightCardWrapper.setLayout(new BorderLayout());

      JPanel formContent = new JPanel();
      formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
      formContent.setBackground(Color.WHITE);
      formContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      JScrollPane scrollPane = new JScrollPane(formContent);
      scrollPane.setBorder(null);
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      rightCardWrapper.add(scrollPane, BorderLayout.CENTER);

      // 1. Personal Info
      formContent.add(createSectionHeader("Professional Information"));
      formContent.add(Box.createRigidArea(new Dimension(0, 15)));

      JPanel personalForm = new JPanel(new GridBagLayout());
      personalForm.setBackground(Color.WHITE);
      personalForm.setAlignmentX(Component.LEFT_ALIGNMENT);

      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(10, 10, 10, 10);
      g.fill = GridBagConstraints.HORIZONTAL;
      g.anchor = GridBagConstraints.WEST;
      int row = 0;

      addFormField(personalForm, g, row++, "First Name:", txtFirstName = Theme.createStyledTextField(15),
            "Last Name:", txtLastName = Theme.createStyledTextField(15));

      cmbGender = new JComboBox<>(new String[] { "Male", "Female", "Other" });
      cmbGender.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      cmbGender.setBackground(Color.WHITE);

      cmbSpecialty = new JComboBox<>(new String[] {
            "Cardiology", "Orthopedics", "Neurology", "Pediatrics", "General Medicine",
            "Dermatology", "Psychiatry", "Radiology", "Surgery"
      });
      cmbSpecialty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      cmbSpecialty.setBackground(Color.WHITE);

      addFormField(personalForm, g, row++, "Gender:", cmbGender, "Specialty:", cmbSpecialty);

      addFormField(personalForm, g, row++, "Qualification:", txtQualification = Theme.createStyledTextField(15),
            "Phone:", txtPhone = Theme.createStyledTextField(15));

      // Address
      g.gridx = 0;
      g.gridy = row;
      g.weightx = 0.2;
      g.gridwidth = 1;
      JLabel lblAddr = new JLabel("Address:");
      lblAddr.setFont(new Font("Segoe UI", Font.BOLD, 14));
      personalForm.add(lblAddr, g);

      g.gridx = 1;
      g.gridwidth = 3;
      g.weightx = 0.8;
      txtAddress = Theme.createStyledTextField(30);
      personalForm.add(txtAddress, g);

      formContent.add(personalForm);
      formContent.add(Box.createRigidArea(new Dimension(0, 30)));

      // 2. Schedule Info
      formContent.add(createSectionHeader("Availability & Schedule"));
      formContent.add(Box.createRigidArea(new Dimension(0, 15)));

      JPanel scheduleForm = new JPanel(new GridBagLayout());
      scheduleForm.setBackground(Color.WHITE);
      scheduleForm.setAlignmentX(Component.LEFT_ALIGNMENT);
      row = 0;
      g.gridwidth = 1;

      cmbSchedule = new JComboBox<>(new String[] {
            "Weekdays", "Weekend", "Full Week", "Mon-Wed-Fri", "Tue-Thu-Sat"
      });
      cmbSchedule.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      cmbSchedule.setBackground(Color.WHITE);

      cmbTimeslot = new JComboBox<>(new String[] {
            "08:00 AM – 12:00 PM", "12:00 PM – 04:00 PM", "04:00 PM – 08:00 PM", "09:00 AM – 05:00 PM"
      });
      cmbTimeslot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      cmbTimeslot.setBackground(Color.WHITE);

      addFormField(scheduleForm, g, row++, "Available Days:", cmbSchedule, "Time Slot:", cmbTimeslot);

      formContent.add(scheduleForm);

      mainPanel.add(leftCard, BorderLayout.WEST);
      mainPanel.add(rightCardWrapper, BorderLayout.CENTER);

      add(mainPanel, BorderLayout.CENTER);

      // -- Footer --
      JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
      footer.setBackground(Theme.BG_COLOR);
      footer.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

      btnEdit = Theme.createButton("Enable Editing", Theme.INFO);
      btnEdit.setPreferredSize(new Dimension(140, 40));

      btnSave = Theme.createGradientButton("Save Changes");
      btnSave.setPreferredSize(new Dimension(160, 40));
      btnSave.setEnabled(false);

      footer.add(btnEdit);
      footer.add(btnSave);
      add(footer, BorderLayout.SOUTH);

      // -- Actions --
      btnEdit.addActionListener(e -> toggleEdit(true));
      btnSave.addActionListener(e -> saveProfile());

      toggleEdit(false);

      // Fix: Add Appointment History Section to doctor profile for viewing
      // functionality
      formContent.add(Box.createRigidArea(new Dimension(0, 30)));
      formContent.add(createAppointmentHistorySection());
   }

   private JTable appointmentsTable;

   private JPanel createAppointmentHistorySection() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(Color.WHITE);

      JLabel title = createSectionHeader("My Appointment History");
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      panel.add(title, BorderLayout.NORTH);

      String[] columns = { "ID", "Date", "Time", "Patient", "Status", "Prescription" };
      javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };

      appointmentsTable = new JTable(model);
      Theme.applyTableStyling(appointmentsTable);
      appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      JScrollPane scroll = new JScrollPane(appointmentsTable);
      scroll.setPreferredSize(new Dimension(0, 200));
      panel.add(scroll, BorderLayout.CENTER);

      JButton btnViewPrescription = Theme.createGradientButton("View Prescription");
      btnViewPrescription.setPreferredSize(new Dimension(180, 35));
      btnViewPrescription.addActionListener(e -> viewPrescription());

      JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      btnPanel.setBackground(Color.WHITE);
      btnPanel.add(btnViewPrescription);
      panel.add(btnPanel, BorderLayout.SOUTH);

      return panel;
   }

   public void loadAppointmentHistory() {
      if (currentDoctor == null || appointmentsTable == null)
         return;

      javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) appointmentsTable.getModel();
      model.setRowCount(0);

      List<model.AppointmentDetails> allAppts = controller.AppointmentController.getAllAppointmentDetails();
      for (model.AppointmentDetails appt : allAppts) {
         if (appt.getDoctorId() == currentDoctor.getId()) {
            boolean hasPresc = (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty());
            model.addRow(new Object[] {
                  appt.getId(),
                  appt.getAppointmentDate(),
                  appt.getAppointmentTime(),
                  appt.getPatientName(),
                  appt.getStatus(),
                  hasPresc ? "Yes" : "No"
            });
         }
      }
   }

   private void viewPrescription() {
      int selectedRow = appointmentsTable.getSelectedRow();
      if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select an appointment first.");
         return;
      }

      int id = (int) appointmentsTable.getValueAt(selectedRow, 0);
      controller.AppointmentController controller = new controller.AppointmentController();
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

   private JLabel createSectionHeader(String text) {
      JLabel lbl = new JLabel(text);
      lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
      lbl.setForeground(new Color(10, 80, 255));
      lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
      return lbl;
   }

   private void addFormField(JPanel form, GridBagConstraints g, int row,
         String label1, Component field1,
         String label2, Component field2) {
      g.gridx = 0;
      g.gridy = row;
      g.weightx = 0.2;
      g.gridwidth = 1;
      JLabel lbl1 = new JLabel(label1);
      lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));
      form.add(lbl1, g);

      g.gridx = 1;
      g.weightx = 0.3;
      form.add(field1, g);

      g.gridx = 2;
      g.weightx = 0.2;
      JLabel lbl2 = new JLabel(label2);
      lbl2.setFont(new Font("Segoe UI", Font.BOLD, 14));
      form.add(lbl2, g);

      g.gridx = 3;
      g.weightx = 0.3;
      form.add(field2, g);
   }

   private void toggleEdit(boolean enable) {
      txtFirstName.setEditable(enable);
      txtLastName.setEditable(enable);
      txtPhone.setEditable(enable);
      txtQualification.setEditable(enable);
      txtAddress.setEditable(enable);

      cmbGender.setEnabled(enable);
      cmbSpecialty.setEnabled(enable);
      cmbSchedule.setEnabled(enable);
      cmbTimeslot.setEnabled(enable);

      btnUploadPic.setEnabled(enable);

      btnSave.setEnabled(enable);
      btnEdit.setEnabled(!enable);
   }

   public void loadProfile() {
      if (!SessionManager.isDoctor())
         return;

      int doctorId = SessionManager.getUser().getReferenceId();

      List<Doctor> list = DoctorController.getAllDoctors();
      currentDoctor = list.stream().filter(d -> d.getId() == doctorId).findFirst().orElse(null);

      if (currentDoctor != null) {
         txtFirstName.setText(currentDoctor.getFirstName());
         txtLastName.setText(currentDoctor.getLastName());
         txtPhone.setText(currentDoctor.getPhone());
         txtQualification.setText(currentDoctor.getQualification());
         txtAddress.setText(currentDoctor.getAddress());
         cmbGender.setSelectedItem(currentDoctor.getGender());
         cmbSpecialty.setSelectedItem(currentDoctor.getSpecialty());

         // Select or Add Schedule/Timeslot if custom
         setComboValue(cmbSchedule, currentDoctor.getSchedule());
         setComboValue(cmbTimeslot, currentDoctor.getTimeslot());

         // Load profile picture
         profilePicturePath = currentDoctor.getProfilePicturePath();
         updateProfilePicture(profilePicturePath);

         // Load history
         loadAppointmentHistory();
      }
   }

   private void setComboValue(JComboBox<String> cmb, String value) {
      if (value == null)
         return;
      cmb.setSelectedItem(value);
      if (!value.equals(cmb.getSelectedItem())) {
         cmb.addItem(value);
         cmb.setSelectedItem(value);
      }
   }

   private void uploadProfilePicture() {
      String selectedPath = ImageUploadHelper.selectProfilePicture();
      if (selectedPath != null) {
         profilePicturePath = selectedPath;
         updateProfilePicture(profilePicturePath);
         JOptionPane.showMessageDialog(this, "Picture uploaded! Click 'Save Changes' to confirm.");
      }
   }

   private void updateProfilePicture(String imagePath) {
      ImageIcon icon = ImageUploadHelper.loadProfileImage(imagePath, true, 150, 150);
      ImageIcon circular = ImageUploadHelper.createCircularImage(icon, 150);
      lblProfilePic.setIcon(circular);
   }

   private void saveProfile() {
      if (currentDoctor == null) {
         JOptionPane.showMessageDialog(this, "No doctor profile loaded.");
         return;
      }

      try {
         String fName = txtFirstName.getText().trim();
         String lName = txtLastName.getText().trim();
         String phone = txtPhone.getText().trim();
         String addr = txtAddress.getText().trim();
         String qual = txtQualification.getText().trim();
         String gender = cmbGender.getSelectedItem().toString();
         String specialty = cmbSpecialty.getSelectedItem().toString();
         String schedule = cmbSchedule.getSelectedItem().toString();
         String timeslot = cmbTimeslot.getSelectedItem().toString();

         if (fName.isEmpty() || lName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
         }

         String sql = "UPDATE Doctors SET first_name = ?, last_name = ?, gender = ?, address = ?, phone = ?, specialty = ?, qualification = ?, schedule = ?, timeslot = ?, profile_picture_path = ? WHERE doctor_id = ?";

         try (java.sql.Connection con = db.Connectdb.getConnection();
               java.sql.PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, fName);
            pst.setString(2, lName);
            pst.setString(3, gender);
            pst.setString(4, addr);
            pst.setString(5, phone);
            pst.setString(6, specialty);
            pst.setString(7, qual);
            pst.setString(8, schedule);
            pst.setString(9, timeslot);
            pst.setString(10, profilePicturePath);
            pst.setInt(11, currentDoctor.getId());

            int updated = pst.executeUpdate();

            if (updated > 0) {
               JOptionPane.showMessageDialog(this, "✅ Profile Updated Successfully!");
               toggleEdit(false);
               loadProfile();
            } else {
               JOptionPane.showMessageDialog(this, "❌ Failed to update profile.");
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
      }
   }
}
