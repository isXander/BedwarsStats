package co.uk.isxander.mcstats.utils;

import co.uk.isxander.mcstats.api.Player;
import com.google.common.base.Joiner;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public String shiftString(String s, int amt) {
        List<Character> chars = new ArrayList<>();
        for (char c : s.toCharArray())
            chars.add(c);

        for (int i = 0; i < amt; i++) {
            Character c = chars.get(chars.size() - 1);
            chars.remove(chars.size() - 1);
            chars.add(0, c);
        }

        return Joiner.on("").join(chars);
    }

    public static String formatStar(int stars) {
        if (stars < 100)
            return Formatting.MC_GRAY + "[" + stars + "]";
        else if (stars < 200)
            return Formatting.MC_WHITE + "[" + stars + "]";
        else if (stars < 300)
            return Formatting.MC_GOLD + "[" + stars + "]";
        else if (stars < 400)
            return Formatting.MC_AQUA + "[" + stars + "]";
        else if (stars < 500)
            return Formatting.MC_DARK_GREEN + "[" + stars + "]";
        else if (stars < 600)
            return Formatting.MC_DARK_AQUA + "[" + stars + "]";
        else if (stars < 700)
            return Formatting.MC_DARK_RED + "[" + stars + "]";
        else if (stars < 800)
            return Formatting.MC_LIGHT_PURPLE + "[" + stars + "]";
        else if (stars < 900)
            return Formatting.MC_BLUE + "[" + stars + "]";
        else if (stars < 1000)
            return Formatting.MC_DARK_PURPLE + "[" + stars + "]";
        else {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            boolean forwards = true;
            String colors = "c6eabd5";
            for (char c : ("[" + stars + "]").toCharArray()) {
                sb.append(Formatting.MC_CHAR).append(colors.toCharArray()[i]).append(c);

                if (i >= colors.length() - 1)
                    forwards = false;
                else if (i < 1)
                    forwards = true;

                if (forwards)
                    i++;
                else
                    i--;
            }
            return sb.toString();
        }
    }

    private static DecimalFormat fkdrFormat = new DecimalFormat("0.0");

    public static String formatFkdr(float fkdr) {
        if (fkdr == -1)
            return Formatting.MC_DARK_GRAY + "N/A";
        else if (fkdr < 1f)
            return Formatting.MC_DARK_GRAY + fkdrFormat.format(fkdr);
        else if (fkdr < 2f)
            return Formatting.MC_GRAY + fkdrFormat.format(fkdr);
        else if (fkdr < 3f)
            return Formatting.MC_WHITE + fkdrFormat.format(fkdr);
        else if (fkdr < 5f)
            return Formatting.MC_GREEN + fkdrFormat.format(fkdr);
        else if (fkdr < 10f)
            return Formatting.MC_DARK_GREEN + fkdrFormat.format(fkdr);
        else if (fkdr < 15f)
            return Formatting.MC_AQUA + fkdrFormat.format(fkdr);
        else if (fkdr < 20f)
            return Formatting.MC_DARK_AQUA + fkdrFormat.format(fkdr);
        else if (fkdr < 25f)
            return Formatting.MC_BLUE + fkdrFormat.format(fkdr);
        else if (fkdr < 30f)
            return Formatting.MC_DARK_BLUE + fkdrFormat.format(fkdr);
        else if (fkdr < 40f)
            return Formatting.MC_YELLOW + fkdrFormat.format(fkdr);
        else if (fkdr < 50f)
            return Formatting.MC_GOLD + fkdrFormat.format(fkdr);
        else if (fkdr < 60f)
            return Formatting.MC_LIGHT_PURPLE + fkdrFormat.format(fkdr);
        else if (fkdr < 75f)
            return Formatting.MC_DARK_PURPLE + fkdrFormat.format(fkdr);
        else if (fkdr < 90)
            return Formatting.MC_RED + fkdrFormat.format(fkdr);
        else
            return Formatting.MC_DARK_RED + fkdrFormat.format(fkdr);
    }

    public static String formatTags(Player player) {
        if (player.isNicked())
            return Formatting.MC_UNDERLINE.toString() + Formatting.MC_DARK_RED + "NICKED" + Formatting.MC_RESET;

        List<String> tags = new ArrayList<>();
        if (player.isParty()) tags.add(Formatting.MC_BLUE + "P");
        if (player.isSniper()) tags.add(Formatting.MC_RED + "S");
        if (player.getReportAmount() > 4) tags.add(Formatting.MC_DARK_RED + "H");
        else if (player.getReportAmount() > 1) tags.add(Formatting.MC_GOLD + "R");
        if (player.isSkilled()) tags.add(Formatting.MC_DARK_PURPLE + "G");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= tags.size() - 1; i++) {
            if (i != 0 && tags.size() != 1) {
                sb.append(Formatting.MC_DARK_GRAY.toString()).append("+");
            }
            sb.append(tags.get(i));
        }

        return sb.toString();
    }

    public static boolean isWindowTitleMinecraft(String title) {
        Matcher vanillaMatcher = Pattern.compile("^Minecraft [0-9]+(\\.[0-9]+)*$").matcher(title);
        Matcher lunarMatcher = Pattern.compile("^Lunar Client \\([0-9]+(\\.[0-9]+)*-[0-9a-zA-Z]+\\/.*\\)$").matcher(title);
        Matcher blcMatcher = Pattern.compile("^Badlion Minecraft Client v[0-9]+(\\.[0-9]+)*-[0-9a-zA-Z]+-[0-9a-zA-Z]+ \\([0-9]+(\\.[0-9]+)*\\)$").matcher(title);

        if (vanillaMatcher.matches())
            return true;
        if (lunarMatcher.matches())
            return true;
        if (blcMatcher.matches())
            return true;

        return false;
    }

    public static int hasDuplicateCharacters(String str, int times) {
        int repeat = 0;
        int index = -1;
        Character lastChar = null;
        for (char c : str.toCharArray()) {
            index++;

            if (lastChar == null) {
                lastChar = c;
                continue;
            }

            if (lastChar == c) {
                repeat++;
            } else {
                repeat = 0;
            }

            if (repeat >= times) {
                return index;
            }

            lastChar = c;
        }

        return -1;
    }

}
