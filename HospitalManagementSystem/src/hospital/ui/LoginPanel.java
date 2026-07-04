package hospital.ui;

import hospital.service.HospitalService;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel.java
 * ---------------------------------------------------------
 * GUI counterpart of console login() / adminLogin() / facultyLogin().
 * A toggle lets the operator pick Admin or Faculty, matching the
 * console's numbered menu (1. Admin Login  2. Faculty Login).
 *
 * Admin login validates credentials against the ones created on the
 * Sign Up screen (identical check to console adminLogin()).
 * Faculty login performs no credential check, exactly like the console
 * version, which reads and discards the faculty username/password.
 * ---------------------------------------------------------
 */
public class LoginPanel extends GradientPanel {

    private CardLayout formSwitcher;
    private JPanel formArea;

    public LoginPanel(MainFrame frame, HospitalService service) {
        super(UITheme.GRADIENT_HERO_FROM, UITheme.GRADIENT_HERO_TO);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(480, 620));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Select a role to continue");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---- Role toggle ----
        JPanel toggle = new JPanel(new GridLayout(1, 2, 0, 0));
        toggle.setMaximumSize(new Dimension(320, 40));
        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggle.setBorder(BorderFactory.createEmptyBorder(16, 0, 10, 0));
        JToggleButton adminToggle = new JToggleButton("Admin Login", true);
        JToggleButton facultyToggle = new JToggleButton("Faculty Login");
        ButtonGroup group = new ButtonGroup();
        group.add(adminToggle);
        group.add(facultyToggle);
        styleToggle(adminToggle);
        styleToggle(facultyToggle);
        toggle.add(adminToggle);
        toggle.add(facultyToggle);

        // ---- Admin form ----
        JTextField adminUser = new JTextField();
        JPasswordField adminPass = new JPasswordField();
        UITheme.styleTextField(adminUser);
        UITheme.styleTextField(adminPass);
        JLabel adminStatus = new JLabel(" ");
        adminStatus.setForeground(UITheme.DANGER);
        adminStatus.setFont(UITheme.FONT_LABEL);
        adminStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel adminForm = formPanel();
        adminForm.add(fieldBlock("Username", adminUser));
        adminForm.add(Box.createVerticalStrut(10));
        adminForm.add(fieldBlock("Password", adminPass));
        adminForm.add(Box.createVerticalStrut(8));
        adminForm.add(adminStatus);
        adminForm.add(Box.createVerticalStrut(6));
        RoundedButton adminBtn = new RoundedButton("Login as Admin", UITheme.PRIMARY,
                UITheme.PRIMARY_DARK, UITheme.PRIMARY_DARK);
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminBtn.addActionListener(e -> {
            String u = adminUser.getText().trim();
            String p = new String(adminPass.getPassword());
            if (service.authenticateAdmin(u, p)) {
                service.loadFromFile();
                frame.showAdminDashboard();
            } else {
                adminStatus.setText("Invalid credentials! Try again.");
            }
        });
        adminForm.add(adminBtn);

        // ---- Faculty form ----
        JTextField facUser = new JTextField();
        JPasswordField facPass = new JPasswordField();
        UITheme.styleTextField(facUser);
        UITheme.styleTextField(facPass);
        JLabel facHint = new JLabel("Faculty access does not require pre-registration.");
        facHint.setFont(UITheme.FONT_LABEL);
        facHint.setForeground(UITheme.TEXT_MUTED);
        facHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel facultyForm = formPanel();
        facultyForm.add(fieldBlock("Faculty Username", facUser));
        facultyForm.add(Box.createVerticalStrut(10));
        facultyForm.add(fieldBlock("Password", facPass));
        facultyForm.add(Box.createVerticalStrut(8));
        facultyForm.add(facHint);
        facultyForm.add(Box.createVerticalStrut(6));
        RoundedButton facBtn = new RoundedButton("Login as Faculty", UITheme.PRIMARY,
                UITheme.PRIMARY_DARK, UITheme.PRIMARY_DARK);
        facBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        facBtn.addActionListener(e -> {
            // Matches console facultyLogin(): input is accepted without validation.
            service.loadFromFile();
            frame.showFacultyDashboard();
        });
        facultyForm.add(facBtn);

        formSwitcher = new CardLayout();
        formArea = new JPanel(formSwitcher);
        formArea.setOpaque(false);
        formArea.add(adminForm, "ADMIN_FORM");
        formArea.add(facultyForm, "FACULTY_FORM");

        adminToggle.addActionListener(e -> formSwitcher.show(formArea, "ADMIN_FORM"));
        facultyToggle.addActionListener(e -> formSwitcher.show(formArea, "FACULTY_FORM"));

        card.add(title);
        card.add(subtitle);
        card.add(toggle);
        card.add(formArea);

        add(card);
    }

    private JPanel formPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private JPanel fieldBlock(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel l = UITheme.sectionLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(340, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(4));
        p.add(field);
        return p;
    }

    private void styleToggle(JToggleButton b) {
        b.setFont(UITheme.FONT_NAV);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(Color.WHITE);
        b.setForeground(UITheme.TEXT_MUTED);
        b.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        b.addChangeListener(e -> {
            if (b.isSelected()) {
                b.setBackground(UITheme.PRIMARY);
                b.setForeground(Color.WHITE);
            } else {
                b.setBackground(Color.WHITE);
                b.setForeground(UITheme.TEXT_MUTED);
            }
        });
    }
}
