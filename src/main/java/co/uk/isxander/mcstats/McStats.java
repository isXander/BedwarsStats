package co.uk.isxander.mcstats;

import co.uk.isxander.mcstats.api.Player;
import co.uk.isxander.mcstats.command.CommandHandler;
import co.uk.isxander.mcstats.command.impl.CommandClear;
import co.uk.isxander.mcstats.command.impl.CommandConfig;
import co.uk.isxander.mcstats.command.impl.CommandFullscreen;
import co.uk.isxander.mcstats.command.impl.CommandPlayerInfo;
import co.uk.isxander.mcstats.config.Configuration;
import co.uk.isxander.mcstats.handlers.WindowManager;
import co.uk.isxander.mcstats.licensing.License;
import co.uk.isxander.mcstats.utils.ApiUtils;
import co.uk.isxander.mcstats.utils.Log;
import co.uk.isxander.mcstats.utils.Multithreading;
import co.uk.isxander.mcstats.window.impl.SetupWindow;
import co.uk.isxander.mcstats.window.impl.MainWindow;
import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class McStats {

    private static Configuration config;
    private static License license = null;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = new Configuration(new File("./config.json"));
        boolean loaded = config.load();
        registerCommands();
        Multithreading.setThreadSize(config.speed.getThreadCount());
        if (loaded) {
            new MainWindow().open();
        } else {
            new SetupWindow() {
                @Override
                protected void onClose() {
                    new MainWindow().open();
                }
            }.open();
        }
        Multithreading.schedule(() -> {
            if (MainWindow.getInstance() != null) {
                MainWindow.getInstance().update();
            }
        }, 0, 5, TimeUnit.MILLISECONDS);
    }

    private static void registerCommands() {
        CommandHandler.getInstance().registerCommand(new CommandClear());
        CommandHandler.getInstance().registerCommand(new CommandConfig());
        CommandHandler.getInstance().registerCommand(new CommandPlayerInfo());
        CommandHandler.getInstance().registerCommand(new CommandFullscreen());
    }

    public static Configuration getConfig() {
        return config;
    }

    public static License getLicense() {
        return license;
    }

    public static void setLicense(License license) {
        McStats.license = license;
    }

//    public static void cachePlayer(String username) {
//        if (!cachedPlayers.containsKey(username) || cachedPlayers.get(username) == null || !cachedPlayers.get(username).isSuccess()) {
//            Log.warn(username + ": Adding");
//            cachedPlayers.put(username, null);
//            Multithreading.runAsync(() -> {
//                cachedPlayers.put(username, ApiUtils.getApiPlayer(username));
//                MainWindow.getInstance().updateDisplay();
//            });
//        }
//    }
//
//    public static void foreachPlayer(BiConsumer<? super String, ? super Player> action) {
//        cachedPlayers.forEach(action);
//    }
//
//    public static Player getPlayer(String username) {
//        return cachedPlayers.get(username);
//    }
//
//    public static Player removePlayer(String username) {
//        Player player = cachedPlayers.remove(username);
//        MainWindow.getInstance().updateDisplay();
//        return player;
//    }
//
//    public static void clearCache() {
//        cachedPlayers.clear();
//        MainWindow.getInstance().updateDisplay();
//    }
//
//    public static int cacheSize() {
//        return cachedPlayers.size();
//    }
}
