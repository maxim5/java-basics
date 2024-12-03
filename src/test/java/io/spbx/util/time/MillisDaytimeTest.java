package io.spbx.util.time;

import io.spbx.util.testing.AssertReverse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

@Tag("fast")
public class MillisDaytimeTest {
    @Test
    public void localTime_roundtrip() {
        assertRoundtrip(LocalTime.of( 0,  0,  0,           0));
        assertRoundtrip(LocalTime.of( 0,  0,  0,   1_000_000));
        assertRoundtrip(LocalTime.of( 1,  0,  0,   1_000_000));
        assertRoundtrip(LocalTime.of( 0,  0,  0, 999_000_000));
        assertRoundtrip(LocalTime.of(11, 59, 59, 999_000_000));
        assertRoundtrip(LocalTime.of(12,  0,  0,   1_000_000));
        assertRoundtrip(LocalTime.of(12, 30, 15, 777_000_000));
        assertRoundtrip(LocalTime.of(18,  0,  0,           0));
        assertRoundtrip(LocalTime.of(23, 59, 59, 999_000_000));
    }

    private static void assertRoundtrip(@NotNull LocalTime localTime) {
        AssertReverse.assertRoundtrip(MillisDaytime::localTimeToMillis32, MillisDaytime::millis32ToLocalTime, localTime);
    }
}
