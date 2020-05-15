package org.access.installer.panel;

import org.access.installer.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class DownloadPanel extends Panel {

    final JProgressBar current = new JProgressBar(0, 100);

    @Override
    public void attach(JPanel container) {
        super.attach(container);

        final Worker worker = new Worker(Settings.URL, new File(Settings.path + Settings.SEPARATOR + Settings.EXEC_FILE));
        worker.addPropertyChangeListener(pcEvt -> {
            System.out.println(pcEvt.getPropertyName());
            if ("progress".equals(pcEvt.getPropertyName())) {
                current.setValue((Integer) pcEvt.getNewValue());
            } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                try {
                    worker.get();

                } catch (InterruptedException | ExecutionException e) {
                    // handle any errors here
                    e.printStackTrace();
                }
            }

        });
        worker.execute();
    }

    public DownloadPanel() {
        setBackground(Settings.BG_COLOR);
        setPreferredSize(new Dimension(512, 318));
        current.setSize(50, 100);
        current.setValue(0);
        current.setStringPainted(true);
        add(current);
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

                final String dosCommand = "cmd /c \"C:\\users\\serg7\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Diplom.vbs\"";

                Runtime.
                        getRuntime().
                        exec("cmd /c \"" + startVBS + "\"");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    static class Worker extends SwingWorker<Void, Void> {
        private String site;
        private File file;

        public Worker(String site, File file) {
            this.site = site;
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            int contentLength = connection.getContentLength();
            int totalDataRead = 0;
            try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(
                    connection.getInputStream())) {
                Files.createDirectories(Paths.get(Settings.path));
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                try (java.io.BufferedOutputStream bout = new BufferedOutputStream(
                        fos, 1024)) {
                    byte[] data = new byte[1024];
                    int i;
                    while ((i = in.read(data, 0, 1024)) >= 0) {
                        totalDataRead = totalDataRead + i;
                        bout.write(data, 0, i);
                        int percent = (totalDataRead * 100) / contentLength;
                        setProgress(percent);
                    }
                }
            }
            return null;
        }
    }
}
