package co.uk.isxander.mcstats.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineFormatter {

    public static String get(String text, int stringLength, int maxLength, String cutoffString) {
        int colorCount = 0;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == Formatting.MC_CHAR.toString().toCharArray()[0] && "0123456789abcdefklmnor".contains(Character.toString(chars[i + 1]).toLowerCase())) {
                colorCount++;
                // Skip the color code because we already checked
                i++;
            }
        }

        stringLength += colorCount * 2;
        maxLength += colorCount * 2;

        char[] empty = new char[maxLength];
        Arrays.fill(empty, ' ');

        for (int i = 0; i < empty.length; i++) {
            if (i >= stringLength - cutoffString.length()) {
                for (int j = 0; j < cutoffString.length(); j++) {
                    empty[i + j] = cutoffString.charAt(j);
                }
                break;
            }
            if (i >= chars.length) {
                break;
            }
            empty[i] = chars[i];
        }

        return new String(empty);
    }

}
