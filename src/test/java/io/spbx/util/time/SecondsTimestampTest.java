package io.spbx.util.time;

import io.spbx.util.testing.AssertReverse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

@Tag("fast")
public class SecondsTimestampTest {
    @Test
    public void instant_roundtrip() {
        assertRoundtrip(Instant.parse("1970-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("1970-01-09T17:58:38.000000000Z"));
        assertRoundtrip(Instant.parse("2000-01-01T00:00:00.000000000Z"));
        assertRoundtrip(Instant.parse("2021-12-27T17:59:15.000000000Z"));
        assertRoundtrip(Instant.parse("2070-12-15T17:59:15.000000000Z"));
        assertRoundtrip(Instant.parse("2100-12-08T18:01:03.000000000Z"));
        assertRoundtrip(Instant.parse("2106-02-07T06:28:15.000000000Z"));
    }

    private static void assertRoundtrip(@NotNull Instant instant) {
        AssertReverse.assertRoundtrip(SecondsTimestamp::instantToSeconds32,
                                      SecondsTimestamp::seconds32ToInstant,
                                      instant);
    }
}
