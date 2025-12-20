package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMigration {

   public static void updateDatabaseSchema() {
      try (Connection con = Connectdb.getConnection()) {
         if (con == null)
            return;

         DatabaseMetaData meta = con.getMetaData();

         // Check and add prescription_image_path column
         ResultSet rs = meta.getColumns(null, null, "appointments", "prescription_image_path");

         if (!rs.next()) {
            System.out.println("⚠️ Column 'prescription_image_path' not found. Adding it now...");
            try (Statement stmt = con.createStatement()) {
               String sql = "ALTER TABLE appointments ADD COLUMN prescription_image_path VARCHAR(255)";
               stmt.execute(sql);
               System.out.println("✅ Column 'prescription_image_path' added successfully.");
            }
         } else {
            System.out.println("✅ Column 'prescription_image_path' exists.");
         }

         // Check and create Notifications table
         ResultSet tables = meta.getTables(null, null, "Notifications", null);
         if (!tables.next()) {
            System.out.println("⚠️ Table 'Notifications' not found. Creating it now...");
            try (Statement stmt = con.createStatement()) {
               String createTableSQL = "CREATE TABLE Notifications (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "user_role VARCHAR(20) NOT NULL, " +
                     "message TEXT NOT NULL, " +
                     "target_user_id INT, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "INDEX idx_target_user_id (target_user_id), " +
                     "INDEX idx_user_role (user_role)" +
                     ")";
               stmt.execute(createTableSQL);
               System.out.println("✅ Table 'Notifications' created successfully.");
            }
         } else {
            System.out.println("✅ Table 'Notifications' exists.");
         }

         System.out.println("✅ Database schema is up to date.");
      } catch (SQLException e) {
         System.err.println("❌ Database migration failed:");
         e.printStackTrace();
      }
   }

   // Main method for manual execution if needed
   public static void main(String[] args) {
      updateDatabaseSchema();
   }
}
