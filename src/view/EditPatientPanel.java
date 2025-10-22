package view;

import javax.swing.*;
import controller.PatientController;
import java.awt.*;

public class EditPatientPanel extends JPanel {

    private Dashboard dashboard;
    private int patientId;

    public EditPatientPanel(Dashboard dashboard, int id, String firstName, String lastName,
                            String gender, String dob, String bloodGroup, String phone,
                            String email, String address) {

        this.dashboard = dashboard;
        this.patientId = id;

        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField txtFirstName = new JTextField(firstName);
        JTextField txtLastName = new JTextField(lastName);
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        cmbGender.setSelectedItem(gender);
        JTextField txtDOB = new JTextField(dob);
        JTextField txtBlood = new JTextField(bloodGroup);
        JTextField txtPhone = new JTextField(phone);
        JTextField txtEmail = new JTextField(email);
        JTextField txtAddress = new JTextField(address);

        JButton updateBtn = new JButton("💾 Update");
        JButton cancelBtn = new JButton("Cancel");

        add(new JLabel("First Name:")); add(txtFirstName);
        add(new JLabel("Last Name:")); add(txtLastName);
        add(new JLabel("Gender:")); add(cmbGender);
        add(new JLabel("DOB:")); add(txtDOB);
        add(new JLabel("Blood Group:")); add(txtBlood);
        add(new JLabel("Phone:")); add(txtPhone);
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Address:")); add(txtAddress);
        add(updateBtn); add(cancelBtn);

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
