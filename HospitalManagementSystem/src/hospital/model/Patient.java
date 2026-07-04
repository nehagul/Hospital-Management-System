package hospital.model;

/**
 * Patient.java
 * ---------------------------------------------------------
 * Plain data model representing a single patient record.
 * This replaces the parallel arrays (patientID[], patientName[], ...)
 * used in the original console version with a single cohesive object,
 * while keeping exactly the same fields and the same CSV persistence
 * format used by the console application (patients.txt).
 * ---------------------------------------------------------
 */
public class Patient {

    private String id;
    private String name;
    private int age;
    private String gender;
    private String disease;
    private String doctor;
    private String admissionDate;
    private String room;
    private String contact;
    private String priority;        // "High" or "Normal"

    // Appointment info (kept only in memory, exactly like the console
    // version: appointment arrays were never written to patients.txt,
    // so they are naturally reset whenever data is reloaded from disk).
    private String appointmentDate = "";
    private String appointmentTime = "";
    private String appointmentDoctor = "";

    public Patient(String id, String name, int age, String gender, String disease,
                   String doctor, String admissionDate, String room, String contact,
                   String priority) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.doctor = doctor;
        this.admissionDate = admissionDate;
        this.room = room;
        this.contact = contact;
        this.priority = priority;
    }

    // ---------------- Getters & Setters ----------------
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(String admissionDate) { this.admissionDate = admissionDate; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getAppointmentDoctor() { return appointmentDoctor; }
    public void setAppointmentDoctor(String appointmentDoctor) { this.appointmentDoctor = appointmentDoctor; }

    public boolean isEmergency() {
        return priority != null && priority.equalsIgnoreCase("High");
    }

    /** Serializes this patient to the exact CSV line format used by the console app. */
    public String toCsv() {
        return id + "," + name + "," + age + "," + gender + "," + disease + "," + doctor + ","
                + admissionDate + "," + room + "," + contact + "," + priority;
    }

    /** Parses a CSV line (as written by toCsv) back into a Patient object. */
    public static Patient fromCsv(String line) {
        String[] d = line.split(",");
        return new Patient(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4], d[5], d[6], d[7], d[8], d[9]);
    }
}
