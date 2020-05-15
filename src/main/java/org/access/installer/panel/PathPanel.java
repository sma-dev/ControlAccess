package org.access.installer.panel;

import org.access.installer.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PathPanel extends Panel {
    private final JTextField pathTextField;

    public PathPanel() {
        setBackground(Settings.BG_COLOR);
        setPreferredSize(new Dimension(512, 318));
        pathTextField = new JTextField(30);
        pathTextField.setText(Settings.path);
        add(pathTextField);


        JButton explore = new JButton("Browse");
        explore.addActionListener(e -> {
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
        add(explore);
    }

    @Override
    public void detach() {
        // finish jobs
        Settings.path = pathTextField.getText();
    }
}
