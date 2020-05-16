package org.access.installer.panel;

import org.access.installer.Settings;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

    final JProgressBar current = new JProgressBar(0, 400);

    @Override
    public void attach(JPanel container) {
        super.attach(container);

        final Worker worker = new Worker(Settings.URL, new File(Settings.path + Settings.SEPARATOR + Settings.EXEC_FILE));
        worker.addPropertyChangeListener(pcEvt -> {
            System.out.println(pcEvt.getPropertyName());
            if ("progress".equals(pcEvt.getPropertyName())) {
                current.setValue((Integer) pcEvt.getNewValue());
                System.out.println(current.getValue());

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
        current.setPreferredSize(new Dimension(400, 30));
        current.setValue(0);
        current.setMaximum(100);
        current.setStringPainted(true);
        current.setBorder(new LineBorder(new Color(0, 120, 215), 3));
        add(current);
    }

    @Override
    public void detach() {

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
