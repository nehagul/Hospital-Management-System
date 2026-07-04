package hospital.ui;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.ui.dialogs.SearchDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * FacultyDashboardPanel.java
 * ---------------------------------------------------------
 * GUI counterpart of console facultyMenu(). Faculty is read-only, just
 * like the console version:
 *
 *   1  View Patients      -> "All Patients" nav button (default view)
 *   2  Search Patient      -> "Search Patient" nav button
 *   3  View Emergency Cases  -> "Emergency Cases" nav button
 *   4  View Statistics     -> "Statistics" nav button
 *   5  Back to Login      -> "Back to Login" nav button
 * ---------------------------------------------------------
 */
public class FacultyDashboardPanel extends JPanel {

    private final MainFrame frame;
    private final HospitalService service;

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel totalStat;
    private JLabel emergencyStat;
    private JLabel viewTitle;
    private boolean emergencyOnly = false;

    public FacultyDashboardPanel(MainFrame frame, HospitalService service) {
        this.frame = frame;
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        refreshTable();
    }

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
        JLabel brandText = new JLabel("  HMS Faculty");
        brandText.setFont(UITheme.FONT_HEADING);
        brandText.setForeground(Color.WHITE);
        brandRow.add(brandIcon);
        brandRow.add(brandText);

        sidebar.add(brandRow);
        sidebar.add(navButton(VectorIcon.Type.LIST, "All Patients", this::onViewPatients));
        sidebar.add(navButton(VectorIcon.Type.SEARCH, "Search Patient", this::onSearch));
        sidebar.add(navButton(VectorIcon.Type.ALERT, "Emergency Cases", this::onEmergencyCases));
        sidebar.add(navButton(VectorIcon.Type.CHART, "Statistics", this::onStatistics));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navButton(VectorIcon.Type.BACK, "Back to Login", this::onBackToLogin, true));

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

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(UITheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel welcome = UITheme.heading("Welcome, Faculty");
        JLabel sub = new JLabel("View-only access to patient records and statistics.");
        sub.setFont(UITheme.FONT_LABEL);
        sub.setForeground(UITheme.TEXT_MUTED);

        JPanel headerText = new JPanel();
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
        headerText.setOpaque(false);
        headerText.add(welcome);
        headerText.add(sub);

        JPanel stats = new JPanel(new GridLayout(1, 2, 16, 0));
        stats.setOpaque(false);
        totalStat = new JLabel();
        emergencyStat = new JLabel();
        stats.add(statCard("Total Patients", totalStat, UITheme.PRIMARY));
        stats.add(statCard("Emergency Cases", emergencyStat, UITheme.DANGER));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
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
                    setText("\u26A0 High");
                } else {
                    c.setForeground(UITheme.TEXT_DARK);
                    setFont(getFont().deriveFont(Font.PLAIN));
                    setText("Normal");
                }
                return c;
            }
        };
    }

    private JPanel statCard(String title, JLabel valueLabel, Color accent) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UITheme.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, accent),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_LABEL);
        t.setForeground(UITheme.TEXT_MUTED);
        valueLabel.setFont(UITheme.FONT_MONO_STAT);
        valueLabel.setForeground(UITheme.TEXT_DARK);
        p.add(t);
        p.add(valueLabel);
        return p;
    }

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

    // ---------------- Menu actions (mirror console facultyMenu() cases) ----------------

    private void onViewPatients() {
        emergencyOnly = false;
        refreshTable();
    }

    private void onSearch() {
        new SearchDialog(frame, service).setVisible(true);
    }

    private void onEmergencyCases() {
        emergencyOnly = true;
        refreshTable();
        if (service.getEmergencyCount() == 0) {
            JOptionPane.showMessageDialog(this, "No high priority cases found.",
                    "Emergency Cases", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onStatistics() {
        JOptionPane.showMessageDialog(this,
                "Total Patients: " + service.getTotalCount() + "\nEmergency Cases: " + service.getEmergencyCount(),
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onBackToLogin() {
        frame.showScreen(MainFrame.LOGIN);
    }
}
