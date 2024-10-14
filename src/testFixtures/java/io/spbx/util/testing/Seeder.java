package io.spbx.util.testing;

import com.google.common.flogger.util.CallerFinder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class Seeder {
    private final ConcurrentHashMap<Object, AtomicLong> seeds = new ConcurrentHashMap<>();

    public long nextSeedForCallerOf(@NotNull Class<?> klass) {
        StackTraceElement caller = CallerFinder.findCallerOf(klass, 0);
        assert caller != null : "Caller `%s` not found".formatted(klass);
        AtomicLong seed = seeds.computeIfAbsent(caller, key -> new AtomicLong(0));
        return seed.incrementAndGet();
    }

    public long nextSeedForThisCallSite() {
        return nextSeedForCallerOf(Seeder.class);
    }

    public @NotNull Random nextSeededRandomForCallerOf(@NotNull Class<?> klass) {
        return new Random(nextSeedForCallerOf(klass));
    }

    public @NotNull Random nextSeededRandomForThisCallSite() {
        return new Random(nextSeedForThisCallSite());
    }
}
