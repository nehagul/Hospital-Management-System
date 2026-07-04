package hospital.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * UITheme.java
 * ---------------------------------------------------------
 * Centralized "design system" for the application: a medical / clinical
 * color palette (deep teal + navy sidebar + clean whites, with red used
 * only for emergency/danger signals), consistent typography, and a set
 * of small helper methods used across every screen so the whole app
 * looks like one coherent, professional product instead of a collection
 * of default Swing components.
 * ---------------------------------------------------------
 */
public final class UITheme {

    private UITheme() { }

    // ---------------- Palette ----------------
    public static final Color PRIMARY        = new Color(0x0E7C7B); // teal - medical/clinical
    public static final Color PRIMARY_DARK    = new Color(0x0B5E5D);
    public static final Color SIDEBAR         = new Color(0x142B45); // deep navy
    public static final Color SIDEBAR_HOVER    = new Color(0x1E3A5C);
    public static final Color SIDEBAR_SELECTED  = new Color(0x0E7C7B);
    public static final Color BACKGROUND       = new Color(0xF3F6F8);
    public static final Color CARD           = Color.WHITE;
    public static final Color BORDER          = new Color(0xDDE4E9);
    public static final Color TEXT_DARK        = new Color(0x1B2733);
    public static final Color TEXT_MUTED       = new Color(0x6B7B8C);
    public static final Color TEXT_ON_DARK      = new Color(0xE7EEF3);
    public static final Color DANGER          = new Color(0xD64545); // emergency / delete
    public static final Color DANGER_DARK      = new Color(0xB53A3A);
    public static final Color SUCCESS         = new Color(0x2E9E5B);
    public static final Color WARNING         = new Color(0xE0A62D);
    public static final Color TABLE_STRIPE      = new Color(0xF0F5F7);
    public static final Color TABLE_HEADER      = new Color(0x0E7C7B);

    // Gradient palette for hero/banner surfaces
    public static final Color GRADIENT_HERO_FROM  = new Color(0xEAF6F5);
    public static final Color GRADIENT_HERO_TO    = new Color(0xF7FAFB);
    public static final Color GRADIENT_BANNER_FROM = new Color(0x0E7C7B);
    public static final Color GRADIENT_BANNER_TO   = new Color(0x123F5C);

    // ---------------- Typography ----------------
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_HEADING  = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL_BOLD= new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_TABLE    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEAD= new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO_STAT = new Font("Segoe UI", Font.BOLD, 30);

    // ---------------- Component helpers ----------------

    /** Applies the standard "primary" look to a filled action button. */
    public static void stylePrimaryButton(JButton b) {
        b.setFont(FONT_BUTTON);
        b.setForeground(Color.WHITE);
        b.setBackground(PRIMARY);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
    }

    /** Applies a "danger" look (delete / logout style) to a button. */
    public static void styleDangerButton(JButton b) {
        stylePrimaryButton(b);
        b.setBackground(DANGER);
    }

    /** Applies an outlined / secondary look to a button. */
    public static void styleSecondaryButton(JButton b) {
        b.setFont(FONT_BUTTON);
        b.setForeground(PRIMARY_DARK);
        b.setBackground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 1, true),
                new EmptyBorder(9, 20, 9, 20)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Standard text field styling with padding and a soft border. */
    public static void styleTextField(JTextComponent field) {
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
    }

    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_LABEL);
        box.setBackground(Color.WHITE);
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL_BOLD);
        l.setForeground(TEXT_DARK);
        return l;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_DARK);
        return l;
    }

    /** A heading row with a small icon beside it, used across dialogs for a consistent look. */
    public static JPanel headingWithIcon(String text, VectorIcon.Type type, Color iconColor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel icon = new JLabel(new VectorIcon(type, iconColor, 22));
        icon.setBorder(new EmptyBorder(0, 0, 0, 10));
        p.add(icon);
        p.add(heading(text));
        return p;
    }

    /** Styles a JTable + its header consistently across the app. */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(0xCFEDEA));
        table.setSelectionForeground(TEXT_DARK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        // IMPORTANT: setting colors directly on JTableHeader is silently ignored by
        // several native Look&Feels (e.g. Windows), which paint the header using their
        // own themed renderer and leave the header text invisible (white-on-white).
        // Supplying an explicit, fully opaque header cell renderer guarantees the
        // teal background + white bold text always show, regardless of platform L&F.
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                              boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                label.setOpaque(true);
                label.setBackground(TABLE_HEADER);
                label.setForeground(Color.WHITE);
                label.setFont(FONT_TABLE_HEAD);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1, PRIMARY_DARK),
                        new EmptyBorder(0, 12, 0, 12)));
                return label;
            }
        });
    }

    /** A simple rounded, subtly shadowed "card" panel used to group content. */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)));
        return p;
    }
}
