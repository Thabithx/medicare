package model;

public class Doctor {

    private int id;
    private String firstName, lastName, gender, address, dob, phone;
    private String specialty, qualification, schedule, timeslot, status;
    private String profilePicturePath;

    public Doctor(int id, String firstName, String lastName, String gender,
            String address, String dob, String phone,
            String specialty, String qualification,
            String schedule, String timeslot, String status, String profilePicturePath) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
        this.specialty = specialty;
        this.qualification = qualification;
        this.schedule = schedule;
        this.timeslot = timeslot;
        this.status = status;
        this.profilePicturePath = profilePicturePath;
    }

    // getters and setters
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

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getStatus() {
        return status;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
