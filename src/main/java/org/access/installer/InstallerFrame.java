package org.access.installer;

import org.access.installer.panel.Panel;
import org.access.installer.panel.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class InstallerFrame extends JFrame {

    private final LinkedList<Panel> installSequence = new LinkedList<>();
    private int pos = 0;
    private JButton cancel;
    private JButton next;
    private JButton back;

    public InstallerFrame() {
        super("Installer");
        setSize(500, 390);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        if (AdminChecker.IS_RUNNING_AS_ADMINISTRATOR) {
            JOptionPane.showMessageDialog(this, "Run program as administrator!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        JPanel content = new JPanel();
        JPanel bottom = new JPanel(new BorderLayout());


        bottom.setBorder(new EmptyBorder(4, 4, 4, 4));

        bottom.setBackground(Settings.BG_COLOR);

        initPanels();

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        installSequence.get(0).attach(content);

        cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            System.exit(0);
        });

        next = new JButton("Next>");
        next.addActionListener(e -> {
            installSequence.get(pos).detach();
            pos++;
            if (pos < installSequence.size()) {
                if (pos == installSequence.size() - 1) {
                    next.setText("Finish");
                    cancel.setVisible(false);
                    back.setVisible(false);
                } else {
                    back.setVisible(true);
                }
                installSequence.get(pos).attach(content);
            } else {
                System.exit(0);
            }
        });
        back = new JButton("<Back");
        back.setVisible(false);
        back.addActionListener(e -> {
            installSequence.get(pos).detach();
            pos--;
            if (pos >= 0) {
                if (pos == 0) {
                    back.setVisible(false);
                }
                installSequence.get(pos).attach(content);
            }
        });

        controls.add(back);
        controls.add(next);
        controls.add(Box.createRigidArea(new Dimension(5, 0)));
        controls.add(cancel);


        bottom.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Settings.SEPA_COLOR);
                g.drawLine(10, 0, getWidth() - 10, 0);
            }
        }, BorderLayout.NORTH);
        bottom.add(controls, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);


        setVisible(true);

    }


    public void blockNext() {
        next.setEnabled(false);
    }

    public void blockCancel() {
        cancel.setEnabled(false);
    }

    private void initPanels() {
        installSequence.add(new WelcomePanel());
        installSequence.add(new PortSelectPanel());
        installSequence.add(new PathPanel());
        installSequence.add(new DownloadPanel());
        installSequence.add(new FinishPanel());
    }

}
