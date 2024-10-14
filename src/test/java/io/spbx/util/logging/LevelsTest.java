package io.spbx.util.logging;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LevelsTest {
    @Test
    public void binarySearchMinLoggableLevel_simple_tiny() {
        assertThat(Levels.binarySearchMinLoggableLevel(1, 2, predicateFor(1))).isEqualTo(1);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 2, predicateFor(2))).isEqualTo(2);
    }

    @Test
    public void binarySearchMinLoggableLevel_simple_small() {
        assertThat(Levels.binarySearchMinLoggableLevel(1, 3, predicateFor(1))).isEqualTo(1);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 3, predicateFor(2))).isEqualTo(2);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 3, predicateFor(3))).isEqualTo(3);
    }

    @Test
    public void binarySearchMinLoggableLevel_simple_mid() {
        assertThat(Levels.binarySearchMinLoggableLevel(1, 4, predicateFor(1))).isEqualTo(1);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 4, predicateFor(2))).isEqualTo(2);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 4, predicateFor(3))).isEqualTo(3);
        assertThat(Levels.binarySearchMinLoggableLevel(1, 4, predicateFor(4))).isEqualTo(4);
    }

    @Test
    public void binarySearchMinLoggableLevel_simple_mid_from_zero() {
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(0))).isEqualTo(0);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(1))).isEqualTo(1);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(4))).isEqualTo(4);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(5))).isEqualTo(5);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(6))).isEqualTo(6);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(9))).isEqualTo(9);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 10, predicateFor(10))).isEqualTo(10);
    }

    @Test
    public void binarySearchMinLoggableLevel_simple_large_from_zero() {
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(0))).isEqualTo(0);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(1))).isEqualTo(1);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(100))).isEqualTo(100);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(499))).isEqualTo(499);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(500))).isEqualTo(500);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(501))).isEqualTo(501);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(700))).isEqualTo(700);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(999))).isEqualTo(999);
        assertThat(Levels.binarySearchMinLoggableLevel(0, 1000, predicateFor(1000))).isEqualTo(1000);
    }

    private static @NotNull IntPredicate predicateFor(int threshold) {
        return value -> value >= threshold;
    }
}
