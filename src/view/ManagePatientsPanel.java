package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.PatientController;
import java.awt.*;
import java.sql.*;

public class ManagePatientsPanel extends JPanel {

    private Dashboard dashboard;
    private JTable patientTable;
    private DefaultTableModel model;

    public ManagePatientsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        model = new DefaultTableModel(new String[]{
                "ID", "First Name", "Last Name", "Gender", "DOB",
                "Blood Group", "Phone", "Email", "Address"
        }, 0);

       
        patientTable = new JTable(model);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton deleteBtn = new JButton("🗑️ Delete Selected");
        JButton editBtn = new JButton("✏️ Edit Selected");
        JButton addBtn = new JButton("➕ Add New Patient");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.add(refreshBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(editBtn);
        btnPanel.add(addBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadPatients());
        deleteBtn.addActionListener(e -> deleteSelectedPatient());
        editBtn.addActionListener(e -> editSelectedPatient());
        addBtn.addActionListener(e -> dashboard.switchPanel("AddPatient"));

        loadPatients();
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
            e.printStackTrace();
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

        dashboard.showEditPatientPanel(
                id,
                (String) model.getValueAt(selectedRow, 1),
                (String) model.getValueAt(selectedRow, 2),
                (String) model.getValueAt(selectedRow, 3),
                model.getValueAt(selectedRow, 4).toString(),
                (String) model.getValueAt(selectedRow, 5),
                (String) model.getValueAt(selectedRow, 6),
                (String) model.getValueAt(selectedRow, 7),
                (String) model.getValueAt(selectedRow, 8)
        );
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
