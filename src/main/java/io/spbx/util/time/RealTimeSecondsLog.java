package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

import static io.spbx.util.time.SecondsTimestamp.instantToSeconds32;

public class RealTimeSecondsLog {
    private final RealTimeLog realTimeLog;

    public RealTimeSecondsLog(@NotNull RealTimeLog realTimeLog) {
        this.realTimeLog = realTimeLog;
    }

    public int size() {
        return realTimeLog.size();
    }

    public boolean isEmpty() {
        return realTimeLog.isEmpty();
    }

    public void append(@NotNull Instant instant) {
        realTimeLog.append(instantToSeconds32(instant));
    }

    public void append(long epochMillis) {
        realTimeLog.append(millisToSeconds(epochMillis));
    }

    public int countAfter(@NotNull Instant instant) {
        return realTimeLog.countGreaterThan(instantToSeconds32(instant));
    }

    public int countAfter(long epochMillis) {
        return realTimeLog.countGreaterThan(millisToSeconds(epochMillis));
    }

    public int countInRange(@NotNull Instant from, @NotNull Instant to) {
        return realTimeLog.countInRange(instantToSeconds32(from), instantToSeconds32(to));
    }

    public int countInRange(long epochMillisFrom, long epochMillisTo) {
        return realTimeLog.countInRange(millisToSeconds(epochMillisFrom), millisToSeconds(epochMillisTo));
    }

    private static int millisToSeconds(long epochMillis) {
        return (int) (epochMillis / 1000);
    }
}
