package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * A util enum class representing the epoch start for date calculations.
 * Each value is bound to a particular {@link Instant} timestamp.
 */
public enum Epoch {
    /** <code>1970-01-01T00:00:00Z</code> */
    DEFAULT(0),
    /** <code>2000-01-01T00:00:00Z</code> */
    MILLENNIAL(946_684_800L),
    /** <code>1900-01-01T00:00:00Z</code> */
    XX_CENTURY(-2_208_988_800L);

    private final long seconds;

    Epoch(long seconds) {
        this.seconds = seconds;
    }

    public long seconds() {
        return seconds;
    }

    public @NotNull Instant instant() {
        return Instant.ofEpochSecond(seconds);
    }

    public @NotNull Timestamp timestamp() {
        return new Timestamp(seconds * 1000);
    }
}
