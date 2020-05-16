package org.access.installer.panel;

import jssc.SerialPortList;
import org.access.installer.Settings;
import org.access.installer.TextFieldCustom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PortSelectPanel extends Panel {

    private final JComboBox<String> comboBox;

    @Override
    public void attach(JPanel container) {
        super.attach(container);
        String[] items = SerialPortList.getPortNames();
        if (items.length == 0) {
            JOptionPane.showMessageDialog(this, "No serial devices detected!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        Settings.port = items[0];


        comboBox.removeAllItems();
        for (String port : items) {
            comboBox.addItem(port);
        }

    }

    public PortSelectPanel() {
        setBackground(Settings.BG_COLOR);
        setPreferredSize(new Dimension(512, 314));
        setLayout(new GridBagLayout());

        comboBox = new JComboBox<>();
        comboBox.addItemListener(e -> {
            JComboBox<String> box = (JComboBox<String>) e.getSource();
            String item = (String) box.getSelectedItem();
            Settings.port = item;
            System.out.println("Selected: " + item);
        });
        comboBox.setFont(new Font("Verdana", Font.PLAIN, 18));
        comboBox.setPreferredSize(new Dimension(100, 25));

        TextFieldCustom textFieldCustom = new TextFieldCustom(
                new String[][]{
                        {"Компонент JTextPane \r\n", "heading"},
                        {"\r\n", "normal"},
                        {"JTextPane незамени \r\n", "normal"},
                        {"многофункциональ.\r\n", "normal"},
                        {"\r\n", "normal"},
                        {"Он позволяет встае \r\n", "normal"}}
        );
        textFieldCustom.setBackground(Settings.BG_COLOR);
        textFieldCustom.setBorder(new EmptyBorder(0, 0, 0, 60));
        textFieldCustom.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(textFieldCustom);
        add(comboBox);
    }

    @Override
    public void detach() {

    }
}
