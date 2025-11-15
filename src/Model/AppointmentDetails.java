package Model;

/**
 * AppointmentDetails - DTO (Data Transfer Object) for displaying appointment information
 * with patient and doctor names instead of just IDs.
 * Used for view layer to show human-readable data.
 */
public class AppointmentDetails extends Appointment {

    private String patientName;
    private String doctorName;

    public AppointmentDetails(int id, int patientId, int doctorId, String patientName, 
                             String doctorName, String appointmentDate, String appointmentTime, 
                             String remarks, String status) {
        super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status);
        this.patientName = patientName;
        this.doctorName = doctorName;
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