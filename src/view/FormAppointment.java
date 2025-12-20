package view;

import controller.AppointmentController;
import controller.DoctorController;
import db.Connectdb;
import model.DoctorAppointment;
import model.Doctor;
import view.style.Theme;
import service.SchedulerService;
import service.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FormAppointment extends JDialog {

    private static final long serialVersionUID = 1L;
    private AppointmentController controller;
    private AppointmentPanel parent;

    private JComboBox<String> cbPatient;
    private JComboBox<String> cbSpecialty;
    private JComboBox<String> cbDoctor;
    private JRadioButton rbAuto, rbManual;
    private ButtonGroup modeGroup;
    private JSpinner spDate;
    private JComboBox<String> cbTimeSlot;
    private JTextArea taRemarks;
    private JTextArea taSymptoms;

    private JButton btnSave;
    private JButton btnCancel;

    private DoctorAppointment editingAppointment;
    private Doctor autoAssignedDoctor; // Fix: Cache the auto-assigned doctor

    private SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FormAppointment(DoctorAppointment appointment, AppointmentController controller, AppointmentPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), true);
        this.controller = controller;
        this.parent = parent;
        initCommon(appointment);
    }

    public FormAppointment(Window owner, DoctorAppointment appointment, AppointmentController controller) {
        super(owner, DEFAULT_MODALITY_TYPE);
        this.controller = controller;
        this.parent = null;
        initCommon(appointment);
    }

    private void initCommon(DoctorAppointment appointment) {
        this.editingAppointment = appointment;

        getContentPane().setBackground(Theme.BG_COLOR);

        setTitle(appointment == null ? "Make Appointment" : "Edit Appointment");
        setSize(500, 550);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Logic handled in onSave
        initComponents();

        if (!java.beans.Beans.isDesignTime()) {
            loadPatients();
            loadSpecialties();
        }

        // Default State
        if (appointment == null) {
            rbAuto.setSelected(true);
            toggleDoctorSelection(false);
        } else {
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

        // -- Patient --
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 0.2;
        form.add(new JLabel("Patient:"), g);
        cbPatient = new JComboBox<>();
        g.gridx = 1;
        g.gridy = 0;
        g.weightx = 0.8;
        form.add(cbPatient, g);

        if (SessionManager.isPatient()) {
            cbPatient.setEnabled(false);
        }

        // -- Specialty (New) --
        g.gridx = 0;
        g.gridy = 1;
        form.add(new JLabel("Specialty:"), g);
        cbSpecialty = new JComboBox<>();
        g.gridx = 1;
        g.gridy = 1;
        form.add(cbSpecialty, g);

        // -- Doctor Selection Mode --
        g.gridx = 0;
        g.gridy = 2;
        form.add(new JLabel("Selection:"), g);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rbAuto = new JRadioButton("Auto-Assign (Based on Specialty)");
        rbManual = new JRadioButton("Manual Select");
        modeGroup = new ButtonGroup();
        modeGroup.add(rbAuto);
        modeGroup.add(rbManual);
        modePanel.add(rbAuto);
        modePanel.add(rbManual);

        g.gridx = 1;
        g.gridy = 2;
        form.add(modePanel, g);

        // -- Doctor Dropdown (Manual) --
        g.gridx = 0;
        g.gridy = 3;
        form.add(new JLabel("Doctor:"), g);
        cbDoctor = new JComboBox<>();
        g.gridx = 1;
        g.gridy = 3;
        form.add(cbDoctor, g);

        // -- Date --
        g.gridx = 0;
        g.gridy = 4;
        form.add(new JLabel("Date:"), g);
        spDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDate, "yyyy-MM-dd");
        spDate.setEditor(dateEditor);
        g.gridx = 1;
        g.gridy = 4;
        form.add(spDate, g);

        // -- Time --
        g.gridx = 0;
        g.gridy = 5;
        form.add(new JLabel("Available Time:"), g);
        cbTimeSlot = new JComboBox<>();
        g.gridx = 1;
        g.gridy = 5;
        form.add(cbTimeSlot, g);

        // -- Symptoms (New) --
        g.gridx = 0;
        g.gridy = 6;
        form.add(new JLabel("Symptoms:"), g);
        taSymptoms = new JTextArea(3, 20);
        g.gridx = 1;
        g.gridy = 6;
        form.add(new JScrollPane(taSymptoms), g);

        // -- Remarks --
        g.gridx = 0;
        g.gridy = 7;
        form.add(new JLabel("Remarks:"), g);
        taRemarks = new JTextArea(3, 20);
        g.gridx = 1;
        g.gridy = 7;
        form.add(new JScrollPane(taRemarks), g);

        content.add(form, BorderLayout.CENTER);

        // -- Buttons --
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Theme.BG_COLOR);

        btnSave = Theme.createButton(editingAppointment == null ? "Book Now" : "Update Booking", Theme.SUCCESS);
        btnCancel = Theme.createButton("Cancel", Theme.DANGER);

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        content.add(btnPanel, BorderLayout.SOUTH);

        // -- Listeners --
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());

        rbAuto.addActionListener(e -> toggleDoctorSelection(false));
        rbManual.addActionListener(e -> toggleDoctorSelection(true));

        cbSpecialty.addActionListener(e -> {
            if (rbManual.isSelected())
                loadDoctorsBySpecialty();
            else {
                // Auto Mode: Assign doctor immediately when specialty changes
                String spec = cbSpecialty.getSelectedItem().toString();
                autoAssignedDoctor = DoctorController.recommendDoctor(spec);

                // Update the "System will assign..." text to show who it is (optional but
                // helpful debugging/UX)
                cbDoctor.removeAllItems();
                if (autoAssignedDoctor != null) {
                    cbDoctor.addItem("System will assign: " + autoAssignedDoctor.getFirstName() + " "
                            + autoAssignedDoctor.getLastName());
                } else {
                    cbDoctor.addItem("System will assign: (No doctor found)");
                }

                updateTimeSlots();
            }
        });

        cbDoctor.addActionListener(e -> updateTimeSlots());
        spDate.addChangeListener(e -> updateTimeSlots());
    }

    // Toggle manual doctor dropdown visibility/enablement
    private void toggleDoctorSelection(boolean isManual) {
        cbDoctor.setEnabled(isManual);
        if (isManual) {
            loadDoctorsBySpecialty();
        } else {
            // Trigger auto-assignment immediately if specialty is already selected
            if (cbSpecialty.getSelectedItem() != null) {
                String spec = cbSpecialty.getSelectedItem().toString();
                autoAssignedDoctor = DoctorController.recommendDoctor(spec);

                cbDoctor.removeAllItems();
                if (autoAssignedDoctor != null) {
                    cbDoctor.addItem("System will assign: " + autoAssignedDoctor.getFirstName() + " "
                            + autoAssignedDoctor.getLastName());
                } else {
                    cbDoctor.addItem("System will assign: (No doctor found)");
                }
                updateTimeSlots();
            } else {
                cbDoctor.removeAllItems();
                cbDoctor.addItem("System will assign...");
            }
        }
    }

    private void loadSpecialties() {
        cbSpecialty.removeAllItems();
        Set<String> specialties = new HashSet<>();
        // Query distinct specialties
        String sql = "SELECT DISTINCT specialty FROM Doctors";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                specialties.add(rs.getString("specialty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String s : specialties)
            cbSpecialty.addItem(s);
    }

    private void loadDoctorsBySpecialty() {
        cbDoctor.removeAllItems();
        if (cbSpecialty.getSelectedItem() == null)
            return;

        String spec = cbSpecialty.getSelectedItem().toString();
        String sql = "SELECT doctor_id, first_name, last_name FROM Doctors WHERE specialty = ?";

        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, spec);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                cbDoctor.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTimeSlots() {
        Doctor doc = null;

        // If Manual, get from dropdown
        if (rbManual.isSelected() && cbDoctor.getSelectedItem() != null
                && !cbDoctor.getSelectedItem().toString().startsWith("System")) {
            String item = cbDoctor.getSelectedItem().toString();
            int id = Integer.parseInt(item.split(" -")[0]);
            doc = DoctorController.getDoctorById(id);
        }

        else if (rbAuto.isSelected()) {
            // Fix: Use the cached auto-assigned doctor
            doc = autoAssignedDoctor;
        }

        if (doc != null) {
            cbTimeSlot.removeAllItems();
            java.util.List<String> slots = SchedulerService.getAvailableSlots(doc, (Date) spDate.getValue());
            if (slots.isEmpty())
                cbTimeSlot.addItem("No slots available");
            else
                for (String s : slots)
                    cbTimeSlot.addItem(s);
        } else {
            cbTimeSlot.removeAllItems();
            cbTimeSlot.addItem("Select Doctor/Specialty first");
        }
    }

    private void onSave() {
        if (cbPatient.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Patient error.");
            return;
        }

        try {
            int patientId = Integer.parseInt(cbPatient.getSelectedItem().toString().split(" -")[0]);
            int doctorId = 0;

            // Resolve Doctor
            if (rbManual.isSelected()) {
                if (cbDoctor.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Select a doctor");
                    return;
                }
                doctorId = Integer.parseInt(cbDoctor.getSelectedItem().toString().split(" -")[0]);
            } else {
                // Auto Assign
                // Fix: Use the cached doctor
                if (autoAssignedDoctor == null) {
                    // Try one last time just in case
                    String spec = cbSpecialty.getSelectedItem() != null ? cbSpecialty.getSelectedItem().toString() : "";
                    autoAssignedDoctor = DoctorController.recommendDoctor(spec);
                }

                if (autoAssignedDoctor == null) {
                    JOptionPane.showMessageDialog(this, "No doctors available for selected specialty.");
                    return;
                }
                doctorId = autoAssignedDoctor.getId();
            }

            // Check Time
            String timeSql = cbTimeSlot.getSelectedItem() != null ? cbTimeSlot.getSelectedItem().toString() : "";
            if (timeSql.length() < 5 || timeSql.startsWith("No") || timeSql.startsWith("Select")) {
                JOptionPane.showMessageDialog(this, "Invalid Time Slot");
                return;
            }
            if (timeSql.length() == 5)
                timeSql += ":00";

            String dateSql = sqlDateFormat.format((Date) spDate.getValue());

            // Merge Remarks + Symptoms
            String finalRemarks = taRemarks.getText().trim();
            if (!taSymptoms.getText().trim().isEmpty()) {
                finalRemarks = "SYMPTOMS: " + taSymptoms.getText().trim() + " | " + finalRemarks;
            }

            if (editingAppointment == null) {
                DoctorAppointment newApp = new DoctorAppointment(0, patientId, doctorId, dateSql, timeSql, finalRemarks,
                        "PENDING");
                if (controller.addAppointment(newApp)) {
                    JOptionPane.showMessageDialog(this, "✅ Appointment Booked! Doctor Assigned.");
                    if (parent != null)
                        parent.loadAppointmentData();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book.");
                }
            } else {
                // Update logic...
                editingAppointment.setPatientId(patientId);
                editingAppointment.setDoctorId(doctorId);
                editingAppointment.setAppointmentDate(dateSql);
                editingAppointment.setAppointmentTime(timeSql);
                editingAppointment.setRemarks(finalRemarks);

                if (controller.updateAppointment(editingAppointment)) {
                    JOptionPane.showMessageDialog(this, "Updated.");
                    if (parent != null)
                        parent.loadAppointmentData();
                    dispose();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads patients from DB into combo box.
     */
    private void loadPatients() {
        cbPatient.removeAllItems();
        // If Patient Login, only show self
        if (SessionManager.isPatient()) {
            int myId = SessionManager.getUser().getReferenceId();
        }

        // General Load logic
        String sql = "SELECT patient_id, first_name, last_name FROM Patients";
        if (SessionManager.isPatient())
            sql += " WHERE patient_id = " + SessionManager.getUser().getReferenceId();

        try (Connection con = Connectdb.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cbPatient.addItem(
                        rs.getInt("patient_id") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill form fields when editing
     */
    private void fillForm(DoctorAppointment a) {
        rbManual.setSelected(true);
        toggleDoctorSelection(true);

        // Set Patient
        for (int i = 0; i < cbPatient.getItemCount(); i++) {
            if (cbPatient.getItemAt(i).startsWith(a.getPatientId() + " -")) {
                cbPatient.setSelectedIndex(i);
                break;
            }
        }

        Doctor doc = DoctorController.getDoctorById(a.getDoctorId());
        if (doc != null) {
            cbSpecialty.setSelectedItem(doc.getSpecialty());
            loadDoctorsBySpecialty();
            // Select Doc
            for (int i = 0; i < cbDoctor.getItemCount(); i++) {
                if (cbDoctor.getItemAt(i).startsWith(a.getDoctorId() + " -")) {
                    cbDoctor.setSelectedIndex(i);
                    break;
                }
            }
        }

        try {
            spDate.setValue(sqlDateFormat.parse(a.getAppointmentDate()));
            // Time...
            updateTimeSlots();
            String t = a.getAppointmentTime();
            if (t.length() >= 5)
                t = t.substring(0, 5);
            cbTimeSlot.setSelectedItem(t);
        } catch (Exception e) {
        }

        // Remarks / Symptoms
        String r = a.getRemarks();
        if (r.contains("SYMPTOMS:")) {
            String[] parts = r.split("\\|");
            for (String p : parts) {
                if (p.trim().startsWith("SYMPTOMS:")) {
                    taSymptoms.setText(p.replace("SYMPTOMS:", "").trim());
                } else {
                    taRemarks.setText(p.trim());
                }
            }
        } else {
            taRemarks.setText(r);
        }
    }
}