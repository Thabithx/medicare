package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.Connectdb;
import model.DoctorAppointment;
import model.AppointmentDetails;
import model.Doctor;
import model.Patient;

public class AppointmentController {

    public boolean addAppointment(DoctorAppointment appointment) {
        if (!isDoctorAvailable(appointment.getDoctorId(), appointment.getAppointmentDate(),
                appointment.getAppointmentTime())) {
            return false;
        }

        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, remarks, status, prescription_image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, appointment.getPatientId());
            pst.setInt(2, appointment.getDoctorId());
            pst.setString(3, appointment.getAppointmentDate());
            pst.setString(4, appointment.getAppointmentTime());
            pst.setString(5, appointment.getRemarks());
            pst.setString(6, appointment.getStatus());
            pst.setString(7, appointment.getPrescriptionImagePath());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Get doctor and patient names for notifications
                Doctor doctor = DoctorController.getDoctorById(appointment.getDoctorId());
                Patient patient = PatientController.getPatientById(appointment.getPatientId());

                String doctorName = (doctor != null) ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName()
                        : "Doctor";
                String patientName = (patient != null) ? patient.getFirstName() + " " + patient.getLastName()
                        : "Patient";

                // Notify Doctor
                int doctorUserId = NotificationController.getUserIdByRoleRef("DOCTOR", appointment.getDoctorId());
                if (doctorUserId > 0) {
                    NotificationController.addNotification("DOCTOR",
                            "New appointment from " + patientName + " on " + appointment.getAppointmentDate() + " at "
                                    + appointment.getAppointmentTime(),
                            doctorUserId);
                }

                // Notify Patient
                int patientUserId = NotificationController.getUserIdByRoleRef("PATIENT", appointment.getPatientId());
                if (patientUserId > 0) {
                    NotificationController.addNotification("PATIENT",
                            "Appointment with " + doctorName + " scheduled for " + appointment.getAppointmentDate()
                                    + " at " + appointment.getAppointmentTime(),
                            patientUserId);
                }

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDoctorAvailable(int doctorId, String date, String time) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=? AND appointment_time=? AND status != 'CANCELLED'";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, doctorId);
            pst.setString(2, date);
            pst.setString(3, time);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DoctorAppointment> getAllAppointments() {
        List<DoctorAppointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";

        try (Connection con = Connectdb.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new DoctorAppointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getString("prescription_image_path")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        DoctorAppointment appt = getAppointmentById(id);
        if (appt == null)
            return false;

        String sql = "UPDATE appointments SET status=? WHERE id=?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, id);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Get doctor and patient names for notifications
                Doctor doctor = DoctorController.getDoctorById(appt.getDoctorId());
                Patient patient = PatientController.getPatientById(appt.getPatientId());

                String doctorName = (doctor != null) ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName()
                        : "Doctor";
                String patientName = (patient != null) ? patient.getFirstName() + " " + patient.getLastName()
                        : "Patient";

                // Determine User IDs
                int doctorUserId = controller.NotificationController.getUserIdByRoleRef("DOCTOR", appt.getDoctorId());
                int patientUserId = controller.NotificationController.getUserIdByRoleRef("PATIENT",
                        appt.getPatientId());

                String docMsg = "";
                String patMsg = "";

                if ("SCHEDULED".equalsIgnoreCase(status) || "ACCEPTED".equalsIgnoreCase(status)) {
                    docMsg = "You accepted appointment with " + patientName + " on " + appt.getAppointmentDate();
                    patMsg = "Your appointment with " + doctorName + " has been ACCEPTED for "
                            + appt.getAppointmentDate();
                } else if ("CANCELLED".equalsIgnoreCase(status) || "DECLINED".equalsIgnoreCase(status)) {
                    docMsg = "You declined appointment with " + patientName;
                    patMsg = doctorName + " has DECLINED your appointment request.";
                } else if ("COMPLETED".equalsIgnoreCase(status)) {
                    docMsg = "Appointment with " + patientName + " marked completed.";
                    patMsg = "Your visit with " + doctorName + " is completed. Check your prescription.";
                }

                // Send Targeted Notifications
                if (doctorUserId > 0 && !docMsg.isEmpty())
                    controller.NotificationController.addNotification("DOCTOR", docMsg, doctorUserId);

                if (patientUserId > 0 && !patMsg.isEmpty())
                    controller.NotificationController.addNotification("PATIENT", patMsg, patientUserId);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<AppointmentDetails> getAllAppointmentDetails() {
        List<AppointmentDetails> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.remarks, a.status, a.prescription_image_path, "
                        + "p.first_name AS patient_first_name, p.last_name AS patient_last_name, "
                        + "d.first_name AS doctor_first_name, d.last_name AS doctor_last_name "
                        + "FROM appointments a "
                        + "LEFT JOIN patients p ON a.patient_id = p.patient_id "
                        + "LEFT JOIN doctors d ON a.doctor_id = d.doctor_id ");

        // Filter for Doctor
        if (service.SessionManager.isDoctor()) {
            sql.append("WHERE a.doctor_id = ? ");
        }

        sql.append("ORDER BY a.appointment_date DESC");

        try (Connection con = Connectdb.getConnection()) {
            if (con == null)
                return list;

            try (PreparedStatement pst = con.prepareStatement(sql.toString())) {
                if (service.SessionManager.isDoctor()) {
                    // We need to get the doctor_id from the session
                    // Assuming referenceId in User is the doctor_id
                    int doctorId = service.SessionManager.getUser().getReferenceId();
                    pst.setInt(1, doctorId);
                }

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        list.add(new AppointmentDetails(
                                rs.getInt("id"),
                                rs.getInt("patient_id"),
                                rs.getInt("doctor_id"),
                                rs.getString("patient_first_name") + " " + rs.getString("patient_last_name"),
                                rs.getString("doctor_first_name") + " " + rs.getString("doctor_last_name"),
                                rs.getString("appointment_date"),
                                rs.getString("appointment_time"),
                                rs.getString("remarks"),
                                rs.getString("status"),
                                rs.getString("prescription_image_path")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateAppointment(DoctorAppointment appointment) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, remarks=?, status=?, prescription_image_path=? WHERE id=?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, appointment.getPatientId());
            pst.setInt(2, appointment.getDoctorId());
            pst.setString(3, appointment.getAppointmentDate());
            pst.setString(4, appointment.getAppointmentTime());
            pst.setString(5, appointment.getRemarks());
            pst.setString(6, appointment.getStatus());
            pst.setString(7, appointment.getPrescriptionImagePath());
            pst.setInt(8, appointment.getId());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id=?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DoctorAppointment getAppointmentById(int id) {
        String sql = "SELECT * FROM appointments WHERE id=?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new DoctorAppointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getString("prescription_image_path"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean uploadPrescription(int appointmentId, String imagePath) {
        String sql = "UPDATE appointments SET prescription_image_path=?, status='COMPLETED' WHERE id=?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, imagePath);
            pst.setInt(2, appointmentId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Notify Patient
                DoctorAppointment appt = getAppointmentById(appointmentId);
                if (appt != null) {
                    int patientUserId = controller.NotificationController.getUserIdByRoleRef("PATIENT",
                            appt.getPatientId());
                    if (patientUserId > 0) {
                        controller.NotificationController.addNotification("PATIENT",
                                "Prescription uploaded for appointment ID #" + appointmentId, patientUserId);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
