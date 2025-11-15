package Model;

public class DoctorAppointment extends Appointment {

    public DoctorAppointment(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime, String remarks, String status) {
        super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status);
    }

    @Override
    public String getAppointmentType() {
        return "Doctor Appointment";
    }

    // Example polymorphism use
    public String summary() {
        return "Appointment with Doctor ID: " + doctorId + " on " + appointmentDate + " at " + appointmentTime;
    }
}
