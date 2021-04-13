package co.uk.isxander.mcstats.utils;

public enum ThreadCount {
    VERY_SLOW(2),
    SLOW(4),
    DEFAULT(8),
    FAST(12),
    VERY_FAST(16);

    private final int threads;
    ThreadCount(int threads) {
        this.threads = threads;
    }

    public int getThreadCount() {
        return threads;
    }

}
