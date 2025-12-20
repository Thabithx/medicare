package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.PatientController;
import model.Patient;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import view.style.Theme;

public class ManagePatientsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private Dashboard dashboard;

    public ManagePatientsPanel() {
        this(null);
    }

    public ManagePatientsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(Theme.createPadding(20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = Theme.createTitleLabel("Manage Patients");
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

        // Table
        String[] columns = { "ID", "First Name", "Last Name", "Gender", "DOB", "Blood Group", "Phone", "Email",
                "Address" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        Theme.applyTableStyling(table);

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

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(Theme.BG_COLOR);

        JButton btnViewDetails = Theme.createButton("View Details", Theme.INFO);
        btnViewDetails.setPreferredSize(new Dimension(140, 35));

        JButton btnAdd = Theme.createGradientButton("Add Patient"); // Gradient for primary action
        btnAdd.setPreferredSize(new Dimension(140, 35));

        JButton btnEdit = Theme.createButton("Edit", Theme.WARNING);
        btnEdit.setPreferredSize(new Dimension(100, 35));

        JButton btnDelete = Theme.createButton("Delete", Theme.DANGER);
        btnDelete.setPreferredSize(new Dimension(100, 35));

        JButton btnRefresh = Theme.createButton("Refresh", Theme.INFO);
        btnRefresh.setPreferredSize(new Dimension(100, 35));

        btnPanel.add(btnViewDetails);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnViewDetails.addActionListener(e -> viewSelectedPatientDetails());
        btnRefresh.addActionListener(e -> loadPatients());

        btnAdd.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "AddPatient");
        });

        btnEdit.addActionListener(e -> editSelectedPatient());
        btnDelete.addActionListener(e -> deleteSelectedPatient());

        if (!java.beans.Beans.isDesignTime()) {
            loadPatients();
        }
    }

    public void loadPatients() {
        model.setRowCount(0);
        List<Patient> list = PatientController.getAllPatients();
        for (Patient p : list) {
            model.addRow(new Object[] {
                    p.getId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getGender(),
                    p.getDob(),
                    p.getBloodGroup(),
                    p.getPhone(),
                    p.getEmail(),
                    p.getAddress()
            });
        }
    }

    private void viewSelectedPatientDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        dashboard.showPatientDetails(id);
    }

    private void editSelectedPatient() {
        int selectedRow = table.getSelectedRow();
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

    private void deleteSelectedPatient() {
        int selectedRow = table.getSelectedRow();
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
