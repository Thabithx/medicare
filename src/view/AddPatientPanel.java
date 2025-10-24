package view;

import javax.swing.*;
import controller.PatientController;
import java.awt.*;

public class AddPatientPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;

    // Form fields
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JComboBox<String> cmbGender;
    private JTextField txtDOB;
    private JTextField txtBlood;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;

    private JButton btnSave;
    private JButton btnCancel;

    public AddPatientPanel(Dashboard dashboard) {
        this.dashboard = dashboard;

        setBackground(new Color(219, 242, 252));
        setLayout(new BorderLayout()); // fills parent panel automatically

        // === Form container ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(219, 242, 252));
        add(formPanel, BorderLayout.CENTER);

        // Labels and fields
        JLabel lblTitle = new JLabel("Add New Patient");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(180, 20, 250, 40);
        formPanel.add(lblTitle);

        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(92, 83, 120, 25);
        formPanel.add(lblFirstName);

        txtFirstName = new JTextField();
        txtFirstName.setBounds(280, 83, 214, 25);
        formPanel.add(txtFirstName);

        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(92, 123, 120, 25);
        formPanel.add(lblLastName);

        txtLastName = new JTextField();
        txtLastName.setBounds(280, 123, 214, 25);
        formPanel.add(txtLastName);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(92, 163, 120, 25);
        formPanel.add(lblGender);

        cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        cmbGender.setBounds(280, 163, 214, 25);
        formPanel.add(cmbGender);

        JLabel lblDOB = new JLabel("DOB (yyyy-mm-dd):");
        lblDOB.setBounds(92, 203, 132, 25);
        formPanel.add(lblDOB);

        txtDOB = new JTextField();
        txtDOB.setBounds(280, 202, 214, 25);
        formPanel.add(txtDOB);

        JLabel lblBlood = new JLabel("Blood Group:");
        lblBlood.setBounds(92, 243, 120, 25);
        formPanel.add(lblBlood);

        txtBlood = new JTextField();
        txtBlood.setBounds(280, 243, 214, 25);
        formPanel.add(txtBlood);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(92, 283, 120, 25);
        formPanel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(280, 283, 214, 25);
        formPanel.add(txtPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(92, 323, 120, 25);
        formPanel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(280, 323, 214, 25);
        formPanel.add(txtEmail);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(92, 363, 120, 25);
        formPanel.add(lblAddress);

        txtAddress = new JTextField();
        txtAddress.setBounds(280, 363, 214, 25);
        formPanel.add(txtAddress);

        // === Button panel ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(192, 211, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave = new JButton("💾 Save");
        btnCancel = new JButton("❌ Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        // === Button actions ===
        btnSave.addActionListener(e -> {
            boolean success = PatientController.addPatient(
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    cmbGender.getSelectedItem().toString(),
                    txtDOB.getText(),
                    txtBlood.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Added Successfully!");
                dashboard.switchPanel("Patients");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add patient!");
            }
        });

        btnCancel.addActionListener(e -> dashboard.switchPanel("Patients"));
    }
}
