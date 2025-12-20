package model;

import java.sql.Date;

public class Patient {
   private int id;
   private String firstName;
   private String lastName;
   private String gender;
   private Date dob;
   private String bloodGroup;
   private String phone;
   private String email;
   private String address;
   private String medicalHistory;
   private String medications;
   private String allergies;
   private String profilePicturePath;

   public Patient(int id, String firstName, String lastName, String gender, Date dob, String bloodGroup,
         String phone, String email, String address, String medicalHistory, String medications, String allergies,
         String profilePicturePath) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.gender = gender;
      this.dob = dob;
      this.bloodGroup = bloodGroup;
      this.phone = phone;
      this.email = email;
      this.address = address;
      this.medicalHistory = medicalHistory;
      this.medications = medications;
      this.allergies = allergies;
      this.profilePicturePath = profilePicturePath;
   }

   // Getters
   public int getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getGender() {
      return gender;
   }

   public Date getDob() {
      return dob;
   }

   public String getBloodGroup() {
      return bloodGroup;
   }

   public String getPhone() {
      return phone;
   }

   public String getEmail() {
      return email;
   }

   public String getAddress() {
      return address;
   }

   public String getMedicalHistory() {
      return medicalHistory;
   }

   public String getMedications() {
      return medications;
   }

   public String getCurrentMedications() {
      return medications;
   }

   public String getAllergies() {
      return allergies;
   }

   // Setters
   public void setId(int id) {
      this.id = id;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setGender(String gender) {
      this.gender = gender;
   }

   public void setDob(Date dob) {
      this.dob = dob;
   }

   public void setBloodGroup(String bloodGroup) {
      this.bloodGroup = bloodGroup;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public void setMedicalHistory(String medicalHistory) {
      this.medicalHistory = medicalHistory;
   }

   public void setMedications(String medications) {
      this.medications = medications;
   }

   public void setAllergies(String allergies) {
      this.allergies = allergies;
   }

   public String getProfilePicturePath() {
      return profilePicturePath;
   }

   public void setProfilePicturePath(String profilePicturePath) {
      this.profilePicturePath = profilePicturePath;
   }
}
