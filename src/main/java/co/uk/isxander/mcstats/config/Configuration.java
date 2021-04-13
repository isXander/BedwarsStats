package co.uk.isxander.mcstats.config;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.licensing.SerialValidator;
import co.uk.isxander.mcstats.utils.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Configuration {

    public static final int CONFIG_VERSION = 1;

    public String licenseKey;
    private final File configFile;
    public String apiKey;
    public File minecraftFolder;
    public File lunarFolder;
    public boolean trackLobbies;
    public ThreadCount speed;

    public Configuration(File configFile) {
        this.configFile = configFile;

        setDefaults();
    }

    public void setDefaults() {
        OsCheck.OSType os = OsCheck.getOperatingSystemType();
        if (os == OsCheck.OSType.Windows) {
            minecraftFolder = new File(System.getenv("APPDATA") + "/.minecraft");
            lunarFolder = new File(System.getProperty("user.home") + "/.lunarclient");
        } else if (os == OsCheck.OSType.MacOS) {
            minecraftFolder = new File("~/Library/Application Support/minecraft");
            lunarFolder = new File("~/.lunarclient");
        } else if (os == OsCheck.OSType.Linux) {
            minecraftFolder = new File("~/.minecraft");
            lunarFolder = new File("~/.lunarclient");
        } else {
            minecraftFolder = new File("");
            lunarFolder = new File("");
        }
        licenseKey = "";
        apiKey = "";
        speed = ThreadCount.DEFAULT;
        trackLobbies = false;
    }

    public void save() {
        Log.info("Saving...");
        BetterJsonObject root = new BetterJsonObject();
        try {
            root.addProperty("CONFIG_VERSION", CONFIG_VERSION);
            root.addProperty("license_key", Base64.getEncoder().encodeToString(licenseKey.getBytes(StandardCharsets.UTF_8)));
            root.addProperty("api_key", apiKey);
            root.addProperty("mc_folder", minecraftFolder.getCanonicalPath());
            root.addProperty("lc_folder", lunarFolder.getCanonicalPath());
            root.addProperty("track_lobbies", trackLobbies);
            root.addProperty("speed", speed.ordinal());
            McStats.setLicense(SerialValidator.getLicense(licenseKey));
            Multithreading.setThreadSize(speed.getThreadCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.info("Attempting to write to file...");
        root.writeToFile(configFile);
    }

    public boolean load() {
        BetterJsonObject root;
        try {
            Log.info("Attempting to get data from file...");
            root = BetterJsonObject.getFromFile(configFile);
        } catch (Exception e) {
            Log.warn("Failed to access file. This is normal if this is your first launch.");
            save();
            return false;
        }
        Log.info("Success. Loading configuration.");
        if (root.optInt("CONFIG_VERSION") != CONFIG_VERSION) {
            Log.warn("Config version does not match. Setting to defaults.");
            save();
            return false;
        }
        licenseKey = new String(Base64.getDecoder().decode(root.optString("license_key")), StandardCharsets.UTF_8);
        apiKey = root.optString("api_key");
        minecraftFolder = new File(root.optString("mc_folder"));
        lunarFolder = new File(root.optString("lc_folder"));
        trackLobbies = root.optBoolean("track_lobbies");
        speed = ThreadCount.values()[root.optInt("speed")];

        McStats.setLicense(SerialValidator.getLicense(licenseKey));
        Multithreading.setThreadSize(speed.getThreadCount());

        return true;
    }

    public File getConfigFile() {
        return configFile;
    }

}
