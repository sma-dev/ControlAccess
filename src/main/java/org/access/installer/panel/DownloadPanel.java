package org.access.installer.panel;

import org.access.installer.InstallerFrame;
import org.access.installer.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class DownloadPanel extends Panel {
    public DownloadPanel() {
        setBackground(Color.RED);
        JButton next = new JButton("Next");
        next.addActionListener(e -> {
            try {
                if (Settings.isWindows()) {
                    File startBatch = new File(System.getenv("APPDATA")
                            + Settings.DIVIDER + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Diplom.bat");
                    if (startBatch.createNewFile() && startBatch.setExecutable(true)) {
                        FileWriter fileWriter = new FileWriter(startBatch.getAbsoluteFile());
                        PrintWriter printWriter = new PrintWriter(fileWriter);
                        printWriter.print("java -jar " + Settings.path + Settings.DIVIDER + Settings.EXEC_FILE + " " + Settings.port);
                        printWriter.close();
                    }

                    Runtime.
                            getRuntime().
                            exec("cmd /c start \"\" " + startBatch.getAbsolutePath());
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            InstallerFrame.getContext().navigate(InstallerFrame.PanelID.EXIT);
        });
        add(next);

        final JProgressBar current = new JProgressBar(0, 100);
        current.setSize(50, 100);
        current.setValue(0);
        current.setStringPainted(true);
        add(current);
        final Worker worker = new Worker(Settings.URL, new File(Settings.path + Settings.DIVIDER + Settings.EXEC_FILE));
        worker.addPropertyChangeListener(pcEvt -> {
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
