package controller;

import db.Connectdb;
import model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {

    public static boolean addDoctor(String firstName, String lastName, String gender,
            String address, String dob, String phone,
            String specialty, String qualification,
            String schedule, String timeslot, String email, String password) {

        String sql = "INSERT INTO Doctors " +
                "(first_name, last_name, gender, address, dob, phone, specialty, qualification, schedule, timeslot) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = Connectdb.getConnection();
        if (con == null) {
            System.err.println("❌ DB Connection failed");
            return false;
        }

        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, address);

            // ✅ Convert String → SQL Date
            pst.setDate(5, Date.valueOf(dob));

            pst.setString(6, phone);
            pst.setString(7, specialty);
            pst.setString(8, qualification);
            pst.setString(9, schedule);
            pst.setString(10, timeslot);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                // Get Generated Doctor ID
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int doctorId = generatedKeys.getInt(1);
                        // Create User Account automatically
                        boolean userCreated = service.AuthService.register(email, password, "DOCTOR", doctorId);
                        if (!userCreated) {
                            System.err.println("Warning: User account not created for doctor " + doctorId);
                            // Rollback
                            deleteDoctor(doctorId);
                            return false;
                        }
                        return true;
                    }
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static List<Doctor> getAllDoctors() {

        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctors";

        Connection con = Connectdb.getConnection();
        if (con == null)
            return doctors;

        try (PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getDate("dob").toString(),
                        rs.getString("phone"),
                        rs.getString("specialty"),
                        rs.getString("qualification"),
                        rs.getString("schedule"),
                        rs.getString("timeslot"),
                        "ACTIVE", // Default status, as DB column is missing
                        rs.getString("profile_picture_path"));
                doctors.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }

        return doctors;
    }

    public static boolean deleteDoctor(int id) {
        String sql = "DELETE FROM Doctors WHERE doctor_id = ?";
        Connection con = Connectdb.getConnection();
        if (con == null)
            return false;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                deleteUserByReference(id, "DOCTOR");
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private static void deleteUserByReference(int refId, String role) {
        String sql = "DELETE FROM Users WHERE reference_id = ? AND role = ?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, refId);
            pst.setString(2, role);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateDoctor(int id, String firstName, String lastName, String gender,
            String address, String dob, String phone,
            String specialty, String qualification,
            String schedule, String timeslot) {

        String sql = "UPDATE Doctors SET first_name=?, last_name=?, gender=?, address=?, dob=?, phone=?, " +
                "specialty=?, qualification=?, schedule=?, timeslot=? WHERE doctor_id=?";

        Connection con = Connectdb.getConnection();
        if (con == null)
            return false;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, address);
            pst.setDate(5, Date.valueOf(dob));
            pst.setString(6, phone);
            pst.setString(7, specialty);
            pst.setString(8, qualification);
            pst.setString(9, schedule);
            pst.setString(10, timeslot);
            pst.setInt(11, id);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static Doctor recommendDoctor(String specialty) {
        String sql = "SELECT * FROM Doctors WHERE specialty LIKE ? ORDER BY RAND() LIMIT 1";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, "%" + specialty + "%");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Handle potential null dates safely
                String dobStr = rs.getDate("dob") != null ? rs.getDate("dob").toString() : "";

                return new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        dobStr,
                        rs.getString("phone"),
                        rs.getString("specialty"),
                        rs.getString("qualification"),
                        rs.getString("schedule"),
                        rs.getString("timeslot"),
                        "ACTIVE",
                        rs.getString("profile_picture_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM Doctors WHERE doctor_id = ?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String dobStr = rs.getDate("dob") != null ? rs.getDate("dob").toString() : "";
                return new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        dobStr,
                        rs.getString("phone"),
                        rs.getString("specialty"),
                        rs.getString("qualification"),
                        rs.getString("schedule"),
                        rs.getString("timeslot"),
                        "ACTIVE",
                        rs.getString("profile_picture_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
