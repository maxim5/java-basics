package io.spbx.util.time;

import io.spbx.util.time.BasicTime.AmbiguousChoice;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BasicTimeTest {
    @Test
    public void parseHumanDuration_simple() {
        assertThat(BasicTime.parseHumanDuration("1 nano")).isEqualTo(Duration.of(1, ChronoUnit.NANOS));
        assertThat(BasicTime.parseHumanDuration("2 ns")).isEqualTo(Duration.of(2, ChronoUnit.NANOS));
        assertThat(BasicTime.parseHumanDuration("5 nanos")).isEqualTo(Duration.of(5, ChronoUnit.NANOS));

        assertThat(BasicTime.parseHumanDuration("1 micro")).isEqualTo(Duration.of(1, ChronoUnit.MICROS));
        assertThat(BasicTime.parseHumanDuration("9 micros")).isEqualTo(Duration.of(9, ChronoUnit.MICROS));

        assertThat(BasicTime.parseHumanDuration("1 milli")).isEqualTo(Duration.of(1, ChronoUnit.MILLIS));
        assertThat(BasicTime.parseHumanDuration("5 ms")).isEqualTo(Duration.of(5, ChronoUnit.MILLIS));
        assertThat(BasicTime.parseHumanDuration("6 millis")).isEqualTo(Duration.of(6, ChronoUnit.MILLIS));

        assertThat(BasicTime.parseHumanDuration("1 second")).isEqualTo(Duration.of(1, ChronoUnit.SECONDS));
        assertThat(BasicTime.parseHumanDuration("2 sec")).isEqualTo(Duration.of(2, ChronoUnit.SECONDS));
        assertThat(BasicTime.parseHumanDuration("3 s")).isEqualTo(Duration.of(3, ChronoUnit.SECONDS));
        assertThat(BasicTime.parseHumanDuration("4 seconds")).isEqualTo(Duration.of(4, ChronoUnit.SECONDS));

        assertThat(BasicTime.parseHumanDuration("1 minute")).isEqualTo(Duration.of(1, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("2 min")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("3 minutes")).isEqualTo(Duration.of(3, ChronoUnit.MINUTES));

        assertThat(BasicTime.parseHumanDuration("1 hour")).isEqualTo(Duration.of(1, ChronoUnit.HOURS));
        assertThat(BasicTime.parseHumanDuration("2 hours")).isEqualTo(Duration.of(2, ChronoUnit.HOURS));
        assertThat(BasicTime.parseHumanDuration("3 h")).isEqualTo(Duration.of(3, ChronoUnit.HOURS));

        assertThat(BasicTime.parseHumanDuration("1 day")).isEqualTo(Duration.of(1, ChronoUnit.DAYS));
        assertThat(BasicTime.parseHumanDuration("2 days")).isEqualTo(Duration.of(2, ChronoUnit.DAYS));
        assertThat(BasicTime.parseHumanDuration("3 d")).isEqualTo(Duration.of(3, ChronoUnit.DAYS));
    }

    @Test
    public void parseHumanDuration_edge_cases() {
        assertThat(BasicTime.parseHumanDuration("2min")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("2 min")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration(" 2     min    ")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("\t2    min\n\r")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));

        assertThat(BasicTime.parseHumanDuration("0min")).isEqualTo(Duration.of(0, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("+2min")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("-2min")).isEqualTo(Duration.of(-2, ChronoUnit.MINUTES));

        assertThat(BasicTime.parseHumanDuration("2 Min")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("2 MIN")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
        assertThat(BasicTime.parseHumanDuration("2 MINUTES")).isEqualTo(Duration.of(2, ChronoUnit.MINUTES));
    }

    @Test
    public void parseHumanLocalDate_simple() {
        assertThat(BasicTime.parseHumanLocalDate("10/02/09")).isEqualTo(LocalDate.of(2009, 2, 10));
        assertThat(BasicTime.parseHumanLocalDate("07/22/09")).isEqualTo(LocalDate.of(2009, 7, 22));
        assertThat(BasicTime.parseHumanLocalDate("09-08-2008")).isEqualTo(LocalDate.of(2008, 8, 9));
        assertThat(BasicTime.parseHumanLocalDate("9/9/2008")).isEqualTo(LocalDate.of(2008, 9, 9));
        assertThat(BasicTime.parseHumanLocalDate("11/4/2010")).isEqualTo(LocalDate.of(2010, 4, 11));
        assertThat(BasicTime.parseHumanLocalDate("03-07-2009")).isEqualTo(LocalDate.of(2009, 7, 3));
        assertThat(BasicTime.parseHumanLocalDate("09/01/2010")).isEqualTo(LocalDate.of(2010, 1, 9));
        assertThat(BasicTime.parseHumanLocalDate("12/11/12")).isEqualTo(LocalDate.of(2012, 11, 12));
        assertThat(BasicTime.parseHumanLocalDate("13/11/12")).isEqualTo(LocalDate.of(2012, 11, 13));

        AmbiguousChoice monthFirst = AmbiguousChoice.MONTH_FIRST;
        assertThat(BasicTime.parseHumanLocalDate("10/02/09", monthFirst)).isEqualTo(LocalDate.of(2009, 10, 2));
        assertThat(BasicTime.parseHumanLocalDate("07/22/09", monthFirst)).isEqualTo(LocalDate.of(2009, 7, 22));
        assertThat(BasicTime.parseHumanLocalDate("09-08-2008", monthFirst)).isEqualTo(LocalDate.of(2008, 9, 8));
        assertThat(BasicTime.parseHumanLocalDate("9/9/2008", monthFirst)).isEqualTo(LocalDate.of(2008, 9, 9));
        assertThat(BasicTime.parseHumanLocalDate("11/4/2010", monthFirst)).isEqualTo(LocalDate.of(2010, 11, 4));
        assertThat(BasicTime.parseHumanLocalDate("03-07-2009", monthFirst)).isEqualTo(LocalDate.of(2009, 3, 7));
        assertThat(BasicTime.parseHumanLocalDate("09/01/2010", monthFirst)).isEqualTo(LocalDate.of(2010, 9, 1));
        assertThat(BasicTime.parseHumanLocalDate("12/11/12", monthFirst)).isEqualTo(LocalDate.of(2012, 12, 11));
        assertThat(BasicTime.parseHumanLocalDate("13/11/12", monthFirst)).isEqualTo(LocalDate.of(2012, 11, 13));

        AmbiguousChoice Null = AmbiguousChoice.NULL;
        assertThat(BasicTime.parseHumanLocalDate("10/02/09", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("07/22/09", Null)).isEqualTo(LocalDate.of(2009, 7, 22));
        assertThat(BasicTime.parseHumanLocalDate("09-08-2008", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("9/9/2008", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("11/4/2010", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("03-07-2009", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("09/01/2010", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("12/11/12", Null)).isNull();
        assertThat(BasicTime.parseHumanLocalDate("13/11/12", Null)).isEqualTo(LocalDate.of(2012, 11, 13));
    }

    @Test
    public void toHMS_simple() {
        assertThat(BasicTime.toHMS(0)).isEqualTo("00:00:00");
        assertThat(BasicTime.toHMS(1)).isEqualTo("00:00:01");
        assertThat(BasicTime.toHMS(23)).isEqualTo("00:00:23");
        assertThat(BasicTime.toHMS(59)).isEqualTo("00:00:59");
        assertThat(BasicTime.toHMS(60)).isEqualTo("00:01:00");
        assertThat(BasicTime.toHMS(90)).isEqualTo("00:01:30");
        assertThat(BasicTime.toHMS(3599)).isEqualTo("00:59:59");
        assertThat(BasicTime.toHMS(3600)).isEqualTo("01:00:00");
        assertThat(BasicTime.toHMS(7200)).isEqualTo("02:00:00");
        assertThat(BasicTime.toHMS(86399)).isEqualTo("23:59:59");
    }

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
