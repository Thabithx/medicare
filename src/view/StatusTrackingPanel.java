package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.AppointmentStatusController;
import Model.AppointmentStatus;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StatusTrackingPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable statusTable;
    private DefaultTableModel tableModel;
    private AppointmentStatusController statusController;
    private AppointmentPanel appointmentPanel; // for refresh

    public StatusTrackingPanel(AppointmentPanel appointmentPanel) {
        this.appointmentPanel = appointmentPanel;
        this.statusController = new AppointmentStatusController();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("Track Appointment Status", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Patient ID", "Doctor ID", "Date", "Time", "Current Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        statusTable = new JTable(tableModel);
        statusTable.setRowHeight(30);
        statusTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusTable.getTableHeader().setBackground(new Color(240, 240, 240));
        add(new JScrollPane(statusTable), BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(new Color(245, 245, 245));

        JButton btnUpdateStatus = new JButton("Update Status");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnViewDetails = new JButton("View Details");

        for (JButton btn : new JButton[]{btnUpdateStatus, btnViewDetails, btnRefresh}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 123, 255));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            styleButton(btn);
            btnPanel.add(btn);
        }

        add(btnPanel, BorderLayout.SOUTH);

        // Load initial data
        loadStatusData();

        // Button Actions
        btnUpdateStatus.addActionListener(e -> onUpdateStatus());
        btnViewDetails.addActionListener(e -> onViewDetails());
        btnRefresh.addActionListener(e -> loadStatusData());
    }

    private void onUpdateStatus() {
        int selectedRow = statusTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update status.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int appointmentId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        AppointmentStatus status = statusController.getAppointmentStatusById(appointmentId);

        if (status != null) {
            StatusUpdateDialog dialog = new StatusUpdateDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), 
                    status, 
                    statusController, 
                    this
            );
            dialog.setVisible(true);
        }
    }

    private void onViewDetails() {
        int selectedRow = statusTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to view details.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int appointmentId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        AppointmentStatus status = statusController.getAppointmentStatusById(appointmentId);

        if (status != null) {
            StringBuilder details = new StringBuilder();
            details.append("Appointment ID: ").append(status.getId()).append("\n");
            details.append("Patient ID: ").append(status.getPatientId()).append("\n");
            details.append("Doctor ID: ").append(status.getDoctorId()).append("\n");
            details.append("Date: ").append(status.getAppointmentDate()).append("\n");
            details.append("Time: ").append(status.getAppointmentTime()).append("\n");
            details.append("Status: ").append(status.getStatus()).append(" ").append(status.getStatusIcon()).append("\n");
            details.append("Notes: ").append(status.getRemarks()).append("\n");

            JOptionPane.showMessageDialog(this, details.toString(), 
                                        "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadStatusData() {
        tableModel.setRowCount(0);
        List<AppointmentStatus> list = statusController.getAllAppointmentStatuses();

        for (AppointmentStatus s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getPatientId(),
                    s.getDoctorId(),
                    s.getAppointmentDate(),
                    s.getAppointmentTime(),
                    s.getStatusIcon() + " " + s.getStatus(),
                    s.getRemarks()
            });
        }
    }

    private void styleButton(JButton btn) {
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

