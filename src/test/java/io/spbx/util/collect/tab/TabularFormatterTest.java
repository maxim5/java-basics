package io.spbx.util.collect.tab;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.tab.TabularFormatter.ASCII_FORMATTER;
import static io.spbx.util.testing.TestingBasics.arrayOf;

@Tag("fast")
public class TabularFormatterTest {
    @Test
    public void formatIntoTableString_0x0() {
        Tabular<String> tab = new ArrayTabular<>(new String[0][0]);
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("<empty>");
    }

    @Test
    public void formatIntoTableString_0x1() {
        Tabular<String> tab = new ArrayTabular<>(new String[0][1]);
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("<empty>");
    }

    @Test
    public void formatIntoTableString_1x0() {
        Tabular<String> tab = new ArrayTabular<>(new String[1][0]);
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("<empty>");
    }

    @Test
    public void formatIntoTableString_1x1() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("1")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -----
        | 1 |
        -----\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_empty() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----
        |  |
        ----\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_null() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf((String) null)
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        --------
        | null |
        --------\
        """);
    }

    @Test
    public void formatIntoTableString_2x1_short_value() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo"),
            arrayOf("1")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -------
        | foo |
        -------
        | 1   |
        -------\
        """);
    }

    @Test
    public void formatIntoTableString_2x1_long_value() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo"),
            arrayOf("123456789")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -------------
        | foo       |
        -------------
        | 123456789 |
        -------------\
        """);
    }

    @Test
    public void formatIntoTableString_2x1_one_empty_value() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo"),
            arrayOf("")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -------
        | foo |
        -------
        |     |
        -------\
        """);
    }

    @Test
    public void formatIntoTableString_2x1_all_empty_values() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf(""),
            arrayOf("")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----
        |  |
        ----
        |  |
        ----\
        """);
    }

    @Test
    public void formatIntoTableString_2x2() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo", "bar"),
            arrayOf("1", "123456")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----------------
        | foo | bar    |
        ----------------
        | 1   | 123456 |
        ----------------\
        """);
    }

    @Test
    public void formatIntoTableString_2x2_null_and_empty() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("", ""),
            arrayOf(null, null)
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ---------------
        |      |      |
        ---------------
        | null | null |
        ---------------\
        """);
    }

    @Test
    public void formatIntoTableString_3x1() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo"),
            arrayOf("12"),
            arrayOf("1234")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        --------
        | foo  |
        --------
        | 12   |
        --------
        | 1234 |
        --------\
        """);
    }

    @Test
    public void formatIntoTableString_3x3() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo", "bar", "baz"),
            arrayOf("12", "12345", ""),
            arrayOf("1234", "123", "12")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----------------------
        | foo  | bar   | baz |
        ----------------------
        | 12   | 12345 |     |
        ----------------------
        | 1234 | 123   | 12  |
        ----------------------\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_multiline_small() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("1\n2")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -----
        | 1 |
        | 2 |
        -----\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_multiline_medium() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("1\n123\n12")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -------
        | 1   |
        | 123 |
        | 12  |
        -------\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_multiline_large() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("""
                  1234
                  
                  1
                  123
                  123456789""")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -------------
        | 1234      |
        |           |
        | 1         |
        | 123       |
        | 123456789 |
        -------------\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_multiline_all_empty_lines() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("\n\n")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----
        |  |
        |  |
        ----\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_multiline_some_empty_lines() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("\nx\n\n")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        -----
        |   |
        | x |
        |   |
        -----\
        """);
    }

    @Test
    public void formatIntoTableString_2x2_multiline_empty_column() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("foo\nbar", ""),
            arrayOf("baz", "\n\n\n")
        );
        String table = ASCII_FORMATTER.formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ----------
        | foo |  |
        | bar |  |
        ----------
        | baz |  |
        |     |  |
        |     |  |
        ----------\
        """);
    }

    @Test
    public void formatIntoTableString_1x1_custom_padding() {
        Tabular<String> tab = ArrayTabular.of(
            arrayOf("123")
        );
        String table = TabularFormatter.of('|', '-', 2).formatIntoTableString(tab);
        assertThat(table).isEqualTo("""
        ---------
        |  123  |
        ---------\
        """);
    }
}
