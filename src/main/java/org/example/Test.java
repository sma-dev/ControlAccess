package org.example;

import jssc.SerialPortList;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

public class Test {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String PATH_DIVIDER = "";
    private static String JAR_TITLE = "Run.jar";

    public static void main(String[] args) throws IOException {
        System.out.println(getSerialPorts());


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
