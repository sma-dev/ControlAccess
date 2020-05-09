package org.access.installer;

public class Settings {
    public static final String URL = "https://jdroid.ru/downloads/run.jar";
    public static final String EXEC_FILE = "run.jar";
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String SEPARATOR = System.getProperty("file.separator");


    // %AppData%\Microsoft\Windows\Start Menu\Programs\Startup

    public static String port = "";
    public static String path = isWindows()?"C:\\Program Files\\Diplom":"/opt/Diplom";


    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }
}
