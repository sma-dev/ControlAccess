package org.access.installer.panel;

import jssc.SerialPortList;
import org.access.installer.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PortSelectPanel extends Panel {


    public PortSelectPanel() {

        setBackground(Settings.BG_COLOR);
        setPreferredSize(new Dimension(512, 318));
        Font font = new Font("Verdana", Font.PLAIN, 18);

        String[] items = SerialPortList.getPortNames();
        if (items.length == 0) {
            JOptionPane.showMessageDialog(this, "No serial devices detected!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        Settings.port = items[0];

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
    }

    @Override
    public void detach() {

    }
}
