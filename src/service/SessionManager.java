package service;

import model.User;

public class SessionManager {
   private static User currentUser;

   public static void setUser(User user) {
      currentUser = user;
   }

   public static User getUser() {
      return currentUser;
   }

   public static void logout() {
      currentUser = null;
   }

   public static boolean isLoggedIn() {
      return currentUser != null;
   }

   public static boolean isAdmin() {
      return currentUser != null && currentUser.isAdmin();
   }

   public static boolean isDoctor() {
      return currentUser != null && currentUser.isDoctor();
   }

   public static boolean isPatient() {
      return currentUser != null && currentUser.isPatient();
   }
}
