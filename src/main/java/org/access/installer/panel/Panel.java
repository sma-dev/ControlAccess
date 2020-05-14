package org.access.installer.panel;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    public void attach(JPanel container) {
        container.removeAll();
        container.add(this, BorderLayout.CENTER);
        container.repaint();
        container.revalidate();
    }

    public abstract void detach();
}
