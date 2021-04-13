package co.uk.isxander.mcstats.handlers.clients;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.utils.FileWatcher;
import co.uk.isxander.mcstats.utils.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public enum Client {
    VANILLA(McStats.getConfig().minecraftFolder.getPath() + "/logs/latest.log", "Vanilla/Forge"),
    BADLION_CLIENT(McStats.getConfig().minecraftFolder.getPath() + "/logs/blclient/minecraft/latest.log", "Badlion Client"),
    LUNAR_CLIENT_1_8(McStats.getConfig().lunarFolder.getPath() + "/offline/1.8/logs/latest.log", "Lunar Client (1.8)"),
    LUNAR_CLIENT_1_7(McStats.getConfig().lunarFolder.getPath() + "/offline/1.7/logs/latest.log", "Lunar Client (1.7)"),
    LUNAR_CLIENT_1_12(McStats.getConfig().lunarFolder.getPath() + "/offline/1.12/logs/latest.log", "Lunar Client (1.12)"),
    LUNAR_CLIENT_1_15(McStats.getConfig().lunarFolder.getPath() + "/offline/1.15/logs/latest.log","Lunar Client (1.15)"),
    LUNAR_CLIENT_1_16(McStats.getConfig().lunarFolder.getPath() + "/offline/1.16/logs/latest.log", "Lunar Client (1.16)");

    private final String logPath;
    private final String displayName;

    Client(String logPath, String displayName) {
        this.logPath = logPath;
        this.displayName = displayName;
    }

    public File getLogFile() {
        return new File(logPath);
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Client getClient() {
        Timer timer = new Timer();
        final Client[] client = {null};

        List<FileWatcher> watchers = new ArrayList<>();
        int i = 0;
        for (Client c : Client.values()) {
            int finalI = i;
            watchers.add(new FileWatcher(c.getLogFile()) {
                final int index = finalI;
                @Override
                protected void onChange(File file) {
                    timer.cancel();
                    client[0] = Client.values()[index];
                }
            });
            ++i;
        }

        for (FileWatcher watcher : watchers) {
            timer.schedule(watcher, 100, 20000);
        }

        if (client[0] == null)
            Log.err("Could not detect any running clients.");
        return client[0];
    }
}
