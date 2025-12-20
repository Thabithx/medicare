package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.Connectdb;
import model.User;

public class AuthService {

   public static User login(String email, String password) {
      String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
      try (Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

         pst.setString(1, email);
         pst.setString(2, password); 
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
            return new User(
                  rs.getInt("user_id"),
                  rs.getString("email"),
                  rs.getString("password"),
                  rs.getString("role"),
                  rs.getInt("reference_id"));
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null; 
   }

   public static boolean register(String email, String password, String role, int referenceId) {
      String sql = "INSERT INTO Users (email, password, role, reference_id) VALUES (?, ?, ?, ?)";
      try (Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

         pst.setString(1, email);
         pst.setString(2, password);
         pst.setString(3, role);
         pst.setInt(4, referenceId);

         return pst.executeUpdate() > 0;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }
}
