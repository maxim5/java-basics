package io.spbx.util.base.str;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.spbx.util.testing.MoreTruth.assertThat;

@Tag("fast")
public class FormatsTest {
    @Test
    public void toHumanReadable_int() {
        assertThat(Formats.toHumanReadable(0)).isEqualTo("0");
        assertThat(Formats.toHumanReadable(1)).isEqualTo("1");
        assertThat(Formats.toHumanReadable(12)).isEqualTo("12");
        assertThat(Formats.toHumanReadable(123)).isEqualTo("123");

        assertThat(Formats.toHumanReadable(1234)).isEqualTo("1,234");
        assertThat(Formats.toHumanReadable(12345)).isEqualTo("12,345");
        assertThat(Formats.toHumanReadable(123456)).isEqualTo("123,456");

        assertThat(Formats.toHumanReadable(1234567)).isEqualTo("1,234,567");
        assertThat(Formats.toHumanReadable(12345678)).isEqualTo("12,345,678");
        assertThat(Formats.toHumanReadable(123456789)).isEqualTo("123,456,789");

        assertThat(Formats.toHumanReadable(-1)).isEqualTo("-1");
        assertThat(Formats.toHumanReadable(-12)).isEqualTo("-12");
        assertThat(Formats.toHumanReadable(-123)).isEqualTo("-123");

        assertThat(Formats.toHumanReadable(-1234)).isEqualTo("-1,234");
        assertThat(Formats.toHumanReadable(-12345)).isEqualTo("-12,345");
        assertThat(Formats.toHumanReadable(-123456)).isEqualTo("-123,456");

        assertThat(Formats.toHumanReadable(-1234567)).isEqualTo("-1,234,567");
        assertThat(Formats.toHumanReadable(-12345678)).isEqualTo("-12,345,678");
        assertThat(Formats.toHumanReadable(-123456789)).isEqualTo("-123,456,789");
    }

    @Test
    public void toHumanReadable_long() {
        assertThat(Formats.toHumanReadable(0L)).isEqualTo("0");
        assertThat(Formats.toHumanReadable(1L)).isEqualTo("1");
        assertThat(Formats.toHumanReadable(12L)).isEqualTo("12");
        assertThat(Formats.toHumanReadable(123L)).isEqualTo("123");

        assertThat(Formats.toHumanReadable(1234L)).isEqualTo("1,234");
        assertThat(Formats.toHumanReadable(12345L)).isEqualTo("12,345");
        assertThat(Formats.toHumanReadable(123456L)).isEqualTo("123,456");

        assertThat(Formats.toHumanReadable(1234567L)).isEqualTo("1,234,567");
        assertThat(Formats.toHumanReadable(12345678L)).isEqualTo("12,345,678");
        assertThat(Formats.toHumanReadable(123456789L)).isEqualTo("123,456,789");

        assertThat(Formats.toHumanReadable(-1L)).isEqualTo("-1");
        assertThat(Formats.toHumanReadable(-12L)).isEqualTo("-12");
        assertThat(Formats.toHumanReadable(-123L)).isEqualTo("-123");

        assertThat(Formats.toHumanReadable(-1234L)).isEqualTo("-1,234");
        assertThat(Formats.toHumanReadable(-12345L)).isEqualTo("-12,345");
        assertThat(Formats.toHumanReadable(-123456L)).isEqualTo("-123,456");

        assertThat(Formats.toHumanReadable(-1234567L)).isEqualTo("-1,234,567");
        assertThat(Formats.toHumanReadable(-12345678L)).isEqualTo("-12,345,678");
        assertThat(Formats.toHumanReadable(-123456789L)).isEqualTo("-123,456,789");
    }

    @Test
    public void groupFromLeft_of_one() {
        assertThat(Formats.groupFromLeft("", 1, '_')).isEqualTo("");
        assertThat(Formats.groupFromLeft("1", 1, '_')).isEqualTo("1");
        assertThat(Formats.groupFromLeft("12", 1, '_')).isEqualTo("1_2");
        assertThat(Formats.groupFromLeft("123", 1, '_')).isEqualTo("1_2_3");
        assertThat(Formats.groupFromLeft("1234", 1, '_')).isEqualTo("1_2_3_4");
    }

