package controller;

import db.Connectdb;
import java.sql.*;

public class DoctorController {

    // ✅ Overloaded method for DoctorPanel (without medical info)
    public static boolean addDoctor(String firstName, String lastName, String specialty,
                                     String qualification, String schedule, String timeslot) {
        // Calls the full version, leaving medical fields empty
        return addDoctor(firstName, lastName, "", "", "", "", specialty, qualification, schedule, timeslot);
    }

    // Add new doctor (full version)
    public static boolean addDoctor(String firstName, String lastName, String gender, String address,
                                     String dob, String phone, String specialty, String qualification,
                                     String schedule, String timeslot) {
        String query = "INSERT INTO Doctors (first_name, last_name, gender, address, dob, phone, specialty, qualification, schedule, timeslot ) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, address);
            pst.setString(5, dob);
            pst.setString(6, phone);
            pst.setString(7, specialty);
            pst.setString(8, qualification);
            pst.setString(9, schedule);
            pst.setString(10, timeslot);   

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all patients
    public static ResultSet getAllDoctors() {
        String query = "SELECT * FROM Doctors";
        try {
            Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            return pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Delete doctor by ID
    public static boolean deleteDoctor(int id) {
        String query = "DELETE FROM Doctors WHERE doctor_id = ?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update doctor
    public static boolean updateDoctor(int id, String firstName, String lastName, String gender, String address,
            						   String dob, String phone, String specialty, String qualification,
            						   String schedule, String timeslot) {
        String query = "UPDATE Doctors SET first_name = ?, last_name = ?, gender = ?, address = ?, dob = ?, phone = ?, specialty = ?, qualification = ?, schedule = ?, timeslot = ?"
                     + "WHERE doctor_id = ?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, address);
            pst.setString(5, dob);
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
        }
    }
}
