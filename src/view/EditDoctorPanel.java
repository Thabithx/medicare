package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.DoctorController;

public class EditDoctorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Dashboard dashboard;
	private int doctorId;
	/**
	 * Create the panel.
	 */
	public EditDoctorPanel(Dashboard dashboard, int id, String firstName, String lastName,
            String gender, String address, String dob, String phone, String specialty, 
            String qualification, String schedule, String timeslot) {

this.dashboard = dashboard;
this.doctorId = id;

setLayout(new BorderLayout());
setBackground(new Color(219, 242, 252));

// === Title ===
JLabel lblTitle = new JLabel("Edit Doctor Details", SwingConstants.CENTER);
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

JLabel lblAddress = new JLabel("Address:");
lblAddress.setBounds(92, 203, 120, 25);
formPanel.add(lblAddress);
JTextField txtAddress = new JTextField(address);
txtAddress.setBounds(280, 203, 214, 25);
formPanel.add(txtAddress);

JLabel lblDOB = new JLabel("DOB (yyyy-mm-dd):");
lblDOB.setBounds(92, 243, 132, 25);
formPanel.add(lblDOB);
JTextField txtDOB = new JTextField(dob);
txtDOB.setBounds(280, 243, 214, 25);
formPanel.add(txtDOB);

JLabel lblPhone = new JLabel("Phone:");
lblPhone.setBounds(92, 283, 120, 25);
formPanel.add(lblPhone);
JTextField txtPhone = new JTextField(phone);
txtPhone.setBounds(280, 283, 214, 25);
formPanel.add(txtPhone);

JLabel lblSpecialty = new JLabel("Specialty:");
lblSpecialty.setBounds(92, 323, 120, 25);
formPanel.add(lblSpecialty);
JTextField txtSpecialty = new JTextField(specialty);
txtSpecialty.setBounds(280, 323, 214, 25);
formPanel.add(txtSpecialty);

JLabel lblQuali = new JLabel("Qualification:");
lblQuali.setBounds(92, 363, 120, 25);
formPanel.add(lblQuali);
JTextField txtQuali = new JTextField(qualification);
txtQuali.setBounds(280, 363, 214, 25);
formPanel.add(txtQuali);

JLabel lblSched = new JLabel("Schedule:");
lblSched.setBounds(92, 403, 120, 25);
formPanel.add(lblSched);
JTextField txtSched = new JTextField(schedule);
txtSched.setBounds(280, 403, 214, 25);
formPanel.add(txtSched);

JLabel lblTimeslot = new JLabel("Timeslot:");
lblTimeslot.setBounds(92, 443, 120, 25);
formPanel.add(lblTimeslot);
JTextField txtTimeslot = new JTextField(timeslot);
txtTimeslot.setBounds(280, 443, 214, 25);
formPanel.add(txtTimeslot);

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
boolean success = DoctorController.updateDoctor(
	doctorId,
    txtFirstName.getText(),
    txtLastName.getText(),
    cmbGender.getSelectedItem().toString(),
    txtAddress.getText(),
    txtDOB.getText(),
    txtPhone.getText(), 
    txtSpecialty.getText(),
    txtQuali.getText(),
    txtSched.getText(),
    txtTimeslot.getText()
    
);

if (success) {
JOptionPane.showMessageDialog(this, "✅ Updated successfully!");
dashboard.switchPanel("Doctors");
} else {
JOptionPane.showMessageDialog(this, "❌ Failed to update!");
}
});

cancelBtn.addActionListener(e -> dashboard.switchPanel("Doctors"));
}


	
}
