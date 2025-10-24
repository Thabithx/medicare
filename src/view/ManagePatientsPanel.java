package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.PatientController;
import java.awt.*;
import java.sql.*;

public class ManagePatientsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable patientTable;
    private Dashboard dashboard;
    private DefaultTableModel model;

    public ManagePatientsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout()); // 🔥 makes it fill parent panel automatically

        // === Table section ===
        patientTable = new JTable();
        model = new DefaultTableModel(new String[]{
                "ID", "First Name", "Last Name", "Gender", "DOB",
                "Blood Group", "Phone", "Email", "Address"
        }, 0);
        patientTable.setModel(model);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER); // fills the main area

        // === Button panel (bottom) ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(192, 211, 255));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton btnRefresh = new JButton("🔄 Refresh");
        JButton btnEdit = new JButton("✏️ Edit Selected");
        JButton btnAdd = new JButton("➕ Add New Patient");
        JButton btnDelete = new JButton("🗑️ Delete Selected");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        // === Button logic ===
        btnRefresh.addActionListener(e -> loadPatients());
        btnEdit.addActionListener(e -> editSelectedPatient());
        btnAdd.addActionListener(e -> addPatient());
        btnDelete.addActionListener(e -> deleteSelectedPatient());

        loadPatients(); // initial load
    }

    private void loadPatients() {
        model.setRowCount(0);
        ResultSet rs = PatientController.getAllPatients();
        try {
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getDate("d_o_b"),
                        rs.getString("blood_group"),
                        rs.getString("phone_num"),
                        rs.getString("email"),
                        rs.getString("address")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patients!");
        }
    }

    private void editSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String firstName = model.getValueAt(selectedRow, 1).toString();
        String lastName = model.getValueAt(selectedRow, 2).toString();
        String gender = model.getValueAt(selectedRow, 3).toString();
        String dob = model.getValueAt(selectedRow, 4).toString();
        String blood = model.getValueAt(selectedRow, 5).toString();
        String phone = model.getValueAt(selectedRow, 6).toString();
        String email = model.getValueAt(selectedRow, 7).toString();
        String address = model.getValueAt(selectedRow, 8).toString();

        // Open the EditPatientPanel with this data
        dashboard.showEditPatientPanel(id, firstName, lastName, gender, dob, blood, phone, email, address);
    }
    private void addPatient() {
        // This is where you handle the "Add" button logic.
        // For now, let's just open the AddPatient form through the Dashboard.
        dashboard.switchPanel("AddPatient");
    }

    

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = PatientController.deletePatient(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                loadPatients();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient!");
            }
        }
    }
}
