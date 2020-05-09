package org.access.installer;

import org.access.installer.panel.*;
import org.access.installer.panel.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class InstallerFrame extends JFrame {

    private static InstallerFrame context;
    private final HashMap<InstallerFrame.PanelID, Panel> savedFrames = new HashMap<>();

    public InstallerFrame() {
        super("Installer");
        setSize(800, 600);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        navigate(PanelID.WELCOME);

    }

    public static InstallerFrame getContext() {
        if (context == null) {
            context = new InstallerFrame();
        }
        return context;
    }

    public void navigate(InstallerFrame.PanelID panelID) {
        System.out.println(panelID.name());
        switch (panelID) {
            case WELCOME: {
                if (savedFrames.get(PanelID.WELCOME) == null)
                    savedFrames.put(PanelID.WELCOME, new WelcomePanel());
                savedFrames.get(PanelID.WELCOME).attachTo(this);

                break;
            }
            case PORT: {
                if (savedFrames.get(PanelID.PORT) == null)
                    savedFrames.put(PanelID.PORT, new PortSelectPanel());
                savedFrames.get(PanelID.PORT).attachTo(this);

                break;
            }
            case PATH: {
                if (savedFrames.get(PanelID.PATH) == null)
                    savedFrames.put(PanelID.PATH, new PathPanel());
                savedFrames.get(PanelID.PATH).attachTo(this);

                break;
            }
            case DOWNLOAD: {
                if (savedFrames.get(PanelID.DOWNLOAD) == null)
                    savedFrames.put(PanelID.DOWNLOAD, new DownloadPanel());
                savedFrames.get(PanelID.DOWNLOAD).attachTo(this);

                break;
            }
            case EXIT:
                System.exit(0);
        }
    }

    public enum PanelID {
        WELCOME,
        PORT,
        PATH,
        EXIT, DOWNLOAD
    }

}
