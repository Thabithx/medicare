package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import controller.AppointmentStatusController;
import model.AppointmentStatus;

public class StatusUpdateDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JComboBox<String> statusComboBox;
    private JTextArea remarksArea;
    private AppointmentStatus appointment;
    private AppointmentStatusController controller;
    private StatusTrackingPanel trackingPanel;

    public StatusUpdateDialog(Frame parent, AppointmentStatus appt, AppointmentStatusController ctrl,
            StatusTrackingPanel panel) {
        super(parent, "Update Status", true);
        this.appointment = appt;
        this.controller = ctrl;
        this.trackingPanel = panel;

        setBounds(100, 100, 450, 350);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Update Appointment Status");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 10, 300, 30);
        contentPanel.add(lblTitle);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(20, 60, 100, 20);
        contentPanel.add(lblStatus);

        String[] statuses = { "SCHEDULED", "COMPLETED", "CANCELED", "DELAYED" };
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setSelectedItem(appt.getStatus());
        statusComboBox.setBounds(120, 60, 200, 25);
        contentPanel.add(statusComboBox);

        JLabel lblRemarks = new JLabel("Remarks:");
        lblRemarks.setBounds(20, 100, 100, 20);
        contentPanel.add(lblRemarks);

        remarksArea = new JTextArea(appt.getRemarks());
        remarksArea.setLineWrap(true);
        remarksArea.setWrapStyleWord(true);
        remarksArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(remarksArea);
        scrollPane.setBounds(20, 130, 400, 120);
        contentPanel.add(scrollPane);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("Update");
        okButton.addActionListener(e -> updateStatus());
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        setLocationRelativeTo(parent);
    }

    private void updateStatus() {
        String newStatus = (String) statusComboBox.getSelectedItem();
        String newRemarks = remarksArea.getText();

        boolean success = controller.updateAppointmentStatus(appointment.getId(), newStatus, newRemarks);

        if (success) {
            JOptionPane.showMessageDialog(this, "Status updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}