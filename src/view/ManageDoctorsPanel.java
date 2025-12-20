package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import java.awt.Dimension; // Import Dimension

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.JTextField; // Explicit import
import controller.DoctorController;
import model.Doctor;
import view.style.Theme;

public class ManageDoctorsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private Dashboard dashboard;
    private DefaultTableModel model;

    public ManageDoctorsPanel() {
        this(null);
    }

    public ManageDoctorsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(Theme.createPadding(20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        javax.swing.JLabel lblTitle = Theme.createTitleLabel("Manage Doctors");
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Theme.BG_COLOR);
        JTextField searchField = Theme.createPlaceholderTextField(20, "Search by Name...");
        searchField.setPreferredSize(new Dimension(250, 35));

        JButton btnSearch = Theme.createGradientButton("Search");
        btnSearch.setPreferredSize(new Dimension(100, 35));

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        /* ================= TABLE ================= */
        String[] columns = {
                "ID", "First Name", "Last Name", "Gender", "Address", "DOB",
                "Phone", "Specialty", "Qualification", "Schedule", "Timeslot"
        };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int str, int r) {
                return false;
            }
        };
        table = new JTable(model);
        Theme.applyTableStyling(table); // Apple Table Styling

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        btnSearch.addActionListener(e -> {
            String query = searchField.getText();
            if (query.trim().length() == 0 || query.equals("Search by Name...")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        /* ================= BUTTONS ================= */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(Theme.BG_COLOR);

        JButton btnViewDetails = Theme.createButton("View Details", Theme.INFO);
        btnViewDetails.setPreferredSize(new Dimension(140, 35));

        JButton btnAdd = Theme.createGradientButton("Add Doctor");
        btnAdd.setPreferredSize(new Dimension(140, 35));

        JButton btnEdit = Theme.createButton("Edit", Theme.WARNING);
        btnEdit.setPreferredSize(new Dimension(100, 35));

        JButton btnDelete = Theme.createButton("Delete", Theme.DANGER);
        btnDelete.setPreferredSize(new Dimension(100, 35));

        JButton btnRefresh = Theme.createButton("Refresh", Theme.INFO);
        btnRefresh.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnViewDetails);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        btnViewDetails.addActionListener(e -> viewSelectedDoctorDetails());
        btnRefresh.addActionListener(e -> loadDoctors());
        btnEdit.addActionListener(e -> editSelectedDoctor());
        btnAdd.addActionListener(e -> dashboard.switchPanel("AddDoctor"));
        btnDelete.addActionListener(e -> deleteSelectedDoctor());

        if (!java.beans.Beans.isDesignTime()) {
            loadDoctors();
        }
    }

    /* ================= LOAD DOCTORS ================= */
    public void loadDoctors() {
        model.setRowCount(0);

        List<Doctor> doctors = DoctorController.getAllDoctors();

        for (Doctor d : doctors) {
            model.addRow(new Object[] {
                    d.getId(),
                    d.getFirstName(),
                    d.getLastName(),
                    d.getGender(),
                    d.getAddress(),
                    d.getDob(),
                    d.getPhone(),
                    d.getSpecialty(),
                    d.getQualification(),
                    d.getSchedule(),
                    d.getTimeslot()
            });
        }
    }

    private void viewSelectedDoctorDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to view!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        dashboard.showDoctorDetails(id);
    }

    /* ================= EDIT ================= */
    private void editSelectedDoctor() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!");
            return;
        }

        dashboard.showEditDoctorPanel(
                (int) model.getValueAt(row, 0), // ID
                model.getValueAt(row, 1).toString(), // First name
                model.getValueAt(row, 2).toString(), // Last name
                model.getValueAt(row, 3).toString(), // Gender
                model.getValueAt(row, 4).toString(), // Address
                model.getValueAt(row, 5).toString(), // DOB
                model.getValueAt(row, 6).toString(), // Phone
                model.getValueAt(row, 7).toString(), // Specialty
                model.getValueAt(row, 8).toString(), // Qualification
                model.getValueAt(row, 9).toString(), // Schedule
                model.getValueAt(row, 10).toString() // Timeslot
        );
    }

    /* ================= DELETE ================= */
    private void deleteSelectedDoctor() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this doctor?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (DoctorController.deleteDoctor(id)) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
                loadDoctors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor!");
            }
        }
    }
}
