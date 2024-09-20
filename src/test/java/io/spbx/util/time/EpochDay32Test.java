package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.spbx.util.testing.AssertBasics.assertReversibleRoundtrip;

@Tag("fast")
public class EpochDay32Test {
    @Test
    public void localDate_roundtrip() {
        assertRoundtrip(LocalDate.parse("1000-01-01"));
        assertRoundtrip(LocalDate.parse("1900-01-01"));
        assertRoundtrip(LocalDate.parse("1970-01-01"));
        assertRoundtrip(LocalDate.parse("2000-01-01"));
        assertRoundtrip(LocalDate.parse("2000-12-31"));
        assertRoundtrip(LocalDate.parse("2020-01-01"));
        assertRoundtrip(LocalDate.parse("2023-02-28"));
        assertRoundtrip(LocalDate.parse("2024-02-29"));
        assertRoundtrip(LocalDate.parse("3000-01-01"));
        assertRoundtrip(LocalDate.parse("9999-12-31"));
    }

    private static void assertRoundtrip(@NotNull LocalDate localDate) {
        assertReversibleRoundtrip(EpochDay32::localDateToEpochDay32, EpochDay32::epochDay32ToLocalDate, localDate);
    }
}
