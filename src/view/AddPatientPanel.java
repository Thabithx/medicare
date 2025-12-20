package view;

import javax.swing.*;
import controller.PatientController;
import java.awt.*;
import view.style.Theme;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class AddPatientPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;

    // Styled input components
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JComboBox<String> cmbGender;
    private JSpinner spDOB;
    private JTextField txtBlood;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextArea txtAllergies;
    private JTextArea txtDiseases;
    private JPasswordField txtPassword;

    private JButton btnSave;
    private JButton btnCancel;

    public AddPatientPanel() {
        this(null);
    }

    public AddPatientPanel(Dashboard dashboard) {
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
        JLabel title = Theme.createTitleLabel("Add New Patient");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentCard.add(title, gbc);

        // --- Section: Personal Details ---
        gbc.gridy++;
        contentCard.add(createSectionHeader("Personal Details"), gbc);

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

        // Row 3 (Contact)
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Phone Number *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Email Address *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        txtPhone = Theme.createStyledTextField(20);
        contentCard.add(txtPhone, gbc);
        gbc.gridx = 1;
        txtEmail = Theme.createStyledTextField(20);
        contentCard.add(txtEmail, gbc);

        // Row 4 (Address Full Width)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Theme.createLabel("Home Address"), gbc);

        gbc.gridy++;
        txtAddress = Theme.createStyledTextField(40);
        contentCard.add(txtAddress, gbc);
        gbc.gridwidth = 1;

        // --- Section: Medical Info ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        gbc.gridy++;
        contentCard.add(createSectionHeader("Medical Information"), gbc);
        gbc.gridwidth = 1;

        // Row 5
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Blood Group"), gbc);
        gbc.gridx = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        txtBlood = Theme.createStyledTextField(10);
        contentCard.add(txtBlood, gbc);

        // Row 6 (Allergies & Diseases)
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Known Allergies"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Chronic Diseases / History"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        txtAllergies = Theme.createStyledTextArea(3, 20);
        contentCard.add(new JScrollPane(txtAllergies), gbc);
        gbc.gridx = 1;
        txtDiseases = Theme.createStyledTextArea(3, 20);
        contentCard.add(new JScrollPane(txtDiseases), gbc);

        // --- Section: Security ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        gbc.gridy++;
        contentCard.add(createSectionHeader("Account Security"), gbc);
        gbc.gridwidth = 1;

        // Row 7 (Password)
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Set Password *"), gbc);

        gbc.gridy++;
        txtPassword = Theme.createStyledPasswordField(20);
        contentCard.add(txtPassword, gbc);

        // --- Buttons ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setForeground(new Color(107, 114, 128));
        btnCancel.setBorderPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSave = Theme.createGradientButton("Save & Register");
        btnSave.setPreferredSize(new Dimension(180, 40));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        contentCard.add(buttonPanel, gbc);

        // Actions
        btnSave.addActionListener(e -> savePatient());
        btnCancel.addActionListener(e -> {
            if (dashboard != null)
                dashboard.switchPanel("Patients");
        });
    }

    private void savePatient() {
        if (!validateForm())
            return;

        try {
            Date d = (Date) spDOB.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dobSql = sdf.format(d);
            String pass = new String(txtPassword.getPassword());

            boolean success = PatientController.addPatient(
                    txtFirstName.getText().trim(),
                    txtLastName.getText().trim(),
                    cmbGender.getSelectedItem().toString(),
                    dobSql,
                    txtBlood.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    "", // medicalHistory
                    txtDiseases.getText().trim(),
                    txtAllergies.getText().trim(),
                    pass);

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Patient registered successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                resetForm();
                dashboard.switchPanel("Patients");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to register patient.\nEmail might already be in use.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
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

        if (txtPhone.getText().trim().isEmpty()) {
            showError("Phone number is required.");
            return false;
        }

        if (!txtPhone.getText().matches("\\d{10}")) { // Simple 10 digit check
            showError("Phone number must be exactly 10 digits.");
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            showError("Email address is required.");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.compile(emailRegex).matcher(txtEmail.getText().trim()).matches()) {
            showError("Please enter a valid email address.");
            return false;
        }

        if (new String(txtPassword.getPassword()).isEmpty()) {
            showError("Password is required for account creation.");
            return false;
        }

        return true;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void resetForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtBlood.setText("");
        txtAllergies.setText("");
        txtDiseases.setText("");
        txtPassword.setText("");
        spDOB.setValue(new Date());
    }

    private JLabel createSectionHeader(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(59, 130, 246)); // Theme Blue
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        lbl.setPreferredSize(new Dimension(200, 30));
        return lbl;
    }
}
