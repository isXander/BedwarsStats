package co.uk.isxander.mcstats.utils;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.api.Player;

import java.io.IOException;

public class ApiUtils {

    public static final String MOJANG_NAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
    public static final String MOJANG_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names";
    public static final String HYPIXEL_PLAYER_DATA_NAME = "https://api.hypixel.net/player?key=%s&name=%s";
    public static final String HYPIXEL_PLAYER_DATA_UUID = "https://api.hypixel.net/player?key=%s&uuid=%s";
    // FIXME: 13/03/2021 Sniper check api now does not work with direct ip access. find out what the domain is
    public static final String BW_OVERLAY_SNIPER_CHECK = "http://104.21.56.58:8080/?playerv5=%s";

    public static String getPlayerUuid(String name) {
        try {
            HttpsUtils.HttpsResponse response = HttpsUtils.getString(String.format(MOJANG_NAME_TO_UUID, name));
            if (response.getResponseCode() != 200) return null;

            BetterJsonObject json = new BetterJsonObject(response.getData());
            return json.optString("id");
        } catch (IOException e) {
            Log.err("Mojang API encountered an exception.");
        }
        return "";
    }

    public static String getPlayerName(String uuid) {
        try {
            HttpsUtils.HttpsResponse response = HttpsUtils.getString(String.format(MOJANG_NAME_HISTORY, uuid));
            if (response.getResponseCode() != 200) return "";

            BetterJsonObject json = new BetterJsonObject(response.getData());
            if (json.has("error")) return "";

            return json.getData().getAsJsonArray().get(0).getAsString();
        } catch (IOException e) {
            Log.err("Mojang API encountered an exception.");
        }

        return "";
    }

    public static Player getApiPlayer(String uuid, String username) {
        boolean success = true;

        BetterJsonObject bwOverlay = new BetterJsonObject();
//        try {
//            HttpsUtils.HttpsResponse bwOverlayResponse = HttpsUtils.getString(String.format(BW_OVERLAY_SNIPER_CHECK, username));
//            bwOverlay = new BetterJsonObject(bwOverlayResponse.getData());
//        } catch (IOException e) {
//            Log.err("Bedwars Overlay API encountered an exception.");
//            e.printStackTrace();
//            success = false;
//        }

        BetterJsonObject hypixel = new BetterJsonObject();
        try {
            HttpsUtils.HttpsResponse hypixelResponse = HttpsUtils.getString(String.format(HYPIXEL_PLAYER_DATA_UUID, McStats.getConfig().apiKey, uuid));
            if (hypixelResponse.getResponseCode() == 200) {
                hypixel = new BetterJsonObject(hypixelResponse.getData());
                bwOverlay.addProperty("nicked", hypixel.getData().get("player").isJsonNull());
            }

        } catch (IOException e) {
            success = false;
            Log.err("Hypixel API Request encountered an exception.");
        }

        return new Player(hypixel, bwOverlay, success);
    }

}
