package hospital.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * VectorIcon.java
 * ---------------------------------------------------------
 * Small, flat, hand-drawn (Java2D) icon set used across the sidebars and
 * tables. These are drawn entirely with vector shapes instead of Unicode
 * emoji/dingbat characters, so they render identically and reliably on
 * every machine -- no dependency on which emoji/symbol fonts happen to
 * be installed (the cause of the "tofu box" glyphs seen with emoji).
 * ---------------------------------------------------------
 */
public class VectorIcon implements Icon {

    public enum Type {
        ADD, EDIT, DELETE, LIST, COUNT, CALENDAR, ALERT, REPORT, CHART,
        WALLET, POWER, SEARCH, BACK, LOGO, DOT
    }

    private final Type type;
    private final Color color;
    private final int size;

    public VectorIcon(Type type, Color color, int size) {
        this.type = type;
        this.color = color;
        this.size = size;
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.setColor(color);
        float s = size;
        float stroke = Math.max(1.6f, s * 0.11f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {
            case ADD:
                g2.drawLine(round(s * 0.5f), round(s * 0.15f), round(s * 0.5f), round(s * 0.85f));
                g2.drawLine(round(s * 0.15f), round(s * 0.5f), round(s * 0.85f), round(s * 0.5f));
                break;
            case EDIT:
                g2.drawLine(round(s * 0.18f), round(s * 0.82f), round(s * 0.68f), round(s * 0.32f));
                g2.fillPolygon(new int[]{round(s * 0.68f), round(s * 0.88f), round(s * 0.68f)},
                        new int[]{round(s * 0.32f), round(s * 0.12f), round(s * 0.12f)}, 3);
                g2.drawLine(round(s * 0.14f), round(s * 0.86f), round(s * 0.24f), round(s * 0.76f));
                break;
            case DELETE:
                g2.drawRoundRect(round(s * 0.24f), round(s * 0.32f), round(s * 0.52f), round(s * 0.56f), 2, 2);
                g2.drawLine(round(s * 0.14f), round(s * 0.32f), round(s * 0.86f), round(s * 0.32f));
                g2.drawLine(round(s * 0.38f), round(s * 0.18f), round(s * 0.62f), round(s * 0.18f));
                g2.drawLine(round(s * 0.38f), round(s * 0.44f), round(s * 0.38f), round(s * 0.76f));
                g2.drawLine(round(s * 0.62f), round(s * 0.44f), round(s * 0.62f), round(s * 0.76f));
                break;
            case LIST:
                for (int i = 0; i < 3; i++) {
                    int yy = round(s * (0.24f + i * 0.28f));
                    g2.drawLine(round(s * 0.14f), yy, round(s * 0.86f), yy);
                }
                break;
            case COUNT:
                g2.drawLine(round(s * 0.32f), round(s * 0.12f), round(s * 0.22f), round(s * 0.88f));
                g2.drawLine(round(s * 0.72f), round(s * 0.12f), round(s * 0.62f), round(s * 0.88f));
                g2.drawLine(round(s * 0.12f), round(s * 0.38f), round(s * 0.86f), round(s * 0.38f));
                g2.drawLine(round(s * 0.08f), round(s * 0.66f), round(s * 0.82f), round(s * 0.66f));
                break;
            case CALENDAR:
                g2.drawRoundRect(round(s * 0.14f), round(s * 0.2f), round(s * 0.72f), round(s * 0.66f), 3, 3);
                g2.drawLine(round(s * 0.14f), round(s * 0.4f), round(s * 0.86f), round(s * 0.4f));
                g2.drawLine(round(s * 0.32f), round(s * 0.12f), round(s * 0.32f), round(s * 0.28f));
                g2.drawLine(round(s * 0.68f), round(s * 0.12f), round(s * 0.68f), round(s * 0.28f));
                break;
            case ALERT:
                g2.drawPolygon(new int[]{round(s * 0.5f), round(s * 0.1f), round(s * 0.9f)},
                        new int[]{round(s * 0.12f), round(s * 0.86f), round(s * 0.86f)}, 3);
                g2.fillOval(round(s * 0.44f), round(s * 0.6f), round(s * 0.12f), round(s * 0.12f));
                g2.drawLine(round(s * 0.5f), round(s * 0.34f), round(s * 0.5f), round(s * 0.52f));
                break;
            case REPORT:
                g2.drawRoundRect(round(s * 0.2f), round(s * 0.1f), round(s * 0.6f), round(s * 0.8f), 3, 3);
                for (int i = 0; i < 3; i++) {
                    int yy = round(s * (0.34f + i * 0.16f));
                    g2.drawLine(round(s * 0.32f), yy, round(s * 0.68f), yy);
                }
                break;
            case CHART:
                g2.fillRect(round(s * 0.16f), round(s * 0.52f), round(s * 0.16f), round(s * 0.34f));
                g2.fillRect(round(s * 0.42f), round(s * 0.3f), round(s * 0.16f), round(s * 0.56f));
                g2.fillRect(round(s * 0.68f), round(s * 0.42f), round(s * 0.16f), round(s * 0.44f));
                break;
            case WALLET:
                g2.drawRoundRect(round(s * 0.12f), round(s * 0.24f), round(s * 0.76f), round(s * 0.52f), 4, 4);
                g2.drawLine(round(s * 0.12f), round(s * 0.42f), round(s * 0.88f), round(s * 0.42f));
                g2.fillOval(round(s * 0.66f), round(s * 0.5f), round(s * 0.12f), round(s * 0.12f));
                break;
            case POWER:
                g2.drawArc(round(s * 0.14f), round(s * 0.22f), round(s * 0.72f), round(s * 0.66f), -60, 300);
                g2.drawLine(round(s * 0.5f), round(s * 0.1f), round(s * 0.5f), round(s * 0.5f));
                break;
            case SEARCH:
                g2.drawOval(round(s * 0.14f), round(s * 0.14f), round(s * 0.5f), round(s * 0.5f));
                g2.drawLine(round(s * 0.58f), round(s * 0.58f), round(s * 0.88f), round(s * 0.88f));
                break;
            case BACK:
                g2.drawLine(round(s * 0.72f), round(s * 0.5f), round(s * 0.2f), round(s * 0.5f));
                g2.drawLine(round(s * 0.42f), round(s * 0.22f), round(s * 0.16f), round(s * 0.5f));
                g2.drawLine(round(s * 0.42f), round(s * 0.78f), round(s * 0.16f), round(s * 0.5f));
                break;
            case LOGO:
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, s, s, s * 0.28f, s * 0.28f));
                g2.setColor(Color.WHITE);
                float t = s * 0.16f;
                g2.fill(new RoundRectangle2D.Float(s * 0.42f, s * 0.18f, t, s * 0.64f, t * 0.4f, t * 0.4f));
                g2.fill(new RoundRectangle2D.Float(s * 0.18f, s * 0.42f, s * 0.64f, t, t * 0.4f, t * 0.4f));
                break;
            case DOT:
                g2.setColor(color);
                g2.fill(new Ellipse2D.Float(s * 0.2f, s * 0.2f, s * 0.6f, s * 0.6f));
                break;
        }
        g2.dispose();
    }

    private int round(float v) {
        return Math.round(v);
    }
}
