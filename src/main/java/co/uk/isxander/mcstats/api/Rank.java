package co.uk.isxander.mcstats.api;

public enum Rank {
    NONE('7'),
    VIP('a'),
    VIP_PLUS('a'),
    MVP('b'),
    MVP_PLUS('b'),
    SUPERSTAR('6'),
    YOUTUBER('c'),
    PIG('d'),
    JR_HELPER('9'),
    HELPER('9'),
    MODERATOR('2'),
    ADMIN('c'),
    OWNER('c');

    private final char colorCode;
    public char getColorCode() {
        return colorCode;
    }
    Rank(char colorCode) {
        this.colorCode = colorCode;
    }
}
