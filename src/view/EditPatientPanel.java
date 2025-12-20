package view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import controller.PatientController;
import model.Patient;
import view.style.Theme;

public class EditPatientPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;
    private int patientId;

    // Fields
    private JTextField firstNameTF, lastNameTF;
    private JComboBox<String> genderCB;
    private JSpinner spDOB;
    private JComboBox<String> bloodGroupCB;
    private JTextField phoneTF, emailTF, addressTF;

    // Medical 
    private JTextArea medicalHistoryArea, currentMedicationsArea, allergiesArea;

    public EditPatientPanel() {
        this(null, 0, "", "", "", "", "", "", "", "");
    }


    public EditPatientPanel(Dashboard dashboard, int id, String firstName, String lastName,
            String gender, String dob, String bloodGroup, String phone,
            String email, String address) {

        this.dashboard = dashboard;
        this.patientId = id;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Refetch full patient data to get medical history etc.
        String medicalHistory = "";
        String medications = "";
        String allergies = "";

        if (!java.beans.Beans.isDesignTime()) {
            Patient p = PatientController.getPatientById(id);
            if (p != null) {
                medicalHistory = p.getMedicalHistory();
                medications = p.getMedications();
                allergies = p.getAllergies();
            }
        }

        JPanel contentCard;
        if (java.beans.Beans.isDesignTime()) {
            contentCard = new JPanel();
        } else {
            contentCard = Theme.createCardPanel();
        }
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
        JLabel title;
        if (java.beans.Beans.isDesignTime()) {
            title = new JLabel("Edit Patient Details");
        } else {
            title = Theme.createTitleLabel("Edit Patient Details");
        }
        title.setHorizontalAlignment(SwingConstants.CENTER);
        addItem(contentCard, title, gbc);

        // --- Section: Personal Details ---
        gbc.gridy++;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Personal Details")
                        : createSectionHeader("Personal Details"),
                gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        // Row 1
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 0;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("First Name *") : Theme.createLabel("First Name *"), gbc);
        gbc.gridx = 1;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Last Name *") : Theme.createLabel("Last Name *"),
                gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 0;
        if (java.beans.Beans.isDesignTime()) {
            firstNameTF = new JTextField(20);
        } else {
            firstNameTF = Theme.createStyledTextField(20);
        }
        firstNameTF.setText(firstName);
        addItem(contentCard, firstNameTF, gbc);
        gbc.gridx = 1;
        gbc.gridx = 1;
        if (java.beans.Beans.isDesignTime()) {
            lastNameTF = new JTextField(20);
        } else {
            lastNameTF = Theme.createStyledTextField(20);
        }
        lastNameTF.setText(lastName);
        addItem(contentCard, lastNameTF, gbc);

        // Row 2
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 0;
        addItem(contentCard, java.beans.Beans.isDesignTime() ? new JLabel("Gender *") : Theme.createLabel("Gender *"),
                gbc);
        gbc.gridx = 1;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Date of Birth *") : Theme.createLabel("Date of Birth *"),
                gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        genderCB = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCB.setBackground(Color.WHITE);
        genderCB.setSelectedItem(gender);
        genderCB.setSelectedItem(gender);
        addItem(contentCard, genderCB, gbc);

        gbc.gridx = 1;
        gbc.gridx = 1;
        if (java.beans.Beans.isDesignTime()) {
            spDOB = new JSpinner(new SpinnerDateModel());
            spDOB.setEditor(new JSpinner.DateEditor(spDOB, "yyyy-MM-dd"));
        } else {
            spDOB = Theme.createDateSpinner();
        }
        try {
            if (dob != null && !dob.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                spDOB.setValue(sdf.parse(dob));
            }
        } catch (Exception ignored) {
        }
        addItem(contentCard, spDOB, gbc);

        // Row 3
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 0;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Blood Group") : Theme.createLabel("Blood Group"),
                gbc);
        gbc.gridx = 1;
        addItem(contentCard, java.beans.Beans.isDesignTime() ? new JLabel("Contact Number *")
                : Theme.createLabel("Contact Number *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        bloodGroupCB = new JComboBox<>(new String[] { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" });
        bloodGroupCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bloodGroupCB.setBackground(Color.WHITE);
        bloodGroupCB.setSelectedItem(bloodGroup);
        bloodGroupCB.setSelectedItem(bloodGroup);
        addItem(contentCard, bloodGroupCB, gbc);

        gbc.gridx = 1;
        gbc.gridx = 1;
        if (java.beans.Beans.isDesignTime()) {
            phoneTF = new JTextField(20);
        } else {
            phoneTF = Theme.createStyledTextField(20);
        }
        phoneTF.setText(phone);
        addItem(contentCard, phoneTF, gbc);

        // Row 4
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 0;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Email Address *") : Theme.createLabel("Email Address *"),
                gbc);
        gbc.gridx = 1;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Home Address") : Theme.createLabel("Home Address"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx = 0;
        if (java.beans.Beans.isDesignTime()) {
            emailTF = new JTextField(20);
        } else {
            emailTF = Theme.createStyledTextField(20);
        }
        emailTF.setText(email);
        addItem(contentCard, emailTF, gbc);

        gbc.gridx = 1;
        gbc.gridx = 1;
        if (java.beans.Beans.isDesignTime()) {
            addressTF = new JTextField(20);
        } else {
            addressTF = Theme.createStyledTextField(20);
        }
        addressTF.setText(address);
        addItem(contentCard, addressTF, gbc);

        // --- Section: Medical Info ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridwidth = 2;
        addItem(contentCard, Box.createRigidArea(new Dimension(0, 10)), gbc); // Spacer
        gbc.gridy++;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Medical Information")
                        : createSectionHeader("Medical Information"),
                gbc);
        gbc.gridwidth = 1;

        // Row 5
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx = 0;
        addItem(contentCard,
                java.beans.Beans.isDesignTime() ? new JLabel("Medical History") : Theme.createLabel("Medical History"),
                gbc);
        gbc.gridx = 1;
        addItem(contentCard, java.beans.Beans.isDesignTime() ? new JLabel("Current Medications")
                : Theme.createLabel("Current Medications"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx = 0;
        if (java.beans.Beans.isDesignTime()) {
            medicalHistoryArea = new JTextArea(3, 20);
        } else {
            medicalHistoryArea = Theme.createStyledTextArea(3, 20);
        }
        medicalHistoryArea.setText(medicalHistory);
        addItem(contentCard, new JScrollPane(medicalHistoryArea), gbc);

        gbc.gridx = 1;
        gbc.gridx = 1;
        if (java.beans.Beans.isDesignTime()) {
            currentMedicationsArea = new JTextArea(3, 20);
        } else {
            currentMedicationsArea = Theme.createStyledTextArea(3, 20);
        }
        currentMedicationsArea.setText(medications);
        addItem(contentCard, new JScrollPane(currentMedicationsArea), gbc);

        // Row 6
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridwidth = 2;
        addItem(contentCard, java.beans.Beans.isDesignTime() ? new JLabel("Allergies") : Theme.createLabel("Allergies"),
                gbc);
        gbc.gridy++;
        if (java.beans.Beans.isDesignTime()) {
            allergiesArea = new JTextArea(3, 20);
        } else {
            allergiesArea = Theme.createStyledTextArea(3, 20);
        }
        allergiesArea.setText(allergies);
        addItem(contentCard, new JScrollPane(allergiesArea), gbc);

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

        JButton btnUpdate;
        if (java.beans.Beans.isDesignTime()) {
            btnUpdate = new JButton("Update Patient");
        } else {
            btnUpdate = Theme.createGradientButton("Update Patient");
        }
        btnUpdate.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);
        addItem(contentCard, buttonPanel, gbc);

        // Actions
        btnUpdate.addActionListener(e -> updatePatient());
        btnCancel.addActionListener(e -> {
            if (dashboard != null)
                dashboard.switchPanel("Patients");
        });
    }

    private void updatePatient() {
        if (!validateForm())
            return;

        try {
            Date d = (Date) spDOB.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dobSql = sdf.format(d);

            boolean success = PatientController.updatePatient(
                    patientId,
                    firstNameTF.getText().trim(),
                    lastNameTF.getText().trim(),
                    genderCB.getSelectedItem().toString(),
                    dobSql,
                    bloodGroupCB.getSelectedItem().toString(),
                    phoneTF.getText().trim(),
                    emailTF.getText().trim(),
                    addressTF.getText().trim(),
                    medicalHistoryArea.getText().trim(),
                    currentMedicationsArea.getText().trim(),
                    allergiesArea.getText().trim(),
                    null // Profile picture unchanged here
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Patient updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(this, "✅ Patient updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (dashboard != null)
                    dashboard.switchPanel("Patients");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update patient.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (firstNameTF.getText().trim().isEmpty() || lastNameTF.getText().trim().isEmpty()) {
            showError("First Name and Last Name are required.");
            return false;
        }
        if (phoneTF.getText().trim().isEmpty()) {
            showError("Phone number is required.");
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
        lbl.setForeground(new Color(59, 130, 246)); // Theme Blue
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        lbl.setPreferredSize(new Dimension(200, 30));
        return lbl;
    }

    private void addItem(JPanel p, Component c, GridBagConstraints gbc) {
        p.add(c, (GridBagConstraints) gbc.clone());
    }
}
