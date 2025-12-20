package model;

// Model class for Doctor Appointment
public class DoctorAppointment extends Appointment {

   public DoctorAppointment(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime,
         String remarks, String status, String prescriptionImagePath) {
      super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status, prescriptionImagePath);
   }

   // Constructor for backward compatibility (optional but good for potential
   // misses)
   public DoctorAppointment(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime,
         String remarks, String status) {
      super(id, patientId, doctorId, appointmentDate, appointmentTime, remarks, status, null);
   }

   @Override
   public String getAppointmentType() {
      return "Doctor Appointment";
   }
}
