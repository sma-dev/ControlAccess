package org.access.installer.panel;

import org.access.installer.InstallerFrame;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends Panel {

    public WelcomePanel() {
        setBackground(Color.BLUE);
        JButton next = new JButton("Next");
        next.addActionListener(e -> InstallerFrame.getContext().navigate(InstallerFrame.PanelID.PORT));
        add(next);
    }

}
