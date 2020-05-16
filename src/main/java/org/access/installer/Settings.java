package org.access.installer;

import java.awt.*;
import java.net.URL;

public class Settings {
    public static final String URL = "https://jdroid.ru/downloads/run.jar";
    public static final String EXEC_FILE = "run.jar";
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final Color BG_COLOR = new Color(240, 240, 240);
    public static final Color SEPA_COLOR = new Color(160, 160, 160);


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
