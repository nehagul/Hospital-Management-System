# 🏥 Hospital Management System

A complete Hospital Management System built with Java Swing. Features patient management, appointment scheduling, automated billing, emergency case tracking, and role-based access (Admin/Faculty) with CSV data persistence.

---

## ✨ Features

### Admin Dashboard
- ➕ Add, Update, Delete Patients
- 📋 View All Patients
- 📊 Patient Count & Statistics
- 🏥 Schedule Appointments
- 🚨 View Emergency Cases
- 💰 Generate Patient Bills
- 📄 Generate Reports
- 💾 Save & Exit

### Faculty Dashboard (Read-Only)
- 👀 View Patient Records
- 🔍 Search Patients (ID, Name, Disease)
- 🚨 View Emergency Cases
- 📊 View Statistics

---

## 🛠️ Tech Stack

- Java 8+
- Swing (GUI Framework)
- File I/O for Data Persistence
- No External Libraries (Pure Java SE)

---

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher

### Run the Application
```bash
# Using JAR
java -jar HospitalManagementSystem.jar

# Or compile from source
javac -d out src/hospital/**/*.java
java -cp out hospital.Main
📁 Project Structure
text
HospitalManagementSystem/
├── src/hospital/
│   ├── Main.java
│   ├── model/Patient.java
│   ├── service/HospitalService.java
│   └── ui/
│       ├── UITheme.java
│       ├── MainFrame.java
│       ├── LoginPanel.java
│       ├── AdminDashboardPanel.java
│       ├── FacultyDashboardPanel.java
│       └── dialogs/
├── patients.txt
└── hospital_report.txt
🔑 Login Guide
First Time: Create Admin account on Sign Up screen

Admin Login: Use created credentials

Faculty Login: Any credentials (read-only access)

💾 Data Storage
patients.txt — Main database (CSV format)

hospital_report.txt — Generated reports

🤝 Contributing
Fork the repository

Create feature branch (git checkout -b feature/AmazingFeature)

Commit changes (git commit -m 'Add feature')

Push to branch (git push origin feature/AmazingFeature)

Open Pull Request

👨‍💻 Author
Neha Gul


