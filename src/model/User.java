package model;

public class User {
   private int userId;
   private String email;
   private String password;
   private String role; // "ADMIN", "DOCTOR", "PATIENT"
   private int referenceId;

   public User(int userId, String email, String password, String role, int referenceId) {
      this.userId = userId;
      this.email = email;
      this.password = password;
      this.role = role;
      this.referenceId = referenceId;
   }

   // Getters and Setters
   public int getUserId() {
      return userId;
   }

   public String getEmail() {
      return email;
   }

   public String getRole() {
      return role;
   }

   public int getReferenceId() {
      return referenceId;
   }

   public boolean isAdmin() {
      return "ADMIN".equalsIgnoreCase(role);
   }

   public boolean isDoctor() {
      return "DOCTOR".equalsIgnoreCase(role);
   }

   public boolean isPatient() {
      return "PATIENT".equalsIgnoreCase(role);
   }
}
