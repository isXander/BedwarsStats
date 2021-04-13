package co.uk.isxander.mcstats.handlers;

import co.uk.isxander.mcstats.api.Player;
import co.uk.isxander.mcstats.utils.ApiUtils;
import co.uk.isxander.mcstats.utils.BetterJsonObject;
import co.uk.isxander.mcstats.utils.Multithreading;
import co.uk.isxander.mcstats.window.impl.MainWindow;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class PlayerHandler {

    private static PlayerHandler instance;
    public static PlayerHandler getInstance() {
        if (instance == null)
            instance = new PlayerHandler();
        return instance;
    }

    /* USERNAME - UUID: Temporary cache that stores values for 30 minutes. Useful for caching usernames and UUIDS */
    private final Cache<String, String> nameCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

    /* Acts as the cache for the actual player data. Uses UUIDs as the key as hypixel api has a limit in place for usernames */
    private final Map<String, Player> playerCache = new HashMap<>();

    public void cachePlayer(String username, boolean update) {
        if (playerCache.containsKey(username)) {
            return;
        }
        playerCache.put(username, null);
        Multithreading.runAsync(() -> {
            String uuid = nameCache.getIfPresent(username);
            if (uuid == null) {
                uuid = ApiUtils.getPlayerUuid(username);

                if (uuid == null) {
                    playerCache.put(username, new Player(new BetterJsonObject(), new BetterJsonObject(), false));
                    return;
                }

                nameCache.put(username, uuid);
            }

            Player player = ApiUtils.getApiPlayer(uuid, username);

            // If the entry has been deleted since doing all the api stuff
            if (!playerCache.containsKey(username)) {
                return;
            }

            playerCache.put(username, player);

            if (update) {
                MainWindow.getInstance().updateDisplay();
            }
        });
    }

    public void foreachPlayer(BiConsumer<? super String, ? super Player> action) {
        playerCache.forEach(action);
    }

    public void clearNameUuidCache() {
        nameCache.invalidateAll();
    }

    public void clearPlayerCache() {
        playerCache.clear();
    }

    public void removeName(String name) {
        nameCache.invalidate(name);
    }

    public void removePlayer(String username) {
        playerCache.remove(username);
    }

    public int getCacheSize() {
        return playerCache.size();
    }

}
