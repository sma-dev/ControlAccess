package org.access.installer.panel;

import org.access.installer.TextFieldCustom;

import javax.swing.*;
import java.awt.*;

public class FinishPanel extends Panel {


    public FinishPanel() {
        setBackground(Color.WHITE);

        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        setPreferredSize(new Dimension(512, 318));
        setLayout(new GridBagLayout());

        TextFieldCustom welcomeTextPane = new TextFieldCustom(new String[][]{
                {"Компонент JTextPane \r\n", "heading"},
                {"\r\n", "normal"},
                {"JTextPane незамени \r\n", "normal"},
                {"многофункциональ.\r\n", "normal"},
                {"\r\n", "normal"},
                {"Он позволяет встае \r\n", "normal"}});
        welcomeTextPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon("src\\main\\java\\org\\access\\installer\\assets\\welcome.png")
                        .getImage().getScaledInstance(209, 318, Image.SCALE_DEFAULT));
        imageLabel.setIcon(imageIcon);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        imageLabel.setHorizontalAlignment(SwingConstants.LEFT);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;    // нулевая ячейка по горизонтали
        constraints.gridy = 0;    // первая ячейка по вертикали
        add(imageLabel, constraints);
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 2;
        constraints.gridx = 1;    // нулевая ячейка по горизонтали
        constraints.gridy = 0;    // первая ячейка по вертикали
        add(welcomeTextPane, constraints);

    }

    @Override
    public void detach() {
        // nothing to do
    }
}