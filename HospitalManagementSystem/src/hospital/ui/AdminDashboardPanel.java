package hospital.ui;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.dialogs.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * AdminDashboardPanel.java
 * ---------------------------------------------------------
 * GUI counterpart of console adminMenu(). Every one of the 11 console
 * menu options has a direct equivalent here, reached through a sidebar
 * instead of typing a number:
 *
 *   1  Add Patient          -> "Add Patient" nav button
 *   2  Update Patient        -> "Update Patient" nav button
 *   3  Delete Patient        -> "Delete Patient" nav button
 *   4  Display All Patients    -> "All Patients" nav button (also the default view)
 *   5  Count Total Patients    -> "Patient Count" nav button
 *   6  Schedule Appointment     -> "Schedule Appointment" nav button
 *   7  View Emergency Cases     -> "Emergency Cases" nav button
 *   8  Generate Report        -> "Generate Report" nav button
 *   9  View Statistics        -> "Statistics" nav button
 *   10 Bill               -> "Billing" nav button
 *   11 Save & Exit          -> "Save & Exit" nav button
 * ---------------------------------------------------------
 */
public class AdminDashboardPanel extends JPanel {

    private final MainFrame frame;
    private final HospitalService service;

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel totalStat;
    private JLabel emergencyStat;
    private JLabel viewTitle;
    private boolean emergencyOnly = false;

    public AdminDashboardPanel(MainFrame frame, HospitalService service) {
        this.frame = frame;
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        refreshTable();
    }

    // ---------------- Sidebar ----------------
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel brandRow = new JPanel();
        brandRow.setLayout(new BoxLayout(brandRow, BoxLayout.X_AXIS));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandRow.setBorder(BorderFactory.createEmptyBorder(0, 20, 24, 0));
        JLabel brandIcon = new JLabel(new VectorIcon(VectorIcon.Type.LOGO, UITheme.PRIMARY, 26));
        JLabel brandText = new JLabel("  HMS Admin");
        brandText.setFont(UITheme.FONT_HEADING);
        brandText.setForeground(Color.WHITE);
        brandRow.add(brandIcon);
        brandRow.add(brandText);

        sidebar.add(brandRow);
        sidebar.add(navButton(VectorIcon.Type.ADD, "Add Patient", () -> onAddPatient()));
        sidebar.add(navButton(VectorIcon.Type.EDIT, "Update Patient", () -> onUpdatePatient(null)));
        sidebar.add(navButton(VectorIcon.Type.DELETE, "Delete Patient", () -> onDeletePatient()));
        sidebar.add(navButton(VectorIcon.Type.LIST, "All Patients", () -> onShowAll()));
        sidebar.add(navButton(VectorIcon.Type.COUNT, "Patient Count", () -> onCount()));
        sidebar.add(navButton(VectorIcon.Type.CALENDAR, "Schedule Appointment", () -> onSchedule(null)));
        sidebar.add(navButton(VectorIcon.Type.ALERT, "Emergency Cases", () -> onEmergencyCases()));
        sidebar.add(navButton(VectorIcon.Type.REPORT, "Generate Report", () -> onGenerateReport()));
        sidebar.add(navButton(VectorIcon.Type.CHART, "Statistics", () -> onStatistics()));
        sidebar.add(navButton(VectorIcon.Type.WALLET, "Billing", () -> onBilling(null)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navButton(VectorIcon.Type.POWER, "Save & Exit", () -> onSaveAndExit(), true));

        return sidebar;
    }

    private interface Action { void run(); }

    private JButton navButton(VectorIcon.Type icon, String text, Action action) {
        return navButton(icon, text, action, false);
    }

