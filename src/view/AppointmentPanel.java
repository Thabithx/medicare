package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import controller.AppointmentController;
import model.AppointmentDetails;
import model.DoctorAppointment;
import service.SessionManager;

import java.awt.*;
import java.util.List;

import view.style.Theme;

public class AppointmentPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentController controller;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTabbedPane tabbedPane;

    // Doctor action buttons
    private JButton btnAccept, btnDecline, btnComplete;

    public AppointmentPanel() {
        controller = new AppointmentController();
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(Theme.createPadding(30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        String titleText = SessionManager.isDoctor() ? "My Schedule" : "Appointment Management";
        JLabel lblTitle = Theme.createTitleLabel(titleText);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Refresh Button in Header
        JButton btnRefresh = Theme.createButton("Refresh", Theme.INFO);
        btnRefresh.setPreferredSize(new Dimension(100, 35));
        btnRefresh.addActionListener(e -> loadAppointmentData());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs or Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Theme.BG_COLOR);

        createTable();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Theme.BG_COLOR);

        // Add Tabs
        tabbedPane.addTab("All Appointments", createTablePanel());
        tabbedPane.addTab("Pending", new JPanel());
        tabbedPane.addTab("Upcoming", new JPanel());
        tabbedPane.addTab("Past", new JPanel());

        // Tab Change Logic
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();

            if (idx > 0) {
            }


            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(Color.WHITE);

            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Color.WHITE);
            p.add(scroll);

            tabbedPane.setComponentAt(idx, p);

            // Filter Logic
            if (idx == 0)
                filterTable("ALL");
            else if (idx == 1)
                filterTable("PENDING");
            else if (idx == 2)
                filterTable("UPCOMING");
            else if (idx == 3)
                filterTable("PAST");

            updateButtonStates();
        });

        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(Theme.BG_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        if (SessionManager.isDoctor()) {
            btnAccept = Theme.createGradientButton("Accept");
            btnComplete = Theme.createGradientButton("Complete");
            btnDecline = Theme.createButton("Decline", Theme.DANGER);
            btnAccept.setPreferredSize(new Dimension(140, 40));
            btnComplete.setPreferredSize(new Dimension(140, 40));
            btnDecline.setPreferredSize(new Dimension(140, 40));

            btnAccept.addActionListener(e -> updateStatus("SCHEDULED"));
            btnDecline.addActionListener(e -> updateStatus("CANCELLED"));
            btnComplete.addActionListener(e -> updateStatus("COMPLETED"));

            footerPanel.add(btnAccept);
            footerPanel.add(btnComplete);
            footerPanel.add(btnDecline);

            updateButtonStates();

        } else {
            if (SessionManager.isAdmin() || SessionManager.isPatient()) {
                JButton btnAdd = Theme.createGradientButton("New Appointment");
                btnAdd.setPreferredSize(new Dimension(180, 40));

                JButton btnEdit = Theme.createButton("Edit", Theme.WARNING);
                JButton btnDelete = Theme.createButton("Cancel", Theme.DANGER);

                btnAdd.addActionListener(e -> new FormAppointment(null, controller, this).setVisible(true));
                btnEdit.addActionListener(e -> editAppointment());
                btnDelete.addActionListener(e -> deleteAppointment());

                footerPanel.add(btnAdd);
                if (SessionManager.isAdmin()) {
                    footerPanel.add(btnEdit);
                    footerPanel.add(btnDelete);
                } else {
                    // Patients can cancel
                    footerPanel.add(btnDelete);
                }
            }
        }

        add(footerPanel, BorderLayout.SOUTH);

        if (!java.beans.Beans.isDesignTime()) {
            loadAppointmentData();
        }
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll);
        return p;
    }

    private void createTable() {
        String[] columnNames = { "ID", "Patient Name", "Doctor Name", "Date", "Time", "Status", "Remarks" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        Theme.applyTableStyling(table);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
    }

    private void filterTable(String mode) {
        if (mode.equals("ALL")) {
            sorter.setRowFilter(null);
        } else if (mode.equals("PENDING")) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)PENDING", 5));
        } else if (mode.equals("UPCOMING")) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)SCHEDULED|ACCEPTED", 5));
        } else if (mode.equals("PAST")) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)COMPLETED|CANCELLED", 5));
        }
    }

    public void loadAppointmentData() {
        tableModel.setRowCount(0);
        List<AppointmentDetails> list = controller.getAllAppointmentDetails();
        for (AppointmentDetails d : list) {
            tableModel.addRow(new Object[] {
                    d.getId(), d.getPatientName(), d.getDoctorName(),
                    d.getAppointmentDate(), d.getAppointmentTime(), d.getStatus(), d.getRemarks()
            });
        }
    }

    // Doctor Actions
    private void updateStatus(String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        if (controller.updateStatus(id, newStatus)) {
            JOptionPane.showMessageDialog(this, "Status updated to " + newStatus);
            loadAppointmentData();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

    // Admin Actions
    private void editAppointment() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        DoctorAppointment ap = controller.getAppointmentById(id);
        new FormAppointment(ap, controller, this).setVisible(true);
    }

    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        if (SessionManager.isPatient()) {
            // Patient cancelling own appointment
            controller.updateStatus(id, "CANCELLED");
            JOptionPane.showMessageDialog(this, "Appointment Cancelled.");
        } else {
            controller.deleteAppointment(id);
            JOptionPane.showMessageDialog(this, "Appointment Deleted.");
        }
        loadAppointmentData();
    }

    private void updateButtonStates() {
        if (!SessionManager.isDoctor() || btnAccept == null)
            return;

        int currentTab = tabbedPane.getSelectedIndex();
        switch (currentTab) {
            case 1: // Pending
                btnAccept.setEnabled(true);
                btnDecline.setEnabled(true);
                btnComplete.setEnabled(false);
                break;
            case 2: // Upcoming
                btnAccept.setEnabled(false);
                btnComplete.setEnabled(true);
                btnDecline.setEnabled(true);
                break;
            case 3: // Past
                btnAccept.setEnabled(false);
                btnComplete.setEnabled(false);
                btnDecline.setEnabled(false);
                break;
            default: // All
                btnAccept.setEnabled(true);
                btnComplete.setEnabled(true);
                btnDecline.setEnabled(true);
        }
    }
}
