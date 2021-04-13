package co.uk.isxander.mcstats.utils;

import java.util.regex.Pattern;

public enum Formatting {
    MC_BLACK("\u00A7", "0"),
    MC_DARK_BLUE("\u00A7", "1"),
    MC_DARK_GREEN("\u00A7", "2"),
    MC_DARK_AQUA("\u00A7", "3"),
    MC_DARK_RED("\u00A7", "4"),
    MC_DARK_PURPLE("\u00A7", "5"),
    MC_GOLD("\u00A7", "6"),
    MC_GRAY("\u00A7", "7"),
    MC_DARK_GRAY("\u00A7", "8"),
    MC_BLUE("\u00A7", "9"),
    MC_GREEN("\u00A7", "a"),
    MC_AQUA("\u00A7", "b"),
    MC_RED("\u00A7", "c"),
    MC_LIGHT_PURPLE("\u00A7", "d"),
    MC_YELLOW("\u00A7", "e"),
    MC_WHITE("\u00A7", "f"),

    MC_BOLD("\u00A7", "l"),
    MC_STRIKETHROUGH("\u00A7", "m"),
    MC_UNDERLINE("\u00A7", "n"),
    MC_ITALIC("\u00A7", "o"),
    MC_RESET("\u00A7", "r"),

    MC_CHAR("\u00A7", null),

    ANSI_RESET("\u001B", "[0m"),
    ANSI_BLACK("\u001B", "[30m"),
    ANSI_RED("\u001B", "[31m"),
    ANSI_GREEN("\u001B", "[32m"),
    ANSI_YELLOW("\u001B", "[33m"),
    ANSI_BLUE("\u001B", "[34m"),
    ANSI_PURPLE("\u001B", "[35m"),
    ANSI_CYAN("\u001B", "[36m"),
    ANSI_WHITE("\u001B", "[37m"),

    ANSI_CHAR("\u001B", null);

    private final String escapeCharacter;
    private final String formatCode;
    Formatting(String escapeCharacter, String formatCode) {
        this.escapeCharacter = escapeCharacter;
        this.formatCode = formatCode;
    }

    @Override
    public String toString() {
        String formatCode = (this.formatCode == null ? "" : this.formatCode);
        return escapeCharacter + formatCode;
    }

    public static String stripColorCodes(String message) {
        Pattern mc = Pattern.compile("(?i)" + MC_CHAR + "[0-9A-FK-ORX]");
        Pattern ansi = Pattern.compile("(?i)" + ANSI_CHAR + "\\[[0-9]+m");

        message = mc.matcher(message).replaceAll("");
        message = ansi.matcher(message).replaceAll("");

        return message;
    }

    public static String translateAlternateMcColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = MC_CHAR.toString().toCharArray()[0];
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}
