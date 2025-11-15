package view;

import controller.AppointmentController;
import db.Connectdb;
import Model.DoctorAppointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FormAppointment - a JDialog used for adding and editing appointments.
 * Constructor:
 *   FormAppointment(DoctorAppointment appointment, AppointmentController controller, AppointmentPanel parent)
 *   - if appointment == null -> it's Add mode
 *   - otherwise Edit mode (fields auto-filled)
 */
public class FormAppointment extends JDialog {

    private static final long serialVersionUID = 1L;
    private AppointmentController controller;
    private AppointmentPanel parent;

    private JComboBox<String> cbPatient; // entries like "1 - John Doe"
    private JComboBox<String> cbDoctor;  // entries like "2 - Dr. Smith"
    private JSpinner spDate;
    private JSpinner spTime;
    private JTextArea taRemarks;

    private JButton btnSave;
    private JButton btnCancel;

    private DoctorAppointment editingAppointment; // null if adding

    private SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sqlTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public FormAppointment(DoctorAppointment appointment, AppointmentController controller, AppointmentPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), true);
        this.controller = controller;
        this.parent = parent;
        this.editingAppointment = appointment;

        setTitle(appointment == null ? "Make Appointment" : "Edit Appointment");
        setSize(480, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents();
        loadPatients();
        loadDoctors();

        if (appointment != null) {
            fillForm(appointment);
        }
    }

    private void initComponents() {
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(content);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        // Patient
        g.gridx = 0; g.gridy = 0; g.weightx = 0.2;
        form.add(new JLabel("Patient:"), g);
        cbPatient = new JComboBox<>();
        g.gridx = 1; g.gridy = 0; g.weightx = 0.8;
        form.add(cbPatient, g);

        // Doctor
        g.gridx = 0; g.gridy = 1; g.weightx = 0.2;
        form.add(new JLabel("Doctor:"), g);
        cbDoctor = new JComboBox<>();
        g.gridx = 1; g.gridy = 1; g.weightx = 0.8;
        form.add(cbDoctor, g);

        // Date (use JSpinner date)
        g.gridx = 0; g.gridy = 2; g.weightx = 0.2;
        form.add(new JLabel("Date:"), g);
        spDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDate, "yyyy-MM-dd");
        spDate.setEditor(dateEditor);
        g.gridx = 1; g.gridy = 2; g.weightx = 0.8;
        form.add(spDate, g);

        // Time (use JSpinner time)
        g.gridx = 0; g.gridy = 3; g.weightx = 0.2;
        form.add(new JLabel("Time:"), g);
        spTime = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE));
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spTime, "HH:mm");
        spTime.setEditor(timeEditor);
        g.gridx = 1; g.gridy = 3; g.weightx = 0.8;
        form.add(spTime, g);

        // Remarks
        g.gridx = 0; g.gridy = 4; g.weightx = 0.2;
        form.add(new JLabel("Remarks:"), g);
        taRemarks = new JTextArea(4, 20);
        JScrollPane spRemarks = new JScrollPane(taRemarks);
        g.gridx = 1; g.gridy = 4; g.weightx = 0.8;
        form.add(spRemarks, g);

        // NOTE: Status field has been removed
        // Status is now managed only in the StatusTrackingPanel (Task 4)

        content.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton(editingAppointment == null ? "Save" : "Update");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        content.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> onSave());
    }

    /**
     * Fill form fields when editing
     */
    private void fillForm(DoctorAppointment a) {
        // select patient item matching id if exists
        String patientPref = a.getPatientId() + " -";
        for (int i = 0; i < cbPatient.getItemCount(); i++) {
            if (cbPatient.getItemAt(i).startsWith(a.getPatientId() + " -")) {
                cbPatient.setSelectedIndex(i);
                break;
            }
        }

        String doctorPref = a.getDoctorId() + " -";
        for (int i = 0; i < cbDoctor.getItemCount(); i++) {
            if (cbDoctor.getItemAt(i).startsWith(a.getDoctorId() + " -")) {
                cbDoctor.setSelectedIndex(i);
                break;
            }
        }

        try {
            Date d = sqlDateFormat.parse(a.getAppointmentDate());
            spDate.setValue(d);
        } catch (Exception ex) {
            // ignore parse errors
        }

        try {
            // To show time, parse HH:mm:ss and set spinner
            Date t = sqlTimeFormat.parse(a.getAppointmentTime());
            spTime.setValue(t);
        } catch (Exception ex) {
            // ignore
        }

        taRemarks.setText(a.getRemarks());
        // Status is NOT filled here anymore - it's handled in StatusTrackingPanel
    }

    private void onSave() {
        // Validation
        if (cbPatient.getSelectedItem() == null || cbDoctor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select both patient and doctor.");
            return;
        }

        try {
            String patientItem = cbPatient.getSelectedItem().toString();
            int patientId = Integer.parseInt(patientItem.split(" -")[0].trim());

            String doctorItem = cbDoctor.getSelectedItem().toString();
            int doctorId = Integer.parseInt(doctorItem.split(" -")[0].trim());

            Date dateVal = (Date) spDate.getValue();
            Date timeVal = (Date) spTime.getValue();

            String dateSql = sqlDateFormat.format(dateVal);
            // combine time spinner into HH:mm:ss (append :00)
            SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
            String timeSql = timeOnly.format(timeVal);

            String remarks = taRemarks.getText().trim();
            // Status is always SCHEDULED for new appointments
            String status = "SCHEDULED";

            if (editingAppointment == null) {
                // Add
                DoctorAppointment newApp = new DoctorAppointment(
                        0,
                        patientId,
                        doctorId,
                        dateSql,
                        timeSql,
                        remarks,
                        status
                );
                boolean ok = controller.addAppointment(newApp);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Appointment added successfully.");
                    parent.loadAppointmentData();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add appointment. Check console for errors.");
                }
            } else {
                // Update (note: status is NOT changed here)
                editingAppointment.setPatientId(patientId);
                editingAppointment.setDoctorId(doctorId);
                editingAppointment.setAppointmentDate(dateSql);
                editingAppointment.setAppointmentTime(timeSql);
                editingAppointment.setRemarks(remarks);
                // Keep the existing status - don't change it

                boolean ok = controller.updateAppointment(editingAppointment);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully.");
                    parent.loadAppointmentData();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update appointment. Check console for errors.");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Loads patients from DB into combo box.
     */
    private void loadPatients() {
        cbPatient.removeAllItems();
        String sql = "SELECT patient_id, first_name,last_name FROM patients ORDER BY first_name";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                cbPatient.addItem(id + " - " + fname + " " + lname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads doctors from DB into combo box.
     */
    private void loadDoctors() {
        cbDoctor.removeAllItems();
        String sql = "SELECT doctor_id, first_name,last_name FROM doctors ORDER BY first_name";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                cbDoctor.addItem(id + " - " + fname+ " " + lname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}