package controller;

import db.Connectdb;
import java.sql.*;

public class PatientController {

    // ✅ Overloaded method for AddPatientPanel (without medical info)
    public static boolean addPatient(String firstName, String lastName, String gender, String dob,
                                     String bloodGroup, String phone, String email, String address) {
        // Calls the full version, leaving medical fields empty
        return addPatient(firstName, lastName, gender, dob, bloodGroup, phone, email, address, "", "", "");
    }

    // Add new patient (full version)
    public static boolean addPatient(String firstName, String lastName, String gender, String dob,
                                     String bloodGroup, String phone, String email, String address,
                                     String medicalHistory, String medications, String allergies) {
        String query = "INSERT INTO Patients (first_name, last_name, gender, d_o_b, blood_group, phone_num, email, address, medical_history, current_medications, allergies) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, dob);
            pst.setString(5, bloodGroup);
            pst.setString(6, phone);
            pst.setString(7, email);
            pst.setString(8, address);
            pst.setString(9, medicalHistory);
            pst.setString(10, medications);
            pst.setString(11, allergies);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all patients
    public static ResultSet getAllPatients() {
        String query = "SELECT * FROM Patients";
        try {
            Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            return pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Delete patient by ID
    public static boolean deletePatient(int id) {
        String query = "DELETE FROM Patients WHERE patient_id = ?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update patient
    public static boolean updatePatient(int id, String firstName, String lastName, String gender, String dob,
                                        String bloodGroup, String phone, String email, String address,
                                        String medicalHistory, String medications, String allergies) {
        String query = "UPDATE Patients SET first_name = ?, last_name = ?, gender = ?, d_o_b = ?, blood_group = ?, phone_num = ?, email = ?, address = ?, medical_history = ?, current_medications = ?, allergies = ? "
                     + "WHERE patient_id = ?";
        try (Connection con = Connectdb.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setString(4, dob);
            pst.setString(5, bloodGroup);
            pst.setString(6, phone);
            pst.setString(7, email);
            pst.setString(8, address);
            pst.setString(9, medicalHistory);
            pst.setString(10, medications);
            pst.setString(11, allergies);
            pst.setInt(12, id);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
