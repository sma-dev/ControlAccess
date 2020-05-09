package org.access.installer.panel;

import jssc.SerialPortList;
import org.access.installer.InstallerFrame;
import org.access.installer.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PortSelectPanel extends Panel {


    public PortSelectPanel() {

        Font font = new Font("Verdana", Font.PLAIN, 18);

        String[] items = SerialPortList.getPortNames();

        ActionListener actionListener = e -> {
            JComboBox<String> box = (JComboBox<String>) e.getSource();
            String item = (String) box.getSelectedItem();
            Settings.port = item;
            System.out.println("Selected: " + item);
        };

        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(font);
        comboBox.addActionListener(actionListener);
        add(comboBox);

        JButton next = new JButton("Next");
        next.addActionListener(e -> InstallerFrame.getContext().navigate(InstallerFrame.PanelID.PATH));
        add(next);
    }
}