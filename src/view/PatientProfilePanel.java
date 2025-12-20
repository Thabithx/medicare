package view;

import controller.PatientController;
import model.Patient;
import utils.ImageUploadHelper;
import view.style.Theme;
import service.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import controller.AppointmentController;
import model.AppointmentDetails;

public class PatientProfilePanel extends JPanel {

   private static final long serialVersionUID = 1L;

   private JLabel lblProfilePic;
   private JLabel lblEmail;
   private JTextField txtFirstName, txtLastName, txtPhone, txtAddress;
   private JTextField txtBloodGroup;
   private JSpinner spDOB;
   private JComboBox<String> cmbGender;
   private JTextArea txtMedicalHistory, txtMedications, txtAllergies;
   private JButton btnSave, btnEdit, btnUploadPic;

   private Patient currentPatient;
   private String profilePicturePath;

   public PatientProfilePanel() {
      setLayout(new BorderLayout(0, 0));
      setBackground(Theme.BG_COLOR);
      setBorder(Theme.createPadding(30));

      // -- Header with Gradient --
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Theme.BG_COLOR);
      headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

      JLabel title = Theme.createTitleLabel("My Profile");
      title.setFont(new Font("Segoe UI", Font.BOLD, 28));
      headerPanel.add(title, BorderLayout.WEST);

      add(headerPanel, BorderLayout.NORTH);

      // -- Main Content Panel --
      JPanel mainPanel = new JPanel(new BorderLayout(30, 0));
      mainPanel.setBackground(Theme.BG_COLOR);

      // Left: Profile Picture Section
      JPanel leftCard = Theme.createCardPanel();
      leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
      leftCard.setPreferredSize(new Dimension(250, 0));

      // Profile Picture
      lblProfilePic = new JLabel();
      lblProfilePic.setAlignmentX(Component.CENTER_ALIGNMENT);
      lblProfilePic.setBorder(null);
      updateProfilePicture(null);

      // Upload Button
      btnUploadPic = Theme.createButton("Upload Picture", Theme.INFO);
      btnUploadPic.setAlignmentX(Component.CENTER_ALIGNMENT);
      btnUploadPic.setMaximumSize(new Dimension(180, 40));
      btnUploadPic.addActionListener(e -> uploadProfilePicture());

      leftCard.add(Box.createVerticalGlue());
      leftCard.add(lblProfilePic);
      leftCard.add(Box.createRigidArea(new Dimension(0, 20)));
      leftCard.add(btnUploadPic);
      leftCard.add(Box.createVerticalGlue());

      // Right: Form Section (Scrollable Card)
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

      formContent.add(createSectionHeader("Personal Information"));
      formContent.add(Box.createRigidArea(new Dimension(0, 15)));

      JPanel personalForm = new JPanel(new GridBagLayout());
      personalForm.setBackground(Color.WHITE);
      personalForm.setAlignmentX(Component.LEFT_ALIGNMENT);

      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(10, 10, 10, 10);
      g.fill = GridBagConstraints.HORIZONTAL;
      g.anchor = GridBagConstraints.WEST;
      int row = 0;

      addFormField(personalForm, g, row++, "First Name:", txtFirstName = createStyledTextField(15),
            "Last Name:", txtLastName = createStyledTextField(15));

      lblEmail = new JLabel("-");
      lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      addFormField(personalForm, g, row++, "Email:", lblEmail,
            "Phone:", txtPhone = createStyledTextField(15));

      cmbGender = new JComboBox<>(new String[] { "Male", "Female", "Other" });
      cmbGender.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      cmbGender.setBackground(Color.WHITE);

      spDOB = new JSpinner(new SpinnerDateModel());
      spDOB.setEditor(new JSpinner.DateEditor(spDOB, "yyyy-MM-dd"));
      spDOB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      addFormField(personalForm, g, row++, "Gender:", cmbGender, "Date of Birth:", spDOB);

      addFormField(personalForm, g, row++, "Blood Group:", txtBloodGroup = createStyledTextField(10),
            "Address:", txtAddress = createStyledTextField(20));

      formContent.add(personalForm);
      formContent.add(Box.createRigidArea(new Dimension(0, 30)));

      formContent.add(createSectionHeader("Medical Information"));
      formContent.add(Box.createRigidArea(new Dimension(0, 15)));

      JPanel medicalForm = new JPanel(new GridBagLayout());
      medicalForm.setBackground(Color.WHITE);
      medicalForm.setAlignmentX(Component.LEFT_ALIGNMENT);

