package model;

public class AppointmentStatus extends Appointment {

    public AppointmentStatus(int id, int patientId, int doctorId, String appointmentDate,
            String appointmentTime, String status, String remarks, String prescriptionImagePath) {
        super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status, prescriptionImagePath);
    }

    public AppointmentStatus(int id, int patientId, int doctorId, String appointmentDate,
            String appointmentTime, String status, String remarks) {
        this(id, patientId, doctorId, appointmentDate, appointmentTime, status, remarks, null);
    }

    @Override
    public String getAppointmentType() {
        return "Status Tracking Appointment";
    }

    public String getStatusColor() {
        switch (this.status) {
            case "SCHEDULED":
                return "Blue";
            case "COMPLETED":
                return "Green";
            case "CANCELED":
                return "Red";
            case "DELAYED":
                return "Orange";
            default:
                return "Gray";
        }
    }

    public String getStatusIcon() {
        switch (this.status) {
            case "SCHEDULED":
                return "📅";
            case "COMPLETED":
                return "✅";
            case "CANCELED":
                return "❌";
            case "DELAYED":
                return "⏰";
            default:
                return "❓";
        }
    }
}