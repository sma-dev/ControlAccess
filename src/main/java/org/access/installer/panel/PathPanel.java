package org.access.installer.panel;

import org.access.installer.Button;
import org.access.installer.Settings;
import org.access.installer.TextFieldCustom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class PathPanel extends Panel {
    private final JTextField pathTextField;

    @Override
    public void attach(JPanel container) {
        super.attach(container);
        pathTextField.setText(Settings.path);
        pathTextField.requestFocus();
        pathTextField.selectAll();
    }

    public PathPanel() {
        setBackground(Settings.BG_COLOR);
        setPreferredSize(new Dimension(512, 314));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(0, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();


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

        JPanel browseContainer = new JPanel(new BorderLayout());
        JPanel browsingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pathTextField = new JTextField(30);
        pathTextField.setBorder(new LineBorder(new Color(0, 120, 215), 1));

        JButton browse = new Button("Browse...");

        browse.setPreferredSize(new Dimension(90, 20));
        browse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")).getParentFile().getParentFile());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                pathTextField.setText(selectedFile.getAbsolutePath());
                Settings.path = pathTextField.getText();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        });

        browsingPanel.add(pathTextField);
        browsingPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        browsingPanel.add(browse);

        JLabel infoText = new JLabel("Destination folder");
        infoText.setBorder(new EmptyBorder(2, 10, 2, 0));
        browseContainer.add(infoText, BorderLayout.NORTH);
        browseContainer.add(browsingPanel, BorderLayout.CENTER);
        browseContainer.add(Box.createRigidArea(new Dimension(0, 18)), BorderLayout.SOUTH);
        browseContainer.setBorder(new LineBorder(new Color(220, 220, 220), 1));


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(textFieldCustom, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.gridy = 1;
        add(browseContainer, gbc);

    }


    @Override
    public void detach() {
        // finish jobs
        Settings.path = pathTextField.getText();
    }
}
