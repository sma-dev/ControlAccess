package org.access.installer.panel;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    public void attachTo(JFrame container) {
        container.getContentPane().removeAll();
        container.add(this, BorderLayout.CENTER);
        container.repaint();
        container.revalidate();
    }
}