      row = 0;
      g.gridx = 0;
      g.gridy = row;
      g.weightx = 0;
      g.gridwidth = 4;
      medicalForm.add(Theme.createLabel("Medical History"), g);
      row++;
      g.gridy = row;
      txtMedicalHistory = Theme.createStyledTextArea(3, 20);
      medicalForm.add(new JScrollPane(txtMedicalHistory), g);
      row++;

      g.gridy = row;
      medicalForm.add(Box.createRigidArea(new Dimension(0, 10)), g);
      row++;
      g.gridy = row;
      medicalForm.add(Theme.createLabel("Current Medications"), g);
      row++;
      g.gridy = row;
      txtMedications = Theme.createStyledTextArea(3, 20);
      medicalForm.add(new JScrollPane(txtMedications), g);
      row++;

      g.gridy = row;
      medicalForm.add(Box.createRigidArea(new Dimension(0, 10)), g);
      row++;
      g.gridy = row;
      medicalForm.add(Theme.createLabel("Allergies"), g);
      row++;
      g.gridy = row;
      txtAllergies = Theme.createStyledTextArea(3, 20);
      medicalForm.add(new JScrollPane(txtAllergies), g);

      formContent.add(medicalForm);

      mainPanel.add(leftCard, BorderLayout.WEST);
      mainPanel.add(rightCardWrapper, BorderLayout.CENTER);

      // -- Appointment History Section --
      JPanel historyPanel = createAppointmentHistoryCard();

      // Combine mainPanel and historyPanel vertically
      JPanel contentWrapper = new JPanel();
      contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
      contentWrapper.setBackground(Theme.BG_COLOR);
      contentWrapper.add(mainPanel);
      contentWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
      contentWrapper.add(historyPanel);

