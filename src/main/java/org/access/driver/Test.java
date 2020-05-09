package org.access.driver;

import jssc.SerialPortList;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Test {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String PATH_DIVIDER = "";
    private static String JAR_TITLE = "Run.jar";
    private static String JAR_URL = "https://jdroid.ru/downloads/Run.jar";

    // %AppData%\Microsoft\Windows\Start Menu\Programs\Startup
    public static void main(String[] args) {
        System.out.println(getSerialPorts());

        InputStream in = null;
        try {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
            }
            System.out.println("Enter the path to create a directory: ");
            Scanner sc = new Scanner(System.in);
            String path = sc.next();
            System.out.println("Enter the name of the desired a directory: ");
            path = path+sc.next();
            //Creating a File object
            File file = new File(path);
            //Creating the directory
            boolean bool = file.mkdirs();
            if(bool){
                System.out.println("Directory created successfully");
            }else{
                System.out.println("Sorry couldnt create specified directory");
            }

            in = new URL(JAR_URL).openStream();
            Files.copy(in, Paths.get(""), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(OS);
        if (isWindows()) {
            PATH_DIVIDER = "\\";
            System.out.println("This is Windows");
        } else if (isMac()) {
            PATH_DIVIDER = "/";
            System.out.println("This is Mac");
        } else if (isUnix()) {
            PATH_DIVIDER = "/";
            System.out.println("This is Unix or Linux");
        } else {
            System.out.println("Your OS is not support!!");
            System.exit(1);
        }

        String cwd = System.getProperty("user.dir");

        System.out.println(cwd + PATH_DIVIDER + JAR_TITLE);
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }

    public static HashSet<String> getSerialPorts() {
        final HashSet<String> ports = new HashSet<>();
        Collections.addAll(ports, SerialPortList.getPortNames());
        return ports;
    }
}
