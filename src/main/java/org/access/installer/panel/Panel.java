package org.access.installer.panel;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    public void attach(JPanel container) {
        container.removeAll();
        container.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(this, gbc);
        container.repaint();
        container.revalidate();
    }

    public abstract void detach();
}
