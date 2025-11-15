package Model;

public class AppointmentStatus extends Appointment {

    public AppointmentStatus(int id, int patientId, int doctorId, String appointmentDate, 
                            String appointmentTime, String status, String remarks) {
        super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status);
    }

    @Override
    public String getAppointmentType() {
        return "Status Tracking Appointment";
    }

    public String getStatusColor() {
        switch (this.status) {
            case "SCHEDULED": return "Blue";
            case "COMPLETED": return "Green";
            case "CANCELED": return "Red";
            case "DELAYED": return "Orange";
            default: return "Gray";
        }
    }

    public String getStatusIcon() {
        switch (this.status) {
            case "SCHEDULED": return "📅";
            case "COMPLETED": return "✅";
            case "CANCELED": return "❌";
            case "DELAYED": return "⏰";
            default: return "❓";
        }
    }
}