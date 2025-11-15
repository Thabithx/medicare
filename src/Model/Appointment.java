package Model;

public abstract class Appointment {
    protected int id;
    protected int patientId;
    protected int doctorId;
    protected String appointmentDate;
    protected String appointmentTime;
    protected String remarks;
    protected String status;

    public Appointment(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime, String remarks, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.remarks = remarks;
        this.status = status;
    }

    // Abstract method for polymorphism demonstration
    public abstract String getAppointmentType();

    // Getters and setters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getRemarks() { return remarks; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public void setStatus(String status) { this.status = status; }
}
