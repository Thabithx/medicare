package view;

import javax.swing.*;
import java.awt.*;
import view.style.Theme;
import controller.DoctorController;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AddDoctorPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JComboBox<String> cmbGender;
    private JTextField txtAddress;
    private JSpinner spDOB;
    private JTextField txtPhone;
    private JComboBox<String> cmbSpecialty;
    private JComboBox<String> cmbQualification;
    private JComboBox<String> cmbSchedule;
    private JComboBox<String> cmbTimeslot;

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public AddDoctorPanel() {
        this(null);
    }

    public AddDoctorPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentCard = Theme.createCardPanel();
        contentCard.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(contentCard);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // --- Header ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        JLabel title = Theme.createTitleLabel("Add New Doctor");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentCard.add(title, gbc);

        // --- Section: Personal Info ---
        gbc.gridy++;
        contentCard.add(createSectionHeader("Personal Information"), gbc);
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        // Row 1
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("First Name *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Last Name *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        txtFirstName = Theme.createStyledTextField(20);
        contentCard.add(txtFirstName, gbc);
        gbc.gridx = 1;
        txtLastName = Theme.createStyledTextField(20);
        contentCard.add(txtLastName, gbc);

        // Row 2
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Gender *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Date of Birth *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        cmbGender = new JComboBox<>(new String[] { "Male", "Female" });
        cmbGender.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbGender.setBackground(Color.WHITE);
        contentCard.add(cmbGender, gbc);

        gbc.gridx = 1;
        spDOB = Theme.createDateSpinner();
        contentCard.add(spDOB, gbc);

        // Row 3
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Phone Number *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Address"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        txtPhone = Theme.createStyledTextField(20);
        contentCard.add(txtPhone, gbc);
        gbc.gridx = 1;
        txtAddress = Theme.createStyledTextField(20);
        contentCard.add(txtAddress, gbc);

        // --- Section: Professional Info ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        gbc.gridy++;
        contentCard.add(createSectionHeader("Professional Details"), gbc);
        gbc.gridwidth = 1;

        // Row 4
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Specialty *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Qualification *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        cmbSpecialty = new JComboBox<>(new String[] {
                "General Physician", "Cardiologist", "Neurologist", "Pediatrician", "Orthopedic", "Dermatologist"
        });
        cmbSpecialty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSpecialty.setBackground(Color.WHITE);
        contentCard.add(cmbSpecialty, gbc);

        gbc.gridx = 1;
        cmbQualification = new JComboBox<>(new String[] {
                "MBBS", "MD", "DO", "BMBS", "MBChB"
        });
        cmbQualification.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbQualification.setBackground(Color.WHITE);
        contentCard.add(cmbQualification, gbc);

        // Row 5
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Available Schedule"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Time Slot"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        cmbSchedule = new JComboBox<>(new String[] {
                "Mon-Fri", "Sat-Sun", "Weekdays", "Weekends", "Daily"
        });
        cmbSchedule.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSchedule.setBackground(Color.WHITE);
        contentCard.add(cmbSchedule, gbc);

        gbc.gridx = 1;
        cmbTimeslot = new JComboBox<>(new String[] {
                "09:00 - 12:00", "13:00 - 16:00", "17:00 - 20:00", "08:00 - 16:00"
        });
        cmbTimeslot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTimeslot.setBackground(Color.WHITE);
        contentCard.add(cmbTimeslot, gbc);

        // --- Section: Security Details ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        gbc.gridy++;
        contentCard.add(createSectionHeader("Login Credentials"), gbc);
        gbc.gridwidth = 1;

        // Row 6
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Email Address *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Password *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        txtEmail = Theme.createStyledTextField(20);
        contentCard.add(txtEmail, gbc);

        gbc.gridx = 1;
        txtPassword = Theme.createStyledPasswordField(20);
        contentCard.add(txtPassword, gbc);

        // --- Buttons ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setForeground(new Color(107, 114, 128));
        btnCancel.setBorderPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnSave = Theme.createGradientButton("Save & Register");
        btnSave.setPreferredSize(new Dimension(180, 40));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        contentCard.add(buttonPanel, gbc);

        // Actions
        btnSave.addActionListener(e -> saveDoctor());
        btnCancel.addActionListener(e -> {
            if (dashboard != null)
                dashboard.switchPanel("Doctors");
        });
    }

    private void saveDoctor() {
        if (!validateForm())
            return;

        try {
            Date d = (Date) spDOB.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dobSql = sdf.format(d);

            String pass = new String(txtPassword.getPassword());

            boolean success = DoctorController.addDoctor(
                    txtFirstName.getText().trim(),
                    txtLastName.getText().trim(),
                    cmbGender.getSelectedItem().toString(),
                    txtAddress.getText().trim(),
                    dobSql,
                    txtPhone.getText().trim(),
                    cmbSpecialty.getSelectedItem().toString(),
                    cmbQualification.getSelectedItem().toString(),
                    cmbSchedule.getSelectedItem().toString(),
                    cmbTimeslot.getSelectedItem().toString(),
                    txtEmail.getText().trim(),
                    pass);

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Doctor added and account created!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (dashboard != null)
                    dashboard.switchPanel("Doctors");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add doctor (Email may be taken).", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (txtFirstName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty()) {
            showError("First Name and Last Name are required.");
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            showError("Email is required for login.");
            return false;
        }

        if (new String(txtPassword.getPassword()).isEmpty()) {
            showError("Password is required.");
            return false;
        }

        if (txtPhone.getText().trim().isEmpty()) {
            showError("Phone number is required.");
            return false;
        }

        // Simple numeric check for phone
        if (!txtPhone.getText().matches("\\d+")) {
            showError("Phone number must contain only digits.");
            return false;
        }

        return true;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private JLabel createSectionHeader(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(59, 130, 246)); 
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        lbl.setPreferredSize(new Dimension(200, 30));
        return lbl;
    }
}
