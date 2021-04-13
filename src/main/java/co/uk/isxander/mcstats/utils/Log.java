package co.uk.isxander.mcstats.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    private static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    private static String getDate() {
        return df.format(Calendar.getInstance().getTime());
    }

    public static void info(String message) {
        System.out.printf("[%s] [%s/INFO] [McStats]: %s\n%s", getDate(), Thread.currentThread().getName(), message, Formatting.ANSI_RESET);
    }

    public static void warn(String message) {
        System.out.printf("%s[%s] [%s/WARN] [McStats]: %s\n%s", Formatting.ANSI_YELLOW, getDate(), Thread.currentThread().getName(), message, Formatting.ANSI_RESET);
    }

    public static void err(String message) {
        System.out.printf("%s[%s] [%s/ERROR] [McStats]: %s\n%s", Formatting.ANSI_RED, getDate(), Thread.currentThread().getName(), message, Formatting.ANSI_RESET);
    }

}
