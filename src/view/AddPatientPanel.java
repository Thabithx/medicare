package view;

import javax.swing.*;
import controller.PatientController;
import java.awt.*;

public class AddPatientPanel extends JPanel {

    private Dashboard dashboard;

    public AddPatientPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField txtDOB = new JTextField();
        JTextField txtBlood = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        add(new JLabel("First Name:")); add(txtFirstName);
        add(new JLabel("Last Name:")); add(txtLastName);
        add(new JLabel("Gender:")); add(cmbGender);
        add(new JLabel("DOB (yyyy-mm-dd):")); add(txtDOB);
        add(new JLabel("Blood Group:")); add(txtBlood);
        add(new JLabel("Phone:")); add(txtPhone);
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Address:")); add(txtAddress);
        add(saveBtn); add(cancelBtn);

        saveBtn.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(this, "❌ Failed to add!");
            }
        });

        cancelBtn.addActionListener(e -> dashboard.switchPanel("Patients"));
    }
}
