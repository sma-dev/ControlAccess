package org.access.installer.panel;

import org.access.installer.Settings;
import org.access.installer.TextFieldCustom;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Objects;

public class FinishPanel extends Panel {


    public FinishPanel() {
        setBackground(Color.WHITE);

        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        setPreferredSize(new Dimension(512, 314));
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
        final URL url = Thread.currentThread().getContextClassLoader().getResource("org\\access\\welcome.png");

        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("welcome.png")))
                        .getImage().getScaledInstance(209, 314, Image.SCALE_DEFAULT));
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
        try {
            if (Settings.isWindows()) {
                File startVBS = new File(System.getenv("APPDATA")
                        + Settings.SEPARATOR + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Diplom.vbs");
                System.out.println(startVBS.getAbsolutePath());
                FileWriter fileWriter = new FileWriter(startVBS.getAbsoluteFile(), false);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.print("Dim WShell\n" +
                        "Set WShell = CreateObject(\"WScript.Shell\")\n" +
                        "WShell.Run \"java -jar \"&Chr(34)&\"" + Settings.path + Settings.SEPARATOR + Settings.EXEC_FILE
                        + "\"&Chr(34)&\" COM3\", 0\n" +
                        "Set WShell = Nothing\n" +
                        "MsgBox(\"Daemon Started!\")");
                printWriter.close();
                Runtime.
                        getRuntime().
                        exec("cmd /c \"" + startVBS + "\"");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}