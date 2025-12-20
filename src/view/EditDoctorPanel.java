package view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import controller.DoctorController;
import view.style.Theme;

public class EditDoctorPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;
    private int doctorId;

    private JTextField firstNameTF, lastNameTF, addressTF, phoneTF;
    private JComboBox<String> genderCB, specCB, qualiCB, scheduleCB, timeslotCB;
    private JSpinner spDOB;

    public EditDoctorPanel() {
        this(null, 0, "", "", "", "", "", "", "", "", "", "");
    }

    public EditDoctorPanel(Dashboard dashboard, int id, String firstName, String lastName,
            String gender, String address, String dob, String phone, String specialty,
            String qualification, String schedule, String timeslot) {

        this.dashboard = dashboard;
        this.doctorId = id;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Use a Card Wrapper
        JPanel contentCard = Theme.createCardPanel();
        contentCard.setLayout(new GridBagLayout());

        // Scroll Pane
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
        JLabel title = Theme.createTitleLabel("Edit Doctor Details");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentCard.add(title, gbc);

        // --- Section: Personal Details ---
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
        firstNameTF = Theme.createStyledTextField(20);
        firstNameTF.setText(firstName);
        contentCard.add(firstNameTF, gbc);
        gbc.gridx = 1;
        lastNameTF = Theme.createStyledTextField(20);
        lastNameTF.setText(lastName);
        contentCard.add(lastNameTF, gbc);

        // Row 2
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Gender *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Date of Birth *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        genderCB = new JComboBox<>(new String[] { "Male", "Female" });
        genderCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCB.setBackground(Color.WHITE);
        genderCB.setSelectedItem(gender);
        contentCard.add(genderCB, gbc);

        gbc.gridx = 1;
        spDOB = Theme.createDateSpinner();
        try {
            if (dob != null && !dob.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                spDOB.setValue(sdf.parse(dob));
            }
        } catch (Exception ignored) {
        }
        contentCard.add(spDOB, gbc);

        // Row 3
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Contact Number *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Address"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        phoneTF = Theme.createStyledTextField(20);
        phoneTF.setText(phone);
        contentCard.add(phoneTF, gbc);

        gbc.gridx = 1;
        addressTF = Theme.createStyledTextField(20);
        addressTF.setText(address);
        contentCard.add(addressTF, gbc);

        // --- Section: Professional Details ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentCard.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        gbc.gridy++;
        contentCard.add(createSectionHeader("Professional Information"), gbc);
        gbc.gridwidth = 1;

        // Row 4
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Specialization *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Qualification *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        specCB = new JComboBox<>(new String[] {
                "Pediatrician", "Gynecologist", "Cardiologist",
                "Dermatologist", "Neurologist", "Ophthalmologist", "General Physician"
        });
        specCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        specCB.setBackground(Color.WHITE);
        specCB.setSelectedItem(specialty);
        // If specialty isn't in list, add it
        if (specCB.getSelectedItem() == null || !specCB.getSelectedItem().equals(specialty)) {
            specCB.addItem(specialty);
            specCB.setSelectedItem(specialty);
        }
        contentCard.add(specCB, gbc);

        gbc.gridx = 1;
        qualiCB = new JComboBox<>(new String[] {
                "MBBS", "MD", "MS", "BDS", "MDS", "DGO", "DCH", "DNB"
        });
        qualiCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        qualiCB.setBackground(Color.WHITE);
        qualiCB.setSelectedItem(qualification);
        // If isn't in list, add it
        if (qualiCB.getSelectedItem() == null || !qualiCB.getSelectedItem().equals(qualification)) {
            qualiCB.addItem(qualification);
            qualiCB.setSelectedItem(qualification);
        }
        contentCard.add(qualiCB, gbc);

        // Row 5 (Schedule)
        gbc.gridy++;
        gbc.gridx = 0;
        contentCard.add(Theme.createLabel("Available Days *"), gbc);
        gbc.gridx = 1;
        contentCard.add(Theme.createLabel("Time Slot *"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        scheduleCB = new JComboBox<>(new String[] {
                "Weekdays", "Weekend", "Full Week", "Mon-Wed-Fri", "Tue-Thu-Sat"
        });
        scheduleCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scheduleCB.setBackground(Color.WHITE);
        scheduleCB.setSelectedItem(schedule);
        if (scheduleCB.getSelectedItem() == null) {
            scheduleCB.addItem(schedule);
            scheduleCB.setSelectedItem(schedule);
        }
        contentCard.add(scheduleCB, gbc);

        gbc.gridx = 1;
        timeslotCB = new JComboBox<>(new String[] {
                "08:00 AM – 12:00 PM",
                "12:00 PM – 04:00 PM",
                "04:00 PM – 08:00 PM",
                "09:00 AM – 05:00 PM"
        });
        timeslotCB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeslotCB.setBackground(Color.WHITE);
        timeslotCB.setSelectedItem(timeslot);
        if (timeslotCB.getSelectedItem() == null) {
            timeslotCB.addItem(timeslot);
            timeslotCB.setSelectedItem(timeslot);
        }
        contentCard.add(timeslotCB, gbc);

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

        JButton btnUpdate = Theme.createGradientButton("Update Doctor");
        btnUpdate.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);
        contentCard.add(buttonPanel, gbc);

        // Actions
        btnUpdate.addActionListener(e -> updateDoctor());
        btnCancel.addActionListener(e -> {
            if (dashboard != null)
                dashboard.switchPanel("Doctors");
        });
    }

    private void updateDoctor() {
        if (!validateForm())
            return;

        try {
            Date d = (Date) spDOB.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dobSql = sdf.format(d);

            boolean success = DoctorController.updateDoctor(
                    doctorId,
                    firstNameTF.getText().trim(),
                    lastNameTF.getText().trim(),
                    genderCB.getSelectedItem().toString(),
                    addressTF.getText().trim(),
                    dobSql,
                    phoneTF.getText().trim(),
                    specCB.getSelectedItem().toString(),
                    qualiCB.getSelectedItem().toString(),
                    scheduleCB.getSelectedItem().toString(),
                    timeslotCB.getSelectedItem().toString());

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Doctor updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(this, "✅ Doctor updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (dashboard != null)
                    dashboard.switchPanel("Doctors");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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

        // Simple numeric check for phone
        if (!phoneTF.getText().matches("\\d+")) {
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
        lbl.setForeground(new Color(59, 130, 246)); // Theme Blue
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        lbl.setPreferredSize(new Dimension(200, 30));
        return lbl;
    }
}
