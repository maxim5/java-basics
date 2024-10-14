package io.spbx.util.time;

import io.spbx.util.testing.AssertReverse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
public class EpochYear8Test {
    @Test
    public void default_roundtrip() {
        assertEpochYear(EpochYear8.DEFAULT_SIGNED).roundtrip(1842);
        assertEpochYear(EpochYear8.DEFAULT_SIGNED).roundtrip(1900);
        assertEpochYear(EpochYear8.DEFAULT_SIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.DEFAULT_SIGNED).roundtrip(2097);

        assertEpochYear(EpochYear8.DEFAULT_UNSIGNED).roundtrip(1970);
        assertEpochYear(EpochYear8.DEFAULT_UNSIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.DEFAULT_UNSIGNED).roundtrip(2100);
        assertEpochYear(EpochYear8.DEFAULT_UNSIGNED).roundtrip(2225);
    }

    @Test
    public void millennial_roundtrip() {
        assertEpochYear(EpochYear8.MILLENNIAL_SIGNED).roundtrip(1872);
        assertEpochYear(EpochYear8.MILLENNIAL_SIGNED).roundtrip(1900);
        assertEpochYear(EpochYear8.MILLENNIAL_SIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.MILLENNIAL_SIGNED).roundtrip(2127);

        assertEpochYear(EpochYear8.MILLENNIAL_UNSIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.MILLENNIAL_UNSIGNED).roundtrip(2100);
        assertEpochYear(EpochYear8.MILLENNIAL_UNSIGNED).roundtrip(2200);
        assertEpochYear(EpochYear8.MILLENNIAL_UNSIGNED).roundtrip(2255);
    }

    @Test
    public void xx_century_roundtrip() {
        assertEpochYear(EpochYear8.XX_CENTURY_SIGNED).roundtrip(1772);
        assertEpochYear(EpochYear8.XX_CENTURY_SIGNED).roundtrip(1800);
        assertEpochYear(EpochYear8.XX_CENTURY_SIGNED).roundtrip(1900);
        assertEpochYear(EpochYear8.XX_CENTURY_SIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.XX_CENTURY_SIGNED).roundtrip(2027);

        assertEpochYear(EpochYear8.XX_CENTURY_UNSIGNED).roundtrip(1900);
        assertEpochYear(EpochYear8.XX_CENTURY_UNSIGNED).roundtrip(2000);
        assertEpochYear(EpochYear8.XX_CENTURY_UNSIGNED).roundtrip(2100);
        assertEpochYear(EpochYear8.XX_CENTURY_UNSIGNED).roundtrip(2155);
    }

    private static @NotNull EpochYear8Subject assertEpochYear(@NotNull EpochYear8 value) {
        return new EpochYear8Subject(value);
    }

    private record EpochYear8Subject(@NotNull EpochYear8 value) {
        private void roundtrip(int humanYear) {
            AssertReverse.assertRoundtrip(value::humanYearToEpochYear8, value::epochYear8ToHumanYear, humanYear);
        }
    }
}
