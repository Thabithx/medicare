package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.AppointmentStatusController;
import model.AppointmentStatus;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import view.style.Theme;

public class StatusTrackingPanel extends JPanel {

    private JTable statusTable;
    private DefaultTableModel tableModel;
    private AppointmentStatusController statusController;
    private AppointmentPanel appointmentPanel;

    public StatusTrackingPanel() {
        this(null);
    }

    public StatusTrackingPanel(AppointmentPanel appointmentPanel) {
        this.appointmentPanel = appointmentPanel;
        this.statusController = new AppointmentStatusController();

        setLayout(new BorderLayout());
        setBackground(view.style.Theme.BG_COLOR);
        setBorder(view.style.Theme.createPadding(20));

        // Header
        JLabel lblTitle = view.style.Theme.createTitleLabel("Track Appointment Status");
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Patient ID", "Doctor ID", "Date", "Time", "Current Status", "Notes" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        statusTable = new JTable(tableModel);
        statusTable.setRowHeight(30);
        statusTable.setFont(Theme.FONT_REGULAR);
        statusTable.getTableHeader().setFont(Theme.FONT_BOLD);
        statusTable.getTableHeader().setBackground(Theme.LIGHT);
        statusTable.setSelectionBackground(Theme.PRIMARY);
        statusTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(statusTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(view.style.Theme.BG_COLOR);

        JButton btnUpdateStatus = view.style.Theme.createButton("Update Status", view.style.Theme.WARNING);
        JButton btnViewDetails = view.style.Theme.createButton("View Details", view.style.Theme.INFO);
        JButton btnRefresh = view.style.Theme.createButton("Refresh", view.style.Theme.PRIMARY);

        btnPanel.add(btnUpdateStatus);
        btnPanel.add(btnViewDetails);
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        loadStatusData();

        btnUpdateStatus.addActionListener(e -> onUpdateStatus());
        btnViewDetails.addActionListener(e -> onViewDetails());
        btnRefresh.addActionListener(e -> loadStatusData());
    }

    private void onUpdateStatus() {
        int row = statusTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment to update.");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        AppointmentStatus status = statusController.getAppointmentStatusById(id);
        if (status != null)
            new StatusUpdateDialog((Frame) SwingUtilities.getWindowAncestor(this), status, statusController, this)
                    .setVisible(true);
    }

    private void onViewDetails() {
        int row = statusTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment to view.");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        AppointmentStatus status = statusController.getAppointmentStatusById(id);
        if (status != null) {
            JOptionPane.showMessageDialog(this,
                    "Appointment ID: " + status.getId() + "\nPatient ID: " + status.getPatientId() +
                            "\nDoctor ID: " + status.getDoctorId() + "\nDate: " + status.getAppointmentDate() +
                            "\nTime: " + status.getAppointmentTime() + "\nStatus: " + status.getStatus() +
                            "\nNotes: " + status.getRemarks(),
                    "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadStatusData() {
        tableModel.setRowCount(0);
        List<AppointmentStatus> list = statusController.getAllAppointmentStatuses();
        for (AppointmentStatus s : list) {
            tableModel.addRow(new Object[] {
                    s.getId(), s.getPatientId(), s.getDoctorId(),
                    s.getAppointmentDate(), s.getAppointmentTime(),
                    s.getStatus(), s.getRemarks()
            });
        }
    }
}
