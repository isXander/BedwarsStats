package co.uk.isxander.mcstats.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Multithreading {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ScheduledThreadPoolExecutor RUNNABLE_POOL = new ScheduledThreadPoolExecutor(8, (r) -> new Thread(r, "Scheduled Thread " + counter.incrementAndGet()));
    public static ThreadPoolExecutor POOL = new ThreadPoolExecutor(8, 8, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), (r) -> new Thread(r, String.format("Thread %s", counter.incrementAndGet())));

    public static ScheduledFuture<?> schedule(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        return RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit) {
        return RUNNABLE_POOL.schedule(r, delay, unit);
    }

    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public static Future<?> submit(Runnable runnable) {
        return POOL.submit(runnable);
    }

    // TODO: 16/03/2021 Add option in config window to change thread size
    public static void setThreadSize(int size) {
        POOL.setCorePoolSize(size);
        POOL.setMaximumPoolSize(size);
        //RUNNABLE_POOL.setCorePoolSize(size);
    }

}
