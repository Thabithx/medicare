package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.Connectdb;
import Model.DoctorAppointment;
import Model.AppointmentDetails;

public class AppointmentController {

    // ✅ CREATE (Add new appointment)
    public boolean addAppointment(DoctorAppointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, remarks, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, appointment.getPatientId());
            pst.setInt(2, appointment.getDoctorId());
            pst.setString(3, appointment.getAppointmentDate());
            pst.setString(4, appointment.getAppointmentTime());
            pst.setString(5, appointment.getRemarks());
            pst.setString(6, appointment.getStatus());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ READ (Get all appointments)
    public List<DoctorAppointment> getAllAppointments() {
        List<DoctorAppointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";

        try (Connection con = Connectdb.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                DoctorAppointment app = new DoctorAppointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("remarks"),
                        rs.getString("status")
                );
                list.add(app);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ READ (Get all appointments WITH patient and doctor names)
    public List<AppointmentDetails> getAllAppointmentDetails() {
        List<AppointmentDetails> list = new ArrayList<>();
        String sql = "SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.remarks, a.status, " +
                     "p.first_name AS patient_first_name, p.last_name AS patient_last_name, " +
                     "d.first_name AS doctor_first_name, d.last_name AS doctor_last_name " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "ORDER BY a.appointment_date DESC";

        try (Connection con = Connectdb.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                AppointmentDetails detail = new AppointmentDetails(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("patient_first_name") + " " + rs.getString("patient_last_name"),
                        rs.getString("doctor_first_name") + " " + rs.getString("doctor_last_name"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("remarks"),
                        rs.getString("status")
                );
                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ UPDATE (Edit existing appointment)
    public boolean updateAppointment(DoctorAppointment appointment) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, remarks=?, status=? WHERE id=?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, appointment.getPatientId());
            pst.setInt(2, appointment.getDoctorId());
            pst.setString(3, appointment.getAppointmentDate());
            pst.setString(4, appointment.getAppointmentTime());
            pst.setString(5, appointment.getRemarks());
            pst.setString(6, appointment.getStatus());
            pst.setInt(7, appointment.getId());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ DELETE
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

    // ✅ Get appointment by ID
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
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}