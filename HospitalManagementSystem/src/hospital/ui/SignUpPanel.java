package hospital.ui;

import hospital.service.HospitalService;

import javax.swing.*;
import java.awt.*;

/**
 * SignUpPanel.java
 * ---------------------------------------------------------
 * GUI counterpart of console signUp(): the very first screen shown when
 * the application starts. It asks the operator to create the admin
 * username/password used for this session, exactly like the console
 * version does unconditionally at startup.
 * ---------------------------------------------------------
 */
public class SignUpPanel extends GradientPanel {

    public SignUpPanel(MainFrame frame, HospitalService service) {
        super(UITheme.GRADIENT_HERO_FROM, UITheme.GRADIENT_HERO_TO);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(440, 560));

        JLabel logo = new JLabel(new VectorIcon(VectorIcon.Type.LOGO, UITheme.PRIMARY, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Hospital Management System");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Patient Record Dashboard");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel formHeading = new JLabel("Admin Sign Up");
        formHeading.setFont(UITheme.FONT_HEADING);
        formHeading.setForeground(UITheme.TEXT_DARK);
        formHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        formHeading.setBorder(BorderFactory.createEmptyBorder(18, 0, 14, 0));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        UITheme.styleTextField(userField);
        UITheme.styleTextField(passField);
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userField.setMaximumSize(new Dimension(320, 38));
        passField.setMaximumSize(new Dimension(320, 38));

        JLabel status = new JLabel(" ");
        status.setFont(UITheme.FONT_LABEL);
        status.setForeground(UITheme.DANGER);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton signUpBtn = new RoundedButton("Create Admin Account", UITheme.PRIMARY,
                UITheme.PRIMARY_DARK, UITheme.PRIMARY_DARK);
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        signUpBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            // Validation improvement over the console version, which accepted blank input.
            if (user.isEmpty() || pass.isEmpty()) {
                status.setText("Username and password cannot be empty.");
                return;
            }
            service.signUp(user, pass);
            JOptionPane.showMessageDialog(frame,
                    "Sign up successful! Please login to continue.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.showScreen(MainFrame.LOGIN);
        });

        card.add(logo);
        card.add(Box.createVerticalStrut(6));
        card.add(title);
        card.add(subtitle);
        card.add(formHeading);
        card.add(labeled("Username"));
        card.add(userField);
        card.add(Box.createVerticalStrut(10));
        card.add(labeled("Password"));
        card.add(passField);
        card.add(Box.createVerticalStrut(6));
        card.add(status);
        card.add(Box.createVerticalStrut(10));
        card.add(signUpBtn);

        add(card);
    }

    private JComponent labeled(String text) {
        JLabel l = UITheme.sectionLabel(text);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        return l;
    }
}
