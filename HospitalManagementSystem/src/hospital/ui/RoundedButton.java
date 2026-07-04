package hospital.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * RoundedButton.java
 * ---------------------------------------------------------
 * A small custom-painted JButton with rounded corners and a hover/press
 * color shift, used for the primary "hero" actions (Sign Up, Login,
 * Save & Exit) to give the app a modern, non-default Swing feel.
 * ---------------------------------------------------------
 */
public class RoundedButton extends JButton {

    private Color base;
    private Color hover;
    private Color pressed;
    private final int arc = 14;

    public RoundedButton(String text, Color base, Color hover, Color pressed) {
        super(text);
        this.base = base;
        this.hover = hover;
        this.pressed = pressed;
        setFont(UITheme.FONT_BUTTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 26, 12, 26));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = base;
        if (getModel().isPressed()) fill = pressed;
        else if (getModel().isRollover()) fill = hover;

        g2.setColor(fill);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
        g2.dispose();
        super.paintComponent(g);
    }
}