    private JButton navButton(VectorIcon.Type icon, String text, Action action, boolean danger) {
        JButton b = new JButton(text);
        Color fg = danger ? new Color(0xFFC9C9) : UITheme.TEXT_ON_DARK;
        b.setIcon(new VectorIcon(icon, fg, 17));
        b.setIconTextGap(12);
        b.setFont(UITheme.FONT_NAV);
        b.setForeground(fg);
        b.setBackground(UITheme.SIDEBAR);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 16));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(UITheme.SIDEBAR_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(UITheme.SIDEBAR); }
        });
        b.addActionListener(e -> action.run());
        return b;
    }

    // ---------------- Content ----------------
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(UITheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel welcome = new JLabel("Welcome, Admin");
        welcome.setFont(UITheme.FONT_TITLE);
        welcome.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Manage patient records, appointments, billing and reports.");
        sub.setFont(UITheme.FONT_SUBTITLE);
        sub.setForeground(new Color(0xDDEFEE));

        JPanel headerText = new JPanel();
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
        headerText.setOpaque(false);
        headerText.add(welcome);
        headerText.add(Box.createVerticalStrut(4));
        headerText.add(sub);

        JPanel stats = new JPanel(new GridLayout(1, 2, 14, 0));
        stats.setOpaque(false);
        totalStat = new JLabel();
        emergencyStat = new JLabel();
        stats.add(statCard(VectorIcon.Type.LIST, "Total Patients", totalStat, UITheme.PRIMARY));
        stats.add(statCard(VectorIcon.Type.ALERT, "Emergency Cases", emergencyStat, UITheme.DANGER));

        GradientPanel header = new GradientPanel(
                UITheme.GRADIENT_BANNER_FROM, UITheme.GRADIENT_BANNER_TO).withArc(18);
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        header.add(headerText, BorderLayout.WEST);
        header.add(stats, BorderLayout.EAST);

        viewTitle = UITheme.heading("All Patients");

        String[] columns = {"ID", "Name", "Age", "Gender", "Disease", "Doctor", "Admission", "Room", "Contact", "Priority"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.getColumnModel().getColumn(9).setCellRenderer(priorityRenderer());
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));

        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(UITheme.CARD);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tableCard.add(viewTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(header, BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        return content;
    }

    private DefaultTableCellRenderer priorityRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                              boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if ("High".equalsIgnoreCase(String.valueOf(value))) {
                    c.setForeground(isSelected ? UITheme.TEXT_DARK : UITheme.DANGER);
                    setFont(getFont().deriveFont(Font.BOLD));
                    setText("HIGH");
                } else {
                    c.setForeground(UITheme.TEXT_DARK);
                    setFont(getFont().deriveFont(Font.PLAIN));
                    setText("Normal");
                }
                return c;
            }
        };
    }

    private JPanel statCard(VectorIcon.Type icon, String title, JLabel valueLabel, Color accent) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 20));

        JLabel iconLabel = new JLabel(new VectorIcon(icon, accent, 22));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel textCol = new JPanel();
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_LABEL);
        t.setForeground(UITheme.TEXT_MUTED);
        valueLabel.setFont(UITheme.FONT_MONO_STAT);
        valueLabel.setForeground(UITheme.TEXT_DARK);
        textCol.add(t);
        textCol.add(valueLabel);

        p.add(iconLabel, BorderLayout.WEST);
        p.add(textCol, BorderLayout.CENTER);
        return p;
    }

    // ---------------- Data refresh ----------------
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = emergencyOnly ? service.getEmergencyCases() : service.getAllPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(),
                    p.getDisease(), p.getDoctor(), p.getAdmissionDate(), p.getRoom(),
                    p.getContact(), p.getPriority()});
        }
        totalStat.setText(String.valueOf(service.getTotalCount()));
        emergencyStat.setText(String.valueOf(service.getEmergencyCount()));
        viewTitle.setText(emergencyOnly ? "Emergency Cases" : "All Patients");
    }

    private String selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return String.valueOf(tableModel.getValueAt(row, 0));
    }

    // ---------------- Menu actions (mirror console adminMenu() cases) ----------------

    private void onAddPatient() {
        AddPatientDialog dlg = new AddPatientDialog(frame, service);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            emergencyOnly = false;
            refreshTable();
            toast("Patient added successfully!");
        }
    }

    private void onUpdatePatient(String presetId) {
        String id = presetId != null ? presetId : selectedId();
        UpdatePatientDialog dlg = new UpdatePatientDialog(frame, service, id);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            refreshTable();
            toast("Patient updated successfully!");
        }
    }

    private void onDeletePatient() {
        String preset = selectedId();
        String id = JOptionPane.showInputDialog(this, "Enter Patient ID to delete:",
                preset != null ? preset : "");
        if (id == null) return; // cancelled
        id = id.trim();
        if (id.isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient \"" + id + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = service.deletePatient(id);
        if (ok) {
            service.saveToFile();
            refreshTable();
            toast("Patient deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onShowAll() {
        emergencyOnly = false;
        refreshTable();
    }

    private void onCount() {
        JOptionPane.showMessageDialog(this, "Total Patients: " + service.countPatients(),
                "Patient Count", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onSchedule(String presetId) {
        String id = presetId != null ? presetId : selectedId();
        new ScheduleAppointmentDialog(frame, service, id).setVisible(true);
    }

    private void onEmergencyCases() {
        emergencyOnly = true;
        refreshTable();
        if (service.getEmergencyCount() == 0) {
            JOptionPane.showMessageDialog(this, "No high priority cases found.",
                    "Emergency Cases", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onGenerateReport() {
        boolean ok = service.generateReport();
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Report generated as hospital_report.txt\n\nLocation: " + service.getReportFilePath(),
                    "Report Generated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error generating report", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onStatistics() {
        JOptionPane.showMessageDialog(this,
                "Total Patients: " + service.getTotalCount() + "\nEmergency Cases: " + service.getEmergencyCount(),
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onBilling(String presetId) {
        String id = presetId != null ? presetId : selectedId();
        new BillingDialog(frame, service, id).setVisible(true);
    }

    private void onSaveAndExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Save all data and exit the application?",
                "Save & Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            service.saveToFile();
            JOptionPane.showMessageDialog(this, "Data saved. Exiting system.");
            System.exit(0);
        }
    }

    private void toast(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
