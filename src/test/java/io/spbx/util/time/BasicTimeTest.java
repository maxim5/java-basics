package io.spbx.util.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BasicTimeTest {
    @Test
    public void toUnit_duration_to_time_unit() {
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.DAYS)).isEqualTo(0);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.HOURS)).isEqualTo(1);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.MINUTES)).isEqualTo(60);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.SECONDS)).isEqualTo(60 * 60);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.MILLISECONDS)).isEqualTo(60 * 60 * 1000);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.MICROSECONDS)).isEqualTo(60 * 60 * 1000_000L);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), TimeUnit.NANOSECONDS)).isEqualTo(60 * 60 * 1000_000_000L);
    }

    @Test
    public void toUnit_time_unit_to_time_unit() {
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.DAYS)).isEqualTo(0);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.HOURS)).isEqualTo(1);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.MINUTES)).isEqualTo(60);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.SECONDS)).isEqualTo(60 * 60);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.MILLISECONDS)).isEqualTo(60 * 60 * 1000);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.MICROSECONDS)).isEqualTo(60 * 60 * 1000_000L);
        assertThat(BasicTime.toUnit(TimeUnit.HOURS, 1, TimeUnit.NANOSECONDS)).isEqualTo(60 * 60 * 1000_000_000L);
    }

    @Test
    public void toUnit_duration_to_chrono_unit() {
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.DAYS)).isEqualTo(0);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.HOURS)).isEqualTo(1);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.MINUTES)).isEqualTo(60);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.SECONDS)).isEqualTo(60 * 60);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.MILLIS)).isEqualTo(60 * 60 * 1000);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.MICROS)).isEqualTo(60 * 60 * 1000_000L);
        assertThat(BasicTime.toUnit(Duration.ofHours(1), ChronoUnit.NANOS)).isEqualTo(60 * 60 * 1000_000_000L);
    }

    @Test
    public void toUnit_chrono_unit_to_chrono_unit() {
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.DAYS)).isEqualTo(0);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.HOURS)).isEqualTo(1);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.MINUTES)).isEqualTo(60);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.SECONDS)).isEqualTo(60 * 60);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.MILLIS)).isEqualTo(60 * 60 * 1000);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.MICROS)).isEqualTo(60 * 60 * 1000_000L);
        assertThat(BasicTime.toUnit(ChronoUnit.HOURS, 1, ChronoUnit.NANOS)).isEqualTo(60 * 60 * 1000_000_000L);
    }
}
