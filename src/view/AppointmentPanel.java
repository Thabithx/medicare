package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.AppointmentController;
import Model.DoctorAppointment;
import Model.AppointmentDetails;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel Model;
    private AppointmentController controller;

    public AppointmentPanel() {
        controller = new AppointmentController();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 🔹 Header label
        JLabel lblTitle = new JLabel("Appointment Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 🔹 Table (PATIENT NAME, DOCTOR NAME - NO IDs)
        String[] columnNames = {"ID", "Patient Name", "Doctor Name", "Date", "Time", "Remarks"};
        Model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table = new JTable(Model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 🔹 Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnAdd = new JButton("Make Appointment");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnView = new JButton("View Status");

        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnView}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 123, 255));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            btnPanelStyle(btn);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Load data into the table
        loadAppointmentData();

        // ✅ Button Actions

        // Add Appointment
        btnAdd.addActionListener(e -> {
            FormAppointment form = new FormAppointment(null, controller, this);
            form.setVisible(true);
        });

        // Edit Appointment
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an appointment to edit.");
                return;
            }

            int id = Integer.parseInt(Model.getValueAt(selectedRow, 0).toString());
            DoctorAppointment appointment = controller.getAppointmentById(id);
            if (appointment != null) {
                FormAppointment form = new FormAppointment(appointment, controller, this);
                form.setVisible(true);
            }
        });

        // Delete Appointment
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an appointment to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(Model.getValueAt(selectedRow, 0).toString());
                if (controller.deleteAppointment(id)) {
                    JOptionPane.showMessageDialog(this, "Appointment deleted successfully!");
                    loadAppointmentData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete appointment!");
                }
            }
        });

        // ========================================
        // VIEW STATUS - TASK 4 INTEGRATION
        // ========================================
        btnView.addActionListener(e -> {
            JFrame statusFrame = new JFrame("Appointment Status Tracking - Task 4");
            statusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            statusFrame.setSize(950, 650);
            statusFrame.setLocationRelativeTo(null);
            statusFrame.add(new StatusTrackingPanel(AppointmentPanel.this));
            statusFrame.setVisible(true);
        });
    }

    // 🔹 Load data from DB into the JTable (SHOW NAMES INSTEAD OF IDs)
    public void loadAppointmentData() {
        Model.setRowCount(0);
        
        // Call controller method that returns AppointmentDetails with names
        List<AppointmentDetails> list = controller.getAllAppointmentDetails();
        
        for (AppointmentDetails detail : list) {
            Model.addRow(new Object[]{
                    detail.getId(),
                    detail.getPatientName(),    // Patient Name from DTO
                    detail.getDoctorName(),     // Doctor Name from DTO
                    detail.getAppointmentDate(),
                    detail.getAppointmentTime(),
                    detail.getRemarks()
            });
        }
    }

    // 🔹 Small helper for button hover style
    private void btnPanelStyle(JButton btn) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 102, 204));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 123, 255));
            }
        });
    }
}