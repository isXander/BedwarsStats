package co.uk.isxander.mcstats.api;

import co.uk.isxander.mcstats.utils.BetterJsonObject;

public class Player {

    private String name;
    private String uuid;
    private Rank rank;

    private boolean sniper;
    private int reports;
    private boolean party;
    private boolean skilled;
    private boolean nicked;

    private int stars;
    private int finalKills;
    private int finalDeaths;
    private int wins;
    private int losses;
    private int winstreak;
    private int bedsBroken;
    private int bedsLost;

    private boolean success;

    public Player(BetterJsonObject hypixel, BetterJsonObject bwOverlay, boolean success) {
        name = hypixel.optString2("player.displayname", "Unknown");
        uuid = hypixel.optString2("player.uuid", "");

        nicked = bwOverlay.optBoolean("nicked", false);
        if (nicked || !success) {
            sniper = party = skilled = false;
            reports = bedsBroken = bedsLost = finalKills = finalDeaths = wins = losses = winstreak = 0;
            stars = 1;
        } else {
            nicked = false;
            sniper = bwOverlay.optBoolean("sniper", false);
            reports = bwOverlay.optInt("report", 0);
            party = false;
            if (hypixel.has2("player.channel"))
                party = hypixel.optString2("player.channel").equals("PARTY");
            stars = hypixel.optInt2("player.achievements.bedwars_level", 1);
            wins = hypixel.optInt2("player.stats.Bedwars.wins_bedwars", 0);
            losses = hypixel.optInt2("player.stats.Bedwars.losses_bedwars", 0);
            finalKills = hypixel.optInt2("player.stats.Bedwars.final_kills_bedwars", 0);
            finalDeaths = hypixel.optInt2("player.stats.Bedwars.final_deaths_bedwars", 0);
            bedsBroken = hypixel.optInt2("player.stats.Bedwars.beds_broken_bedwars", 0);
            bedsLost = hypixel.optInt2("player.stats.Bedwars.beds_lost_bedwars", 0);
            skilled = getFinalKillDeathRatio() > 4f && getWinLossRatio() > 4f && getStars() > 100;
            winstreak = hypixel.optInt2("player.stats.Bedwars.winstreak", 0);
        }
        rank = calculateRank(hypixel);
        this.success = success;
    }

    private Rank calculateRank(BetterJsonObject hypixelJson) {
        if (hypixelJson.has2("player.rank"))
            return Rank.valueOf(hypixelJson.optString2("player.rank"));
        else if (hypixelJson.has2("player.monthlyPackageRank") && !hypixelJson.optString2("player.monthlyPackageRank").equals("NONE"))
            return Rank.valueOf(hypixelJson.optString2("player.monthlyPackageRank"));
        else if (hypixelJson.has2("player.newPackageRank"))
            return Rank.valueOf(hypixelJson.optString2("player.newPackageRank"));
        else if (hypixelJson.has2("player.packageRank"))
            return Rank.valueOf(hypixelJson.optString2("player.packageRank"));
        else
            return Rank.NONE;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isSniper() {
        return sniper;
    }

    public int getReportAmount() {
        return reports;
    }

    public boolean isParty() {
        return party;
    }

    public boolean isSkilled() {
        return skilled;
    }

    public boolean isNicked() {
        return nicked;
    }

    public int getStars() {
        return stars;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public int getFinalDeaths() {
        return finalDeaths;
    }

    public float getFinalKillDeathRatio() {
        if (getFinalKills() == 0 || getFinalDeaths() == 0)
            return -1;

        return (float) getFinalKills() / (float) getFinalDeaths();
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public float getWinLossRatio() {
        if (getWins() == 0 || getFinalDeaths() == 0)
            return -1;

        return (float) getWins() / (float) getLosses();
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public int getBedsLost() {
        return bedsLost;
    }

    public float getBedBreakLossRatio() {
        if (getBedsBroken() == 0 || getBedsLost() == 0)
            return -1;

        return (float) getBedsBroken() / (float) getBedsLost();
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isSuccess() {
        return success;
    }
}
