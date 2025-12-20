package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Connectdb;

public class NotificationController {

   public static void addNotification(String role, String message, int targetUserId) {
      String sql = "INSERT INTO Notifications (user_role, message, target_user_id) VALUES (?, ?, ?)";
      try (Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
         pst.setString(1, role);
         pst.setString(2, message);
         if (targetUserId > 0)
            pst.setInt(3, targetUserId);
         else
            pst.setNull(3, java.sql.Types.INTEGER);
         pst.executeUpdate();
      } catch (SQLException e) {
         System.err.println("[NOTIF-ERROR] Failed to insert notification: " + e.getMessage());
         e.printStackTrace();
      }
   }

   // Overload for backward compatibility
   public static void addNotification(String role, String message) {
      addNotification(role, message, 0);
   }

   public static List<String> getNotifications(String role, int currentUserId) {
      List<String> list = new ArrayList<>();
      String sql = "SELECT message, created_at FROM Notifications " +
            "WHERE (user_role = 'All') " +
            "OR (user_role = ? AND target_user_id IS NULL) " +
            "OR (target_user_id = ?) " +
            "ORDER BY created_at DESC LIMIT 20";

      try (Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
         pst.setString(1, role);
         pst.setInt(2, currentUserId);
         ResultSet rs = pst.executeQuery();
         while (rs.next()) {
            list.add(rs.getString("created_at") + ": " + rs.getString("message"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return list;
   }

   // Overload for backward compatibility
   public static List<String> getNotifications(String role) {
      return getNotifications(role, 0);
   }

   public static int getUserIdByRoleRef(String role, int refId) {
      String sql = "SELECT user_id FROM Users WHERE role = ? AND reference_id = ?";
      try (Connection con = Connectdb.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
         pst.setString(1, role);
         pst.setInt(2, refId);
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
            return rs.getInt("user_id");
         } else {
            System.err.println("[NOTIF-WARN] No user found for role=" + role + ", reference_id=" + refId);
         }
      } catch (SQLException e) {
         System.err.println("[NOTIF-ERROR] Database error in getUserIdByRoleRef: " + e.getMessage());
         e.printStackTrace();
      }
      return 0;
   }
}
