package io.spbx.util.time;

import io.spbx.util.testing.AssertReverse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

@Tag("fast")
public class MicroTimestampTest {
    @Test
    public void instant_roundtrip() {
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.566443000Z"));
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.000000000Z"));
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.999999000Z"));
        assertRoundtrip(Instant.parse("1000-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("1900-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("1970-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("1970-01-09T17:58:38.071544000Z"));
        assertRoundtrip(Instant.parse("2000-12-15T17:59:15.566443000Z"));
        assertRoundtrip(Instant.parse("5000-12-08T18:01:03.533116000Z"));
        assertRoundtrip(Instant.parse("9999-12-31T23:59:59.999999000Z"));
    }

    private static void assertRoundtrip(@NotNull Instant instant) {
        AssertReverse.assertRoundtrip(MicroTimestamp::instantToMicros64,
                                      MicroTimestamp::micros64ToInstant,
                                      instant);
    }
}
