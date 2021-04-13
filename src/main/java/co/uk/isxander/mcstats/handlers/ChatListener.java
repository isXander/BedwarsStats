package co.uk.isxander.mcstats.handlers;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.command.CommandHandler;
import co.uk.isxander.mcstats.handlers.clients.Client;
import co.uk.isxander.mcstats.utils.*;
import co.uk.isxander.mcstats.window.impl.MainWindow;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

    private Client currentClient = null;
    private int lineCount;
    private Timer timer = new Timer();

    public void start() {
        startClientChecker();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentClient == null) return;

                try {
                    // Minecraft Logs use ANSI encoding (Windows 1252)
                    List<String> lines = Files.readAllLines(currentClient.getLogFile().toPath(), Charset.forName("windows-1252"));
                    int newLineCount = lines.size();
                    int lineDiff = newLineCount - lineCount;
                    if (lineDiff > 0 && lineCount != -1) {
                        for (int i = lineCount; i < newLineCount; i++) {
                            String line = lines.get(i);
                            if (isLogLineChat(line)) {
                                onChat(line.substring(40));
                            }
                        }
                    }
                    lineCount = newLineCount;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Date(), 100);
    }

    private void startClientChecker() {
        List<FileWatcher> watchers = new ArrayList<>();
        int i = 0;
        for (Client c : Client.values()) {
            int finalI = i;
            watchers.add(new FileWatcher(c.getLogFile()) {
                final int index = finalI;
                @Override
                protected void onChange(File file) {
                    Client newClient = Client.values()[index];
                    if (newClient != currentClient) {
                        MainWindow.getInstance().addWarning(new Warning(1, Formatting.MC_GREEN + "Connected to: " + newClient.getDisplayName(), 5000));
                        currentClient = newClient;
                        lineCount = -1;
                    }
                }
            });
            ++i;
        }

        for (FileWatcher watcher : watchers) {
            timer.scheduleAtFixedRate(watcher, new Date(), 2500);
        }
    }

    private boolean isLogLineChat(String logLine) {
        // starts with [00:00:00] [main/INFO]: [CHAT] some message
        return logLine.matches("^\\[[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\\] \\[(main|Client thread)\\/INFO\\]: \\[CHAT\\] .*$");
    }

    public static final Pattern PLAYER_CHAT = Pattern.compile("^(\\[[0-9]+\\?\\] |)(\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)\\+*\\] |)([a-zA-Z0-9_]+): .*$");
    public static final Pattern LOBBY_JOIN = Pattern.compile("^( ?>>> |)\\[MVP\\+\\+?\\] ([a-zA-Z0-9_]+) joined the lobby!( <<< ?|)*$");
    public static final Pattern BEDWARS_JOIN = Pattern.compile("^([a-zA-Z0-9_]+) has joined \\(([0-9]+)\\/[0-9]+\\)!$");
    // TODO: 22/03/2021 add hytilities message support
    public static final Pattern BEDWARS_JOIN_HYTILITIES_RESTYLE = Pattern.compile("^\\+ ([a-zA-Z0-9_]+) \\(([0-9]+)\\/[0-9]+\\)$");
    public static final Pattern BEDWARS_LEAVE = Pattern.compile("^([a-zA-Z0-9_]+) has quit( \\(([0-9]+)\\/[0-9]+\\)|)!$");
    public static final Pattern BEDWARS_LEAVE_HYTILITIES_RESTYLE = Pattern.compile("^- ([a-zA-Z0-9_]+) \\(([0-9]+)\\/[0-9]+\\)$");
    public static final Pattern MESSAGE_COMMAND = Pattern.compile("^Can't find a player by the name of '(.+)'$");
    public static final Pattern ONLINE_PLAYERS = Pattern.compile("^Online Players\\([0-9]+\\):.*$");
    public static final Pattern WHO_COMMAND = Pattern.compile("");

    private void processMessage(String message) {
        message = Formatting.stripColorCodes(message);

        Matcher chatMatcher = PLAYER_CHAT.matcher(message);
        Matcher lobbyJoinMatcher = LOBBY_JOIN.matcher(message);
        Matcher bedwarsJoin = BEDWARS_JOIN.matcher(message);
        Matcher bedwarsJoinHytilities = BEDWARS_JOIN_HYTILITIES_RESTYLE.matcher(message);
        Matcher bedwarsLeave = BEDWARS_LEAVE.matcher(message);
        Matcher messageCommand = MESSAGE_COMMAND.matcher(message);
        Matcher onlinePlayers = ONLINE_PLAYERS.matcher(message);

        boolean update = true;
        if (messageCommand.matches()) {
            Log.warn("Executing Command: " + messageCommand.group(1));
            CommandHandler.getInstance().onCommand(messageCommand.group(1));
        } else if (message.startsWith("ONLINE: ")) {
            String msg = message.replaceFirst("ONLINE: ", "");
            String[] split = msg.split(", ");
            for (String playerName : split) {
                PlayerHandler.getInstance().cachePlayer(playerName, true);
            }
            Warning warning = MainWindow.getInstance().getWarning(0);
            if (warning != null) {
                MainWindow.getInstance().removeWarning(warning.getId());
            }
        } else if (onlinePlayers.matches()) {
            String msg = message.replaceFirst("Online Players\\([0-9]+\\): ", "");
            msg = msg.replaceAll("\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)\\+*\\] ", "");
            String[] split = msg.split(", ");
            for (String playerName : split) {
                PlayerHandler.getInstance().cachePlayer(playerName, true);
            }
        } else if (bedwarsJoin.matches()) {
            PlayerHandler.getInstance().cachePlayer(bedwarsJoin.group(1), true);
            // Calculate how many players are missing
            int current = Integer.parseInt(bedwarsJoin.group(2));
            int missing = current - PlayerHandler.getInstance().getCacheSize();
            if (missing > 0) {
                MainWindow.getInstance().addWarning(new Warning(0, "Players out of sync. Do /who to sync. (" + missing + ")", 10000));
            } else {
                Warning warning = MainWindow.getInstance().getWarning(0);
                if (warning != null) {
                    MainWindow.getInstance().removeWarning(warning.getId());
                }
            }
        } else if (bedwarsLeave.matches()) {
            PlayerHandler.getInstance().removePlayer(bedwarsLeave.group(1));
        } else if (McStats.getConfig().trackLobbies && chatMatcher.matches()) {
            PlayerHandler.getInstance().cachePlayer(chatMatcher.group(4), true);
        } else if (McStats.getConfig().trackLobbies && lobbyJoinMatcher.matches()) {
            PlayerHandler.getInstance().cachePlayer(lobbyJoinMatcher.group(2), true);
        } else {
            update = false;
        }

        if (update)
            MainWindow.getInstance().updateDisplay();
    }

    private void checkWorldChange(String message) {
        switch (message) {
            case "                         ":
            case "       ":
            case "You are AFK. Move around to return from AFK.":
                MainWindow.getInstance().clearText();
                MainWindow.getInstance().addWarning(new Warning(3, "Clearing cache!", 2000));
                PlayerHandler.getInstance().clearPlayerCache();
                MainWindow.getInstance().updateDisplay();
                break;
        }
    }

    private void onChat(String message) {
        checkWorldChange(message);
        processMessage(message);
        Log.info("[CHAT]: " + Formatting.stripColorCodes(message));
    }

}
