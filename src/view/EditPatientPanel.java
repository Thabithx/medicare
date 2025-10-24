package view;

import javax.swing.*;
import controller.PatientController;
import java.awt.*;

public class EditPatientPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;
    private int patientId;

    public EditPatientPanel(Dashboard dashboard, int id, String firstName, String lastName,
                            String gender, String dob, String bloodGroup, String phone,
                            String email, String address) {

        this.dashboard = dashboard;
        this.patientId = id;

        setLayout(new BorderLayout());
        setBackground(new Color(219, 242, 252));

        // === Title ===
        JLabel lblTitle = new JLabel("Edit Patient Details", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // === Form section ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(219, 242, 252));
        add(formPanel, BorderLayout.CENTER);

        // Labels & Fields
        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(92, 83, 120, 25);
        formPanel.add(lblFirstName);
        JTextField txtFirstName = new JTextField(firstName);
        txtFirstName.setBounds(280, 83, 214, 25);
        formPanel.add(txtFirstName);

        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(92, 123, 120, 25);
        formPanel.add(lblLastName);
        JTextField txtLastName = new JTextField(lastName);
        txtLastName.setBounds(280, 123, 214, 25);
        formPanel.add(txtLastName);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(92, 163, 120, 25);
        formPanel.add(lblGender);
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        cmbGender.setSelectedItem(gender);
        cmbGender.setBounds(280, 163, 214, 25);
        formPanel.add(cmbGender);

        JLabel lblDOB = new JLabel("DOB (yyyy-mm-dd):");
        lblDOB.setBounds(92, 203, 132, 25);
        formPanel.add(lblDOB);
        JTextField txtDOB = new JTextField(dob);
        txtDOB.setBounds(280, 203, 214, 25);
        formPanel.add(txtDOB);

        JLabel lblBlood = new JLabel("Blood Group:");
        lblBlood.setBounds(92, 243, 120, 25);
        formPanel.add(lblBlood);
        JTextField txtBlood = new JTextField(bloodGroup);
        txtBlood.setBounds(280, 243, 214, 25);
        formPanel.add(txtBlood);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(92, 283, 120, 25);
        formPanel.add(lblPhone);
        JTextField txtPhone = new JTextField(phone);
        txtPhone.setBounds(280, 283, 214, 25);
        formPanel.add(txtPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(92, 323, 120, 25);
        formPanel.add(lblEmail);
        JTextField txtEmail = new JTextField(email);
        txtEmail.setBounds(280, 323, 214, 25);
        formPanel.add(txtEmail);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(92, 363, 120, 25);
        formPanel.add(lblAddress);
        JTextField txtAddress = new JTextField(address);
        txtAddress.setBounds(280, 363, 214, 25);
        formPanel.add(txtAddress);

        // === Buttons section ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(192, 211, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton updateBtn = new JButton("💾 Update");
        JButton cancelBtn = new JButton("❌ Cancel");
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        // === Button actions ===
        updateBtn.addActionListener(e -> {
            boolean success = PatientController.updatePatient(
                    patientId,
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    cmbGender.getSelectedItem().toString(),
                    txtDOB.getText(),
                    txtBlood.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    "", "", ""
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Updated successfully!");
                dashboard.switchPanel("Patients");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update!");
            }
        });

        cancelBtn.addActionListener(e -> dashboard.switchPanel("Patients"));
    }
}
