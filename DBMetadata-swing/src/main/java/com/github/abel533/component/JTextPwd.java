package com.github.abel533.component;

import com.github.abel533.Code;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author liuzh
 */
public class JTextPwd extends JPasswordField {

    private static final long serialVersionUID = 1L;
    private BufferedImage buffer = null;

    public JTextPwd() {
        super();
        setFont(Code.TEXT_FONT);
    }

    @Override
    public void paintComponent(Graphics g) {
        Component window = this.getTopLevelAncestor();
        if (window instanceof Window && !((Window) window).isOpaque()) {
            // This is a translucent window, so we need to draw to a buffer
            // first to work around a bug in the DirectDraw rendering in Swing.
            int w = this.getWidth();
            int h = this.getHeight();
            if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {
                // Create a new buffer based on the current size.
                GraphicsConfiguration gc = this.getGraphicsConfiguration();
                buffer = gc.createCompatibleImage(w, h, BufferedImage.TRANSLUCENT);
            }

            // Use the super class's paintComponent implementation to draw to
            // the buffer, then write that buffer to the original Graphics object.
            Graphics bufferGraphics = buffer.createGraphics();
            try {
                super.paintComponent(bufferGraphics);
            } finally {
                bufferGraphics.dispose();
            }
            g.drawImage(buffer, 0, 0, w, h, 0, 0, w, h, null);
        } else {
            // This is not a translucent window, so we can call the super class
            // implementation directly.
            super.paintComponent(g);
        }
    }
}
