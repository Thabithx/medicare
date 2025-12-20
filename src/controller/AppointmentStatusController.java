package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.Connectdb;
import model.AppointmentStatus;

public class AppointmentStatusController {

    public boolean updateAppointmentStatus(int appointmentId, String newStatus, String notes) {
        String sql = "UPDATE appointments SET status=?, remarks=? WHERE id=?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, newStatus);
            pst.setString(2, notes);
            pst.setInt(3, appointmentId);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AppointmentStatus> getAllAppointmentStatuses() {
        List<AppointmentStatus> list = new ArrayList<>();
        String sql = "SELECT id, patient_id, doctor_id, appointment_date, appointment_time, status, remarks " +
                     "FROM appointments ORDER BY appointment_date DESC";

        try (Connection con = Connectdb.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                AppointmentStatus status = new AppointmentStatus(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("status"),
                        rs.getString("remarks")
                );
                list.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public AppointmentStatus getAppointmentStatusById(int id) {
        String sql = "SELECT id, patient_id, doctor_id, appointment_date, appointment_time, status, remarks " +
                     "FROM appointments WHERE id=?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new AppointmentStatus(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("status"),
                        rs.getString("remarks")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAppointmentCountByStatus(String status) {
        String sql = "SELECT COUNT(*) as count FROM appointments WHERE status=?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, status);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}