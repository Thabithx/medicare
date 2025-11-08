package view;

import javax.swing.*;
import java.awt.*;
import controller.DoctorController;

public class AddDoctorPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;

    // Form fields
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JComboBox<String> genderCB;
    private JTextField addressTF;
    private JTextField dobTF;
    private JTextField phoneTF;
    private JComboBox<String> specCB;
    private JComboBox<String> qualiCB;
    private JComboBox<String> scheduleCB;
    private JComboBox<String> timeslotCB;

    public AddDoctorPanel(Dashboard dashboard) {
        this.dashboard = dashboard;

        setLayout(new GridBagLayout()); 
        setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        // Title
        JLabel title = new JLabel("Add New Doctor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(46, 99, 125));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(title, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Utility method
        java.util.function.BiConsumer<String, JComponent> addRow = (labelText, comp) -> {
            JLabel label = new JLabel(labelText);
            label.setFont(labelFont);
            comp.setFont(fieldFont);
            gbc.gridx = 0;
            formPanel.add(label, gbc);
            gbc.gridx = 1;
            formPanel.add(comp, gbc);
            gbc.gridy++;
        };

        firstNameTF = new JTextField(15);
        lastNameTF = new JTextField(15);
        genderCB = new JComboBox<>(new String[]{"Male", "Female"});
        addressTF = new JTextField(15);
        dobTF = new JTextField(15);
        phoneTF = new JTextField(15);
        specCB = new JComboBox<>(new String[]{
            "Pediatrician", "Gynecologist", "Cardiologist",
            "Dermatologist", "Neurologist", "Ophthalmologist"
        });
        qualiCB = new JComboBox<>(new String[]{
            "MBBS", "MD", "MS", "BDS", "MDS", "DGO", "DCH", "DNB"
        });
        scheduleCB = new JComboBox<>(new String[]{
            "Weekdays", "Weekend", "Full Week"
        });
        timeslotCB = new JComboBox<>(new String[]{
            "08:00 AM – 08:30 AM", "08:30 AM – 9:00 AM",
            "12:00 PM – 12:30 PM", "12:30 PM – 01:00 PM",
            "06:30 PM – 07:30 PM"
        });

        addRow.accept("First Name", firstNameTF);
        addRow.accept("Last Name", lastNameTF);
        addRow.accept("Gender", genderCB);
        addRow.accept("Address", addressTF);
        addRow.accept("Date of Birth (yyyy-mm-dd)", dobTF);
        addRow.accept("Telephone", phoneTF);
        addRow.accept("Specialty", specCB);
        addRow.accept("Qualification", qualiCB);
        addRow.accept("Schedule", scheduleCB);
        addRow.accept("Timeslot", timeslotCB);

        // Buttons
        JButton btnSave = new JButton("Add Doctor");
        JButton btnCancel = new JButton("Cancel");

        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnSave.setBackground(new Color(46, 125, 175));
        btnSave.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(220, 220, 220));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel); // ✅ Centered in AddDoctorPanel

        // === Button Actions ===
        btnSave.addActionListener(e -> {
            boolean success = DoctorController.addDoctor(
                    firstNameTF.getText(),
                    lastNameTF.getText(),
                    genderCB.getSelectedItem().toString(),
                    addressTF.getText(),
                    dobTF.getText(),
                    phoneTF.getText(),
                    specCB.getSelectedItem().toString(),
                    qualiCB.getSelectedItem().toString(),
                    scheduleCB.getSelectedItem().toString(),
                    timeslotCB.getSelectedItem().toString()
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Doctor added successfully!");
                if (dashboard != null) dashboard.switchPanel("Doctors");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add doctor!");
            }
        });

        btnCancel.addActionListener(e -> {
            if (dashboard != null) dashboard.switchPanel("Doctors");
        });
    }


}