      add(contentWrapper, BorderLayout.CENTER);

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
   }

   private JLabel createSectionHeader(String text) {
      JLabel lbl = new JLabel(text);
      lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
      lbl.setForeground(Theme.PRIMARY);
      lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
      return lbl;
   }

   private JTextField createStyledTextField(int columns) {
      return Theme.createStyledTextField(columns);
   }

   private void addFormField(JPanel form, GridBagConstraints g, int row,
         String label1, Component field1,
         String label2, Component field2) {
      g.gridx = 0;
      g.gridy = row;
      g.weightx = 0.15;
      g.gridwidth = 1;
      JLabel lbl1 = new JLabel(label1);
      lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));
      form.add(lbl1, g);

      g.gridx = 1;
      g.weightx = 0.35;
      form.add(field1, g);

      g.gridx = 2;
      g.weightx = 0.15;
      JLabel lbl2 = new JLabel(label2);
      lbl2.setFont(new Font("Segoe UI", Font.BOLD, 14));
      form.add(lbl2, g);

      g.gridx = 3;
      g.weightx = 0.35;
      form.add(field2, g);
   }

   private void toggleEdit(boolean enable) {
      txtFirstName.setEditable(enable);
      txtLastName.setEditable(enable);
      txtPhone.setEditable(enable);
      txtBloodGroup.setEditable(enable);
      txtAddress.setEditable(enable);

      txtMedicalHistory.setEditable(enable);
      txtMedications.setEditable(enable);
      txtAllergies.setEditable(enable);

      cmbGender.setEnabled(enable);
      spDOB.setEnabled(enable);
      btnUploadPic.setEnabled(enable);

      btnSave.setEnabled(enable);
      btnEdit.setEnabled(!enable);
   }

   public void loadProfile() {
      if (!SessionManager.isPatient())
         return;

      int patientId = SessionManager.getUser().getReferenceId();

      // Refresh data
      currentPatient = PatientController.getPatientById(patientId);

      if (currentPatient != null) {
         txtFirstName.setText(currentPatient.getFirstName());
         txtLastName.setText(currentPatient.getLastName());
         lblEmail.setText(currentPatient.getEmail());
         txtPhone.setText(currentPatient.getPhone());
         txtBloodGroup.setText(currentPatient.getBloodGroup());
         txtAddress.setText(currentPatient.getAddress());

         txtMedicalHistory.setText(currentPatient.getMedicalHistory());
         txtMedications.setText(currentPatient.getCurrentMedications());
         txtAllergies.setText(currentPatient.getAllergies());

         cmbGender.setSelectedItem(currentPatient.getGender());
         if (currentPatient.getDob() != null) {
            spDOB.setValue(currentPatient.getDob());
         }

         // Load profile picture
         profilePicturePath = currentPatient.getProfilePicturePath();
         updateProfilePicture(profilePicturePath);

         // Load Appointment History
         loadAppointmentHistory();
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
      ImageIcon icon = ImageUploadHelper.loadProfileImage(imagePath, false, 150, 150);
      ImageIcon circular = ImageUploadHelper.createCircularImage(icon, 150);
      lblProfilePic.setIcon(circular);
   }

   private JTable historyTable;

   public void loadAppointmentHistory() {
      if (currentPatient == null || historyTable == null)
         return;

      DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
      model.setRowCount(0);

      java.util.List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
      java.util.List<AppointmentDetails> myAppts = allAppts.stream()
            .filter(a -> a.getPatientId() == currentPatient.getId())
            .sorted((a1, a2) -> {
               int dateCompare = a2.getAppointmentDate().compareTo(a1.getAppointmentDate());
               if (dateCompare != 0)
                  return dateCompare;
               return a2.getAppointmentTime().compareTo(a1.getAppointmentTime());
            })
            .collect(Collectors.toList());

      for (AppointmentDetails appt : myAppts) {
         model.addRow(new Object[] {
               appt.getId(),
               appt.getAppointmentDate(),
               appt.getAppointmentTime(),
               appt.getDoctorName(),
               appt.getStatus(),
               (appt.getPrescriptionImagePath() != null && !appt.getPrescriptionImagePath().isEmpty()) ? "Yes" : "No",
               appt.getRemarks()
         });
      }
   }

   private JPanel createAppointmentHistoryCard() {
      JPanel card = Theme.createCardPanel();
      card.setLayout(new BorderLayout());

      JLabel title = new JLabel("My Appointment History");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
      card.add(title, BorderLayout.NORTH);

      String[] columns = { "ID", "Date", "Time", "Doctor", "Status", "Prescription", "Remarks" };
      DefaultTableModel model = new DefaultTableModel(columns, 0) {
         public boolean isCellEditable(int r, int c) {
            return false;
         }
      };

      historyTable = new JTable(model);
      Theme.applyTableStyling(historyTable);

      JScrollPane scroll = new JScrollPane(historyTable);
      scroll.setPreferredSize(new Dimension(0, 250));
      card.add(scroll, BorderLayout.CENTER);

      JButton btnViewPrescription = Theme.createGradientButton("View Prescription");
      btnViewPrescription.setPreferredSize(new Dimension(180, 35));
      btnViewPrescription.addActionListener(e -> {
         int row = historyTable.getSelectedRow();
         if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment.");
            return;
         }
         int id = Integer.parseInt(historyTable.getValueAt(row, 0).toString());
         AppointmentDetails sel = AppointmentController.getAllAppointmentDetails().stream().filter(a -> a.getId() == id)
               .findFirst().orElse(null);

         if (sel != null && sel.getPrescriptionImagePath() != null && !sel.getPrescriptionImagePath().isEmpty()) {
            if ("COMPLETED".equalsIgnoreCase(sel.getStatus())) {
               viewPrescriptionImage(sel.getPrescriptionImagePath());
            } else {
               JOptionPane.showMessageDialog(this, "Prescription available only for COMPLETED appointments.");
            }
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

   private void saveProfile() {
      if (currentPatient == null)
         return;

      try {
         String fName = txtFirstName.getText().trim();
         String lName = txtLastName.getText().trim();
         String phone = txtPhone.getText().trim();
         String addr = txtAddress.getText().trim();
         String bg = txtBloodGroup.getText().trim();
         String gender = cmbGender.getSelectedItem().toString();

         String medHist = txtMedicalHistory.getText().trim();
         String meds = txtMedications.getText().trim();
         String alg = txtAllergies.getText().trim();

         java.util.Date utilDate = (java.util.Date) spDOB.getValue();
         String dobSql = new SimpleDateFormat("yyyy-MM-dd").format(utilDate);

         if (fName.isEmpty() || lName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
         }

         boolean success = PatientController.updatePatient(
               currentPatient.getId(),
               fName, lName, gender, dobSql, bg, phone,
               currentPatient.getEmail(),
               addr,
               medHist,
               meds,
               alg,
               profilePicturePath);

         if (success) {
            JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            toggleEdit(false);
            loadProfile();
         } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.");
         }
      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
      }
   }
}
