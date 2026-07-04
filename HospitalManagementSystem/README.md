# Hospital Management System — GUI Edition

A full Swing GUI conversion of the original console-based
`HospitalManagementSystem05.java`. Every feature from the console menus
is preserved; only the interface changed.

## Project Structure

```
HospitalManagementSystem/
├── HospitalManagementSystem.jar     (prebuilt, ready to run)
├── README.md
└── src/
    └── hospital/
        ├── Main.java                    entry point
        ├── model/
        │   └── Patient.java              patient data object
        ├── service/
        │   └── HospitalService.java       all business logic (CRUD, billing,
        │                                   search, reports, file I/O)
        └── ui/
            ├── UITheme.java              colors, fonts, shared styling
            ├── RoundedButton.java         custom flat/rounded button
            ├── MainFrame.java            top-level window, screen navigation
            ├── SignUpPanel.java           admin sign-up screen
            ├── LoginPanel.java            admin / faculty login screen
            ├── AdminDashboardPanel.java     admin sidebar + dashboard
            ├── FacultyDashboardPanel.java    faculty sidebar + dashboard
            └── dialogs/
                ├── AddPatientDialog.java
                ├── UpdatePatientDialog.java
                ├── ScheduleAppointmentDialog.java
                ├── BillingDialog.java
                └── SearchDialog.java
```

## How to Compile and Run

**Requirements:** JDK 8 or later (tested on JDK 21). No external
libraries — pure Java SE / Swing.

### Option A — Run the prebuilt jar
```
java -jar HospitalManagementSystem.jar
```

### Option B — Compile from source
```
cd HospitalManagementSystem
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out hospital.Main
```

### Option C — Rebuild the jar yourself
```
cd HospitalManagementSystem
find src -name "*.java" > sources.txt
javac -d out @sources.txt
jar cfe HospitalManagementSystem.jar hospital.Main -C out .
java -jar HospitalManagementSystem.jar
```

Data files (`patients.txt`, `hospital_report.txt`) are created in the
directory you run the app from, exactly like the console version.

## Console → GUI Feature Map

| Console function/menu item      | GUI equivalent                                   |
|----------------------------------|---------------------------------------------------|
| `showTitle()`                    | Branding on the Sign Up screen                     |
| `signUp()`                       | **Sign Up** screen (first screen shown)             |
| `login()` (menu 1/2)              | **Login** screen with Admin/Faculty toggle           |
| `adminLogin()`                    | Admin tab credential check on Login screen           |
| `facultyLogin()`                   | Faculty tab (no credential check, same as console)      |
| Admin menu 1 — Add Patient          | Sidebar → **Add Patient** dialog                   |
| Admin menu 2 — Update Patient        | Sidebar → **Update Patient** dialog (find by ID, then edit) |
| Admin menu 3 — Delete Patient        | Sidebar → **Delete Patient** (ID prompt + confirm)      |
| Admin menu 4 — Display All Patients    | Sidebar → **All Patients** (also the default table view)  |
| Admin menu 5 — Count Total Patients    | Sidebar → **Patient Count**                        |
| Admin menu 6 — Schedule Appointment    | Sidebar → **Schedule Appointment** dialog             |
| Admin menu 7 — View Emergency Cases     | Sidebar → **Emergency Cases** (filters main table)       |
| Admin menu 8 — Generate Report        | Sidebar → **Generate Report** (writes `hospital_report.txt`) |
| Admin menu 9 — View Statistics        | Sidebar → **Statistics**                          |
| Admin menu 10 — Bill              | Sidebar → **Billing** dialog                       |
| Admin menu 11 — Save & Exit          | Sidebar → **Save & Exit**                          |
| Faculty menu 1 — View Patients        | Sidebar → **All Patients**                          |
| Faculty menu 2 — Search Patient        | Sidebar → **Search Patient** (ID / Name / Disease)      |
| Faculty menu 3 — View Emergency Cases    | Sidebar → **Emergency Cases**                        |
| Faculty menu 4 — View Statistics       | Sidebar → **Statistics**                           |
| Faculty menu 5 — Back to Login        | Sidebar → **Back to Login**                         |
| `patients.txt` CSV format           | Unchanged — same file, same column order              |
| `hospital_report.txt`              | Unchanged — same file, same content format             |
| Billing formula (`room + 1500 + medicine`) | Unchanged                                    |
| Emergency priority `H`/`N` → `High`/`Normal` | Unchanged                                   |

## Design Decisions

- **Domain-driven theme:** a clinical teal (`#0E7C7B`) as the primary
  color, paired with a deep navy sidebar and a soft off-white
  background — evokes a hospital/clinical dashboard rather than a
  generic business app. Emergency/high-priority items are called out
  in red throughout (table rows, stat cards, nav icon) so critical
  cases are impossible to miss, mirroring the console's `!!! High !!!`
  emphasis.
- **Sidebar navigation** replaces numbered text menus. Each console
  menu item became exactly one sidebar action, so nothing was added or
  removed — only the input method changed (click vs. typing a number).
- **Live stat cards** (Total Patients / Emergency Cases) sit at the top
  of both dashboards for at-a-glance status, backed by the same
  `countPatients()` / emergency-count logic as the console.
- **Tables replace ASCII-art tables.** The box-drawing character tables
  from `displayPatients()` are now a real `JTable`, sortable-ready,
  with the same columns in the same order, and a bold red "⚠ High"
  badge for emergency rows in place of the console's `!!! High !!!` text.
- **Dialogs for data entry** (Add / Update / Schedule / Bill / Search)
  keep each console prompt as a labeled field, with the same fixed
  values (doctor fee = Rs. 1500) and the same validation gaps closed
  gracefully — e.g., non-numeric age now shows an inline error instead
  of crashing with `NumberFormatException`, and duplicate IDs are now
  caught before insert (console previously allowed silent duplicates).
- **`HospitalService`** is a straight extraction of every console
  function into one class with no UI dependencies, so the exact same
  calculations, conditions and file formats are reused by every
  screen — nothing was "reimagined," only relocated and made testable.
