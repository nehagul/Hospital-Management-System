package hospital.ui;

import javax.swing.*;
import java.awt.*;

/**
 * GradientPanel.java
 * ---------------------------------------------------------
 * A JPanel that paints a soft diagonal gradient background instead of a
 * flat fill. Used on the Sign Up / Login "hero" screens and dashboard
 * header banners so the app reads as a designed product rather than a
 * default gray Swing form.
 * ---------------------------------------------------------
 */
public class GradientPanel extends JPanel {

    private final Color from;
    private final Color to;
    private final boolean diagonal;
    private int arc = 0;

    public GradientPanel(Color from, Color to) {
        this(from, to, true);
    }

    public GradientPanel(Color from, Color to, boolean diagonal) {
        this.from = from;
        this.to = to;
        this.diagonal = diagonal;
        setOpaque(false);
    }

    /** Sets a corner radius so this panel renders as a rounded banner/card. */
    public GradientPanel withArc(int arc) {
        this.arc = arc;
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = diagonal
                ? new GradientPaint(0, 0, from, w, h, to)
                : new GradientPaint(0, 0, from, 0, h, to);
        g2.setPaint(gp);
        if (arc > 0) {
            g2.fill(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, arc, arc));
        } else {
            g2.fillRect(0, 0, w, h);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
