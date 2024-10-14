package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.spbx.util.testing.AssertReverse.assertRoundtrip;

@Tag("fast")
public class EpochDay16Test {
    @Test
    public void default_roundtrip() {
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("1970-01-01"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2000-01-01"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2000-12-31"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2020-01-01"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2023-02-28"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2024-02-29"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2100-01-01"));
        assertEpochDay(EpochDay16.DEFAULT).roundtrip(LocalDate.parse("2149-06-06"));
    }

    @Test
    public void millennial_roundtrip() {
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2000-01-01"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2000-12-31"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2020-01-01"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2023-02-28"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2024-02-29"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2100-01-01"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2149-06-06"));
        assertEpochDay(EpochDay16.MILLENNIAL).roundtrip(LocalDate.parse("2179-06-06"));
    }

    @Test
    public void xx_century_roundtrip() {
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("1900-01-01"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("1945-01-01"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("1970-01-01"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2000-01-01"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2000-12-31"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2020-01-01"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2023-02-28"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2024-02-29"));
        assertEpochDay(EpochDay16.XX_CENTURY).roundtrip(LocalDate.parse("2079-06-06"));
    }

    private static @NotNull EpochDay16Subject assertEpochDay(@NotNull EpochDay16 value) {
        return new EpochDay16Subject(value);
    }

    private record EpochDay16Subject(@NotNull EpochDay16 value) {
        private void roundtrip(@NotNull LocalDate localDate) {
            assertRoundtrip(value::localDateToEpochDay16, value::epochDay16ToLocalDate, localDate);
        }
    }
}
