package org.bh.tools.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JTextArea;

/**
 * BHTextArea, made for English Learner, is copyright BHStudios ©2016 BH-1-PS <hr/>
 * Merely an extension of the {@link JTextArea} class that adds placeholder capability to the field that only appears
 * (in a different color than normal text) when the text field is both out of focus and empty.
 *
 * @author Kyli Rouge of Blue Husky Programming, © 2016
 * @version 1.0.0
 * @since 2016-04-03
 * @see javax.swing.JTextArea
 */
public class BHTextArea extends JTextArea {

    private static final Color DEFAULT_PLACEHOLDER_COLOR = new Color(0x80_808080, true);
    private CharSequence placeholder;
    private Color placeholderColor = DEFAULT_PLACEHOLDER_COLOR;


    public BHTextArea(CharSequence initText, CharSequence initPlaceholder, Color initPlaceholderColor) {
        super(null == initText ? null : initText.toString());
        placeholder = initPlaceholder;
        if (null != initPlaceholderColor) {
            placeholderColor = initPlaceholderColor;
        }
        addOwnListeners();
    }

    public BHTextArea(CharSequence initText, CharSequence initPlaceholder) {
        this(initText, initPlaceholder, null);
    }

    public BHTextArea(CharSequence initText) {
        this(initText, null, null);
    }

    public BHTextArea() {
        this(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (null != placeholder && placeholder.length() > 0 && !hasFocus() && getText().isEmpty()) {
            String placeholderString = placeholder.toString();
            g.setFont(getFont());
            g.setColor(placeholderColor);
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            Rectangle2D stringSize = g.getFontMetrics().getStringBounds(placeholderString, g);
            double width = getWidth(),
                    height = getHeight(),
                    placeholderX = (width / 2) - (stringSize.getWidth() / 2),
                    placeholderY = (height / 2) + (g.getFont().getSize2D() / 2);
            g.drawString(placeholder.toString(), (int) Math.round(placeholderX), (int) Math.round(placeholderY));
        }
    }

    public CharSequence getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(CharSequence newPlaceholder) {
        placeholder = newPlaceholder;
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color newPlaceholderColor) {
        placeholderColor = newPlaceholderColor;
    }

    private void addOwnListeners() {
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }
}
