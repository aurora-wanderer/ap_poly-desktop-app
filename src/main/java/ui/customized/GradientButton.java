package main.java.ui.customized;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

/**
 *
 * @author oXCToo
 */
public class GradientButton extends JButton {

    private Color startColor;
    private Color endColor;
    private boolean transparentControls = true;
    private int gradientFocus;

    public GradientButton() {
        setBg(transparentControls);
    }

    public GradientButton(String text) {
        super(text);
        setBg(transparentControls);
    }

    public GradientButton(Color startColor, Color endColor, String text) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        setBg(transparentControls);
    }

    public GradientButton(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        setBg(transparentControls);
    }

    public GradientButton(Color startColor, Color endColor, boolean transparentControls) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.transparentControls = transparentControls;
        setBg(transparentControls);
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public boolean isTransparentControls() {
        return transparentControls;
    }

    public void setTransparentControls(boolean transparentControls) {
        this.transparentControls = transparentControls;
    }

    public int getGradientFocus() {
        return gradientFocus;
    }

    public void setGradientFocus(int gradientFocus) {
        this.gradientFocus = gradientFocus;
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setContentAreaFilled(false);
        setFocusPainted(false);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0,
                startColor, gradientFocus, h, endColor);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        super.paintComponent(g);
    }

    private void setBg(boolean isOpaque) {
        Component[] components = this.getComponents();
        for (Component component : components) {
            ((JLabel) component).setOpaque(isOpaque);
            ((JCheckBox) component).setOpaque(isOpaque);
            ((JTextField) component).setOpaque(isOpaque);
            ((JPasswordField) component).setOpaque(isOpaque);
            ((JFormattedTextField) component).setOpaque(isOpaque);
            ((JToolBar) component).setOpaque(isOpaque);
            ((JRadioButton) component).setOpaque(isOpaque);
        }
    }

}
