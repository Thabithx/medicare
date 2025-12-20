package controller;

import db.Connectdb;
import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    public static boolean addPatient(String firstName, String lastName, String gender, String dob,
            String bloodGroup, String phone, String email, String address, String password) {
        return addPatient(firstName, lastName, gender, dob, bloodGroup, phone, email, address, "", "", "", password);
    }

    public static boolean addPatient(String firstName, String lastName, String gender, String dob,
            String bloodGroup, String phone, String email, String address,
            String medicalHistory, String medications, String allergies, String password) {

        String query = "INSERT INTO Patients (first_name, last_name, gender, dob, blood_group, phone, email, address, medical_history, medications, allergies) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { // Request keys

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

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                // Get Generated Patient ID
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int patientId = generatedKeys.getInt(1);
                        // Create User Account automatically
                        boolean userCreated = service.AuthService.register(email, password, "PATIENT", patientId);
                        if (!userCreated) {
                            System.err.println("Warning: User account could not be created for patient " + patientId);
                            deletePatient(patientId);
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
        }
    }

    // Fetch all patients
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patients";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getDate("dob"),
                        rs.getString("blood_group"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("medical_history"),
                        rs.getString("medications"),
                        rs.getString("allergies"),
                        rs.getString("profile_picture_path"));
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Delete patient by ID
    public static boolean deletePatient(int id) {
        Patient p = getPatientById(id);
        if (p == null)
            return false;

        String query = "DELETE FROM Patients WHERE patient_id = ?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                deleteUserByEmail(p.getEmail());
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void deleteUserByEmail(String email) {
        String sql = "DELETE FROM Users WHERE email = ?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update patient
    public static boolean updatePatient(int id, String firstName, String lastName, String gender, String dob,
            String bloodGroup, String phone, String email, String address,
            String medicalHistory, String medications, String allergies, String profilePicturePath) {
        String query = "UPDATE Patients SET first_name = ?, last_name = ?, gender = ?, dob = ?, blood_group = ?, phone = ?, email = ?, address = ?, medical_history = ?, medications = ?, allergies = ?, profile_picture_path = ? "
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
            pst.setString(12, profilePicturePath);
            pst.setInt(13, id);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Patient by ID
    public static Patient getPatientById(int id) {
        String query = "SELECT * FROM Patients WHERE patient_id = ?";
        try (Connection con = Connectdb.getConnection();
                PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getInt("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("gender"),
                            rs.getDate("dob"),
                            rs.getString("blood_group"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("address"),
                            rs.getString("medical_history"),
                            rs.getString("medications"),
                            rs.getString("allergies"),
                            rs.getString("profile_picture_path"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
