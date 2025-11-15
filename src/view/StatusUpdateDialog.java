package view;

import javax.swing.*;
import controller.AppointmentStatusController;
import Model.AppointmentStatus;
import java.awt.*;

public class StatusUpdateDialog extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppointmentStatusController statusController;
    private StatusTrackingPanel parent;
    private AppointmentStatus appointment;

    private JComboBox<String> cbStatus;
    private JTextArea taRemarks;
    private JLabel lblCurrentStatus;

    public StatusUpdateDialog(Frame owner, AppointmentStatus appointment, 
                             AppointmentStatusController controller, StatusTrackingPanel parent) {
        super(owner, "Update Appointment Status", true);
        this.appointment = appointment;
        this.statusController = controller;
        this.parent = parent;

        setSize(450, 350);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(content);

        // Header with appointment info
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        JLabel lblAppId = new JLabel("Appointment ID: " + appointment.getId());
        lblAppId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCurrentStatus = new JLabel("Current Status: " + appointment.getStatusIcon() + " " + appointment.getStatus());
        lblCurrentStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCurrentStatus.setForeground(new Color(100, 100, 100));
        headerPanel.add(lblAppId);
        headerPanel.add(lblCurrentStatus);
        content.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        // Status dropdown
        g.gridx = 0; g.gridy = 0; g.weightx = 0.25;
        formPanel.add(new JLabel("New Status:"), g);

        cbStatus = new JComboBox<>(new String[]{"SCHEDULED", "COMPLETED", "CANCELED", "DELAYED"});
        cbStatus.setSelectedItem(appointment.getStatus());
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1; g.gridy = 0; g.weightx = 0.75;
        formPanel.add(cbStatus, g);

        // Status info
        g.gridx = 0; g.gridy = 1;
        formPanel.add(new JLabel("Status Info:"), g);

        JLabel statusInfo = new JLabel("<html>" +
                "SCHEDULED: Appointment booked<br>" +
                "COMPLETED: Patient visited<br>" +
                "CANCELED: Appointment canceled<br>" +
                "DELAYED: Running late</html>");
        statusInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusInfo.setForeground(new Color(80, 80, 80));
        g.gridx = 1; g.gridy = 1;
        formPanel.add(statusInfo, g);

        // Remarks
        g.gridx = 0; g.gridy = 2; g.weightx = 0.25; g.weighty = 0.5;
        formPanel.add(new JLabel("Notes:"), g);

        taRemarks = new JTextArea(5, 25);
        taRemarks.setText(appointment.getRemarks());
        taRemarks.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        taRemarks.setLineWrap(true);
        taRemarks.setWrapStyleWord(true);
        JScrollPane spRemarks = new JScrollPane(taRemarks);
        g.gridx = 1; g.gridy = 2; g.weightx = 0.75; g.weighty = 0.5;
        formPanel.add(spRemarks, g);

        content.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSave = new JButton("Update");
        JButton btnCancel = new JButton("Cancel");

        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        content.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());
    }

    private void onSave() {
        String newStatus = cbStatus.getSelectedItem().toString();
        String remarks = taRemarks.getText().trim();

        if (statusController.updateAppointmentStatus(appointment.getId(), newStatus, remarks)) {
            JOptionPane.showMessageDialog(this, 
                    "Status updated successfully to: " + newStatus, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            parent.loadStatusData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Failed to update status. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}