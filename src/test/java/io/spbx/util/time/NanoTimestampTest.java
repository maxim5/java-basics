package io.spbx.util.time;

import io.spbx.util.testing.AssertReverse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

@Tag("fast")
public class NanoTimestampTest {
    @Test
    public void instant_roundtrip() {
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.566443700Z"));
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.000000000Z"));
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.999999999Z"));
        assertRoundtrip(Instant.parse("1970-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("1970-01-09T17:58:38.071544600Z"));
        assertRoundtrip(Instant.parse("2070-12-15T17:59:15.566443700Z"));
        assertRoundtrip(Instant.parse("2100-12-08T18:01:03.533116200Z"));
        assertRoundtrip(Instant.parse("2200-12-08T18:01:03.533116200Z"));
        assertRoundtrip(Instant.parse("2240-12-08T18:01:03.533116200Z"));
        assertRoundtrip(Instant.parse("2510-12-08T18:01:03.533116200Z"));
    }

    private static void assertRoundtrip(@NotNull Instant instant) {
        AssertReverse.assertRoundtrip(NanoTimestamp::instantToNanos64,
                                      NanoTimestamp::nanos64ToInstant,
                                      instant);
    }
}
