package hospital.service;

import hospital.model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HospitalService.java
 * ---------------------------------------------------------
 * This class contains 100% of the business logic that existed in the
 * original console application (HospitalManagementSystem05.java).
 * Every method here corresponds 1:1 with a function from the console
 * version. The only change is *how* data is stored internally
 * (a List<Patient> instead of ten parallel arrays of size 100) -- the
 * calculations, conditions, validations, file formats and outcomes are
 * identical.
 *
 * Console function      -> Service method
 * ---------------------------------------------------------
 * signUp()               -> signUp(user, pass)
 * adminLogin()            -> authenticateAdmin(user, pass)
 * facultyLogin()           -> (no server-side check; handled by UI, matches console)
 * addPatient()            -> addPatient(Patient)
 * updatePatient()          -> findById(id) + setters on returned Patient
 * deletePatient()          -> deletePatient(id)
 * displayPatients()         -> getAllPatients()
 * countPatients()           -> countPatients()
 * scheduleAppointment()      -> scheduleAppointment(id, date, time, doctor)
 * viewEmergencyCases()        -> getEmergencyCases()
 * generateReport()          -> generateReport()
 * viewStatistics()          -> getTotalCount() / getEmergencyCount()
 * billing()               -> calculateBill(id, medicine, roomCharge)
 * saveToFile()/loadFromFile()   -> saveToFile()/loadFromFile()
 * searchPatient()           -> searchById/searchByName/searchByDisease
 * ---------------------------------------------------------
 */
public class HospitalService {

    private static final String PATIENTS_FILE = "patients.txt";
    private static final String REPORT_FILE = "hospital_report.txt";
    private static final int DOCTOR_FEE = 1500; // fixed doctor fee, identical to console version

    private final List<Patient> patients = new ArrayList<>();

    // Admin credentials created at sign-up (session based, exactly like the console app,
    // where adminUser/adminPass are plain static fields set fresh each run).
    private String adminUser;
    private String adminPass;

    // ---------------- Sign Up / Login ----------------

    /** Registers new admin credentials for this session. Mirrors console signUp(). */
    public void signUp(String username, String password) {
        this.adminUser = username;
        this.adminPass = password;
    }

    /** Mirrors console adminLogin() credential check. */
    public boolean authenticateAdmin(String username, String password) {
        return adminUser != null && adminUser.equals(username)
                && adminPass != null && adminPass.equals(password);
    }

    // ---------------- Patient CRUD ----------------

    /** Mirrors console addPatient(). Patient must already have all fields populated. */
    public void addPatient(Patient p) {
        patients.add(p);
    }

    /** Mirrors the ID lookup loop used throughout the console version. */
    public Patient findById(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    /** Mirrors console deletePatient(). Returns true if a record was removed. */
    public boolean deletePatient(String id) {
        Patient p = findById(id);
        if (p == null) return false;
        patients.remove(p);
        return true;
    }

    /** Mirrors console displayPatients() -- returns the live list of all patients. */
    public List<Patient> getAllPatients() {
        return patients;
    }

    /** Mirrors console countPatients(). */
    public int countPatients() {
        return patients.size();
    }

    // ---------------- Appointment ----------------

    /** Mirrors console scheduleAppointment(). Returns false if patient ID doesn't exist. */
    public boolean scheduleAppointment(String id, String date, String time, String doctor) {
        Patient p = findById(id);
        if (p == null) return false;
        p.setAppointmentDate(date);
        p.setAppointmentTime(time);
        p.setAppointmentDoctor(doctor);
        return true;
    }

    // ---------------- Emergency & Statistics ----------------

    /** Mirrors console viewEmergencyCases(). */
    public List<Patient> getEmergencyCases() {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.isEmergency()) result.add(p);
        }
        return result;
    }

    /** Mirrors console viewStatistics() total count. */
    public int getTotalCount() {
        return patients.size();
    }

    /** Mirrors console viewStatistics() emergency count. */
    public int getEmergencyCount() {
        int high = 0;
        for (Patient p : patients) if (p.isEmergency()) high++;
        return high;
    }

    // ---------------- Billing ----------------

    /** Simple holder for a computed bill, mirrors the console billing() output values. */
    public static class BillResult {
        public final int roomCharge;
        public final int doctorFee;
        public final int medicine;
        public final int total;

        public BillResult(int roomCharge, int doctorFee, int medicine, int total) {
            this.roomCharge = roomCharge;
            this.doctorFee = doctorFee;
            this.medicine = medicine;
            this.total = total;
        }
    }

    /** Mirrors console billing(). Returns null if the patient ID was not found. */
    public BillResult calculateBill(String id, int medicine, int roomCharge) {
        Patient p = findById(id);
        if (p == null) return null;
        int total = roomCharge + DOCTOR_FEE + medicine;
        return new BillResult(roomCharge, DOCTOR_FEE, medicine, total);
    }

    // ---------------- Report ----------------

    /** Mirrors console generateReport(). Writes hospital_report.txt. Returns true on success. */
    public boolean generateReport() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_FILE))) {
            for (Patient p : patients) {
                bw.write("ID:" + p.getId() + " Name:" + p.getName() + " Age:" + p.getAge()
                        + " Gender:" + p.getGender() + " Disease:" + p.getDisease()
                        + " Doctor:" + p.getDoctor() + " Admission:" + p.getAdmissionDate()
                        + " Room:" + p.getRoom() + " Contact:" + p.getContact()
                        + " Priority:" + p.getPriority() + "\n");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getReportFilePath() {
        return new File(REPORT_FILE).getAbsolutePath();
    }

    // ---------------- File Persistence ----------------

    /** Mirrors console saveToFile(). Writes patients.txt in the exact same CSV format. */
    public boolean saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient p : patients) {
                bw.write(p.toCsv() + "\n");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Mirrors console loadFromFile(). Silently does nothing if the file doesn't exist yet. */
    public void loadFromFile() {
        patients.clear();
        File f = new File(PATIENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                patients.add(Patient.fromCsv(line));
            }
        } catch (Exception e) {
            // Matches console behavior: "No previous data found." -- silently ignored here,
            // the UI layer decides whether to show a message.
        }
    }

    // ---------------- Search ----------------

    /** Mirrors console searchPatient() option 1. */
    public List<Patient> searchById(String id) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) if (p.getId().equals(id)) result.add(p);
        return result;
    }

    /** Mirrors console searchPatient() option 2. */
    public List<Patient> searchByName(String name) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) if (p.getName().equalsIgnoreCase(name)) result.add(p);
        return result;
    }

    /** Mirrors console searchPatient() option 3. */
    public List<Patient> searchByDisease(String disease) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) if (p.getDisease().equalsIgnoreCase(disease)) result.add(p);
        return result;
    }
}
