package hospital;

import hospital.ui.MainFrame;

import javax.swing.*;

/**
 * Main.java
 * ---------------------------------------------------------
 * Application entry point. Replaces the console version's
 * "main() { showTitle(); signUp(); loadFromFile(); login(); }" with the
 * equivalent GUI startup sequence, launched safely on the Swing Event
 * Dispatch Thread.
 * ---------------------------------------------------------
 */
public class Main {
    public static void main(String[] args) {
        // NOTE: The native/system Look & Feel (e.g. Windows) paints many components,
        // most notably JTableHeader, using its own theme and silently ignores custom
        // setBackground/setForeground calls -- this was causing invisible header text
        // and inconsistent button styling. The cross-platform "Metal" L&F fully
        // respects our custom UITheme colors everywhere, giving a consistent,
        // reliable, professional look on every operating system.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to whatever default is available.
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }
}
