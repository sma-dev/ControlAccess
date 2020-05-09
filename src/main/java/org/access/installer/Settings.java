package org.access.installer;

public class Settings {
    public static final String URL = "https://jdroid.ru/downloads/run.jar";
    public static final String EXEC_FILE = "run.jar";
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String DIVIDER = isWindows() ? "\\" : isUnix() ? "/" : "";

    public static String port = "";
    public static String win_path = "C:\\Program Files\\Diplom";
    public static String nix_path = "/opt/Diplom";
    public static String path = "";


    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }
}
