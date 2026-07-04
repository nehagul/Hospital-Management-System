package hospital.ui;

import hospital.service.HospitalService;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame.java
 * ---------------------------------------------------------
 * The single top-level JFrame for the whole application. It hosts a
 * CardLayout that swaps between the Sign Up, Login, Admin Dashboard and
 * Faculty Dashboard screens -- mirroring the exact program flow of the
 * console version:
 *
 *   showTitle() -> signUp() -> loadFromFile() -> login() -> (admin|faculty) menu
 *
 * Screens are created lazily where they depend on runtime state (the
 * dashboards are rebuilt on each visit so their tables/stats are fresh).
 * ---------------------------------------------------------
 */
public class MainFrame extends JFrame {

    public static final String SIGNUP = "SIGNUP";
    public static final String LOGIN = "LOGIN";
    public static final String ADMIN = "ADMIN";
    public static final String FACULTY = "FACULTY";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final HospitalService service = new HospitalService();

    private JPanel adminPanel;
    private JPanel facultyPanel;

    public MainFrame() {
        super("Hospital Management System - Patient Record Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(UITheme.BACKGROUND);

        cards.setBackground(UITheme.BACKGROUND);
        cards.add(new SignUpPanel(this, service), SIGNUP);
        cards.add(new LoginPanel(this, service), LOGIN);
        add(cards);

        // Matches console: loadFromFile() is called right after signUp(), before login.
        service.loadFromFile();

        showScreen(SIGNUP);
    }

    /** Switches to a named, already-registered static screen (SIGNUP/LOGIN). */
    public void showScreen(String name) {
        cardLayout.show(cards, name);
    }

    /** Rebuilds and shows the Admin dashboard so its data is always current. */
    public void showAdminDashboard() {
        if (adminPanel != null) cards.remove(adminPanel);
        adminPanel = new AdminDashboardPanel(this, service);
        cards.add(adminPanel, ADMIN);
        cardLayout.show(cards, ADMIN);
    }

    /** Rebuilds and shows the Faculty dashboard so its data is always current. */
    public void showFacultyDashboard() {
        if (facultyPanel != null) cards.remove(facultyPanel);
        facultyPanel = new FacultyDashboardPanel(this, service);
        cards.add(facultyPanel, FACULTY);
        cardLayout.show(cards, FACULTY);
    }

    public HospitalService getService() {
        return service;
    }
}