    @Test
    public void groupFromLeft_of_two() {
        assertThat(Formats.groupFromLeft("", 2, '_')).isEqualTo("");
        assertThat(Formats.groupFromLeft("1", 2, '_')).isEqualTo("1");
        assertThat(Formats.groupFromLeft("12", 2, '_')).isEqualTo("12");
        assertThat(Formats.groupFromLeft("123", 2, '_')).isEqualTo("12_3");
        assertThat(Formats.groupFromLeft("1234", 2, '_')).isEqualTo("12_34");
        assertThat(Formats.groupFromLeft("12345", 2, '_')).isEqualTo("12_34_5");
        assertThat(Formats.groupFromLeft("123456", 2, '_')).isEqualTo("12_34_56");
    }

    @Test
    public void groupFromLeft_of_three() {
        assertThat(Formats.groupFromLeft("", 3, '_')).isEqualTo("");
        assertThat(Formats.groupFromLeft("1", 3, '_')).isEqualTo("1");
        assertThat(Formats.groupFromLeft("12", 3, '_')).isEqualTo("12");
        assertThat(Formats.groupFromLeft("123", 3, '_')).isEqualTo("123");

        assertThat(Formats.groupFromLeft("1234", 3, '_')).isEqualTo("123_4");
        assertThat(Formats.groupFromLeft("12345", 3, '_')).isEqualTo("123_45");
        assertThat(Formats.groupFromLeft("123456", 3, '_')).isEqualTo("123_456");

        assertThat(Formats.groupFromLeft("1234567", 3, '_')).isEqualTo("123_456_7");
        assertThat(Formats.groupFromLeft("12345678", 3, '_')).isEqualTo("123_456_78");
        assertThat(Formats.groupFromLeft("123456789", 3, '_')).isEqualTo("123_456_789");
    }

    @Test
    public void groupFromRight_of_one() {
        assertThat(Formats.groupFromRight("", 1, '_')).isEqualTo("");
        assertThat(Formats.groupFromRight("1", 1, '_')).isEqualTo("1");
        assertThat(Formats.groupFromRight("12", 1, '_')).isEqualTo("1_2");
        assertThat(Formats.groupFromRight("123", 1, '_')).isEqualTo("1_2_3");
        assertThat(Formats.groupFromRight("1234", 1, '_')).isEqualTo("1_2_3_4");
    }

    @Test
    public void groupFromRight_of_two() {
        assertThat(Formats.groupFromRight("", 2, '_')).isEqualTo("");
        assertThat(Formats.groupFromRight("1", 2, '_')).isEqualTo("1");
        assertThat(Formats.groupFromRight("12", 2, '_')).isEqualTo("12");
        assertThat(Formats.groupFromRight("123", 2, '_')).isEqualTo("1_23");
        assertThat(Formats.groupFromRight("1234", 2, '_')).isEqualTo("12_34");
        assertThat(Formats.groupFromRight("12345", 2, '_')).isEqualTo("1_23_45");
        assertThat(Formats.groupFromRight("123456", 2, '_')).isEqualTo("12_34_56");
    }

    @Test
    public void groupFromRight_of_three() {
        assertThat(Formats.groupFromRight("", 3, '_')).isEqualTo("");
        assertThat(Formats.groupFromRight("1", 3, '_')).isEqualTo("1");
        assertThat(Formats.groupFromRight("12", 3, '_')).isEqualTo("12");
        assertThat(Formats.groupFromRight("123", 3, '_')).isEqualTo("123");

        assertThat(Formats.groupFromRight("1234", 3, '_')).isEqualTo("1_234");
        assertThat(Formats.groupFromRight("12345", 3, '_')).isEqualTo("12_345");
        assertThat(Formats.groupFromRight("123456", 3, '_')).isEqualTo("123_456");

        assertThat(Formats.groupFromRight("1234567", 3, '_')).isEqualTo("1_234_567");
        assertThat(Formats.groupFromRight("12345678", 3, '_')).isEqualTo("12_345_678");
        assertThat(Formats.groupFromRight("123456789", 3, '_')).isEqualTo("123_456_789");
    }
}
