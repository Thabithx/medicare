package model;

public class AppointmentDetails extends Appointment {

    private String patientName;
    private String doctorName;

    public AppointmentDetails(int id, int patientId, int doctorId, String patientName,
            String doctorName, String appointmentDate, String appointmentTime,
            String remarks, String status, String prescriptionImagePath) {
        super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status, prescriptionImagePath);
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    public AppointmentDetails(int id, int patientId, int doctorId, String patientName,
            String doctorName, String appointmentDate, String appointmentTime,
            String remarks, String status) {
        this(id, patientId, doctorId, patientName, doctorName, appointmentDate, appointmentTime, remarks, status, null);
    }

    // Getters
    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    @Override
    public String getAppointmentType() {
        return "Appointment with Details";
    }
}