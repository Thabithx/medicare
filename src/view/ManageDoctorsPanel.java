package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.DoctorController;

public class ManageDoctorsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
    private JTable doctorTable;
    private Dashboard dashboard;
    private DefaultTableModel model;

	/**
	 * Create the panel.
	 */
	public ManageDoctorsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout()); // 🔥 makes it fill parent panel automatically

        // === Table section ===
        doctorTable = new JTable();
        model = new DefaultTableModel(new String[]{
                "ID", "First Name", "Last Name", "Gender", "Address", "DOB",
                "Phone", "Specialty", "Qualification", "Schedule", "Timeslot"
        }, 0);
        doctorTable.setModel(model);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        add(scrollPane, BorderLayout.CENTER); // fills the main area

        // === Button panel (bottom) ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(192, 211, 255));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton btnRefresh = new JButton("🔄 Refresh");
        JButton btnEdit = new JButton("✏️ Edit Selected");
        JButton btnAdd = new JButton("➕ Add New Doctor");
        JButton btnDelete = new JButton("🗑️ Delete Selected");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        // === Button logic ===
        btnRefresh.addActionListener(e -> loadDoctors());
        btnEdit.addActionListener(e -> editSelectedDoctor());
        btnAdd.addActionListener(e -> addDoctor());
        btnDelete.addActionListener(e -> deleteSelectedDoctor());

        loadDoctors(); // initial load
    }

    private void loadDoctors() {
        model.setRowCount(0);
        ResultSet rs = DoctorController.getAllDoctors();
        try {
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getDate("dob"),
                        rs.getString("phone"),
                        rs.getString("specialty"),
                        rs.getString("qualification"),                       
                        rs.getString("schedule"),
                        rs.getString("timeslot")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors!");
        }
    }

    private void editSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String firstName = model.getValueAt(selectedRow, 1).toString();
        String lastName = model.getValueAt(selectedRow, 2).toString();
        String gender = model.getValueAt(selectedRow, 3).toString();
        String address = model.getValueAt(selectedRow, 8).toString();
        String dob = model.getValueAt(selectedRow, 4).toString();
        String phone = model.getValueAt(selectedRow, 6).toString(); 
        String specialty = model.getValueAt(selectedRow, 5).toString();
        String qualification = model.getValueAt(selectedRow, 7).toString();
        String schedule = model.getValueAt(selectedRow, 7).toString();
        String timeslot = model.getValueAt(selectedRow, 7).toString();
        

        // Open the EditDoctorPanel with this data
        dashboard.showEditDoctorPanel(id, firstName, lastName, gender,  address, dob, phone, specialty, qualification, schedule, timeslot);
    }
    private void addDoctor() {
        // This is where you handle the "Add" button logic.
        // For now, let's just open the AddDoctor form through the Dashboard.
        dashboard.switchPanel("AddDoctor");
    }

    

    private void deleteSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this doctor?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DoctorController.deleteDoctor(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
                loadDoctors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor!");
            }
        }
    }
    
}
