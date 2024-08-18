package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BasicParsingTest {
    @Test
    public void parseIntSafe_simple() {
        assertThat(BasicParsing.parseIntSafe("0", -1)).isEqualTo(0);
        assertThat(BasicParsing.parseIntSafe("123", -1)).isEqualTo(123);
        assertThat(BasicParsing.parseIntSafe("-123", -1)).isEqualTo(-123);

        assertThat(BasicParsing.parseIntSafe("2147483647", -1)).isEqualTo(Integer.MAX_VALUE);
        assertThat(BasicParsing.parseIntSafe("-2147483648", -1)).isEqualTo(Integer.MIN_VALUE);
        assertThat(BasicParsing.parseIntSafe("2147483648", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntSafe("-2147483649", -1)).isEqualTo(-1);

        assertThat(BasicParsing.parseIntSafe("", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntSafe("0.", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntSafe("0.0", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntSafe("1e123", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntSafe("foo", -1)).isEqualTo(-1);
    }

    @Test
    public void parseLongSafe_simple() {
        assertThat(BasicParsing.parseLongSafe("0", -1)).isEqualTo(0);
        assertThat(BasicParsing.parseLongSafe("123", -1)).isEqualTo(123);
        assertThat(BasicParsing.parseLongSafe("-123", -1)).isEqualTo(-123);

        assertThat(BasicParsing.parseLongSafe("2147483647", -1)).isEqualTo(Integer.MAX_VALUE);
        assertThat(BasicParsing.parseLongSafe("-2147483648", -1)).isEqualTo(Integer.MIN_VALUE);
        assertThat(BasicParsing.parseLongSafe("2147483648", -1)).isEqualTo(2147483648L);
        assertThat(BasicParsing.parseLongSafe("-2147483649", -1)).isEqualTo(-2147483649L);
        assertThat(BasicParsing.parseLongSafe("9223372036854775807", -1)).isEqualTo(Long.MAX_VALUE);
        assertThat(BasicParsing.parseLongSafe("9223372036854775807L", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("9223372036854775808", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("-9223372036854775808", -1)).isEqualTo(Long.MIN_VALUE);
        assertThat(BasicParsing.parseLongSafe("-9223372036854775808L", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("-9223372036854775809", -1)).isEqualTo(-1);

        assertThat(BasicParsing.parseLongSafe("", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("0.", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("0.0", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("1e123", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongSafe("foo", -1)).isEqualTo(-1);
    }

    @Test
    public void parseByteSafe_simple() {
        byte def = -1;

        assertThat(BasicParsing.parseByteSafe("0", def)).isEqualTo(0);
        assertThat(BasicParsing.parseByteSafe("123", def)).isEqualTo(123);
        assertThat(BasicParsing.parseByteSafe("-123", def)).isEqualTo(-123);

        assertThat(BasicParsing.parseByteSafe("127", def)).isEqualTo(Byte.MAX_VALUE);
        assertThat(BasicParsing.parseByteSafe("-128", def)).isEqualTo(Byte.MIN_VALUE);
        assertThat(BasicParsing.parseByteSafe("128", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("-129", def)).isEqualTo(def);

        assertThat(BasicParsing.parseByteSafe("", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("0.", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("0.0", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("255", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("1e123", def)).isEqualTo(def);
        assertThat(BasicParsing.parseByteSafe("foo", def)).isEqualTo(def);
    }

    @Test
    public void parseBoolSafe_simple() {
        assertThat(BasicParsing.parseBoolSafe("true", false)).isTrue();
        assertThat(BasicParsing.parseBoolSafe("True", false)).isTrue();
        assertThat(BasicParsing.parseBoolSafe("TRUE", false)).isTrue();

        assertThat(BasicParsing.parseBoolSafe("false", true)).isFalse();
        assertThat(BasicParsing.parseBoolSafe("False", true)).isFalse();
        assertThat(BasicParsing.parseBoolSafe("FALSE", true)).isFalse();

        assertThat(BasicParsing.parseBoolSafe("0")).isFalse();
        assertThat(BasicParsing.parseBoolSafe("1")).isFalse();
        assertThat(BasicParsing.parseBoolSafe("no")).isFalse();
        assertThat(BasicParsing.parseBoolSafe("yes")).isFalse();

        assertThat(BasicParsing.parseBoolSafe("0", true)).isTrue();
        assertThat(BasicParsing.parseBoolSafe("1", false)).isFalse();
        assertThat(BasicParsing.parseBoolSafe("no", true)).isTrue();
        assertThat(BasicParsing.parseBoolSafe("yes", false)).isFalse();
    }

    @Test
    public void parseDoubleSafe_simple() {
        assertThat(BasicParsing.parseDoubleSafe("0", -1)).isEqualTo(0.0);
        assertThat(BasicParsing.parseDoubleSafe("0.", -1)).isEqualTo(0.0);
        assertThat(BasicParsing.parseDoubleSafe("0.0", -1)).isEqualTo(0.0);
        assertThat(BasicParsing.parseDoubleSafe("123", -1)).isEqualTo(123);
        assertThat(BasicParsing.parseDoubleSafe("-123", -1)).isEqualTo(-123);
        assertThat(BasicParsing.parseDoubleSafe("1e123", -1)).isEqualTo(1e123);

        assertThat(BasicParsing.parseDoubleSafe("2147483647", -1)).isEqualTo(2147483647.0);
        assertThat(BasicParsing.parseDoubleSafe("-2147483648", -1)).isEqualTo(-2147483648.0);
        assertThat(BasicParsing.parseDoubleSafe("2147483648", -1)).isEqualTo(2147483648.0);
        assertThat(BasicParsing.parseDoubleSafe("-2147483649", -1)).isEqualTo(-2147483649.0);

        assertThat(BasicParsing.parseDoubleSafe("", -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseDoubleSafe("foo", -1)).isEqualTo(-1);
    }

    @Test
    public void parseFloatSafe_simple() {
        assertThat(BasicParsing.parseFloatSafe("0", -1)).isEqualTo(0.0f);
        assertThat(BasicParsing.parseFloatSafe("0.", -1)).isEqualTo(0.0f);
        assertThat(BasicParsing.parseFloatSafe("0.0", -1)).isEqualTo(0.0f);
        assertThat(BasicParsing.parseFloatSafe("123", -1)).isEqualTo(123f);
        assertThat(BasicParsing.parseFloatSafe("-123", -1)).isEqualTo(-123f);
        assertThat(BasicParsing.parseFloatSafe("1e123", -1)).isPositiveInfinity();

        assertThat(BasicParsing.parseFloatSafe("2147483647", -1)).isEqualTo(2147483647.0f);
        assertThat(BasicParsing.parseFloatSafe("-2147483648", -1)).isEqualTo(-2147483648.0f);
        assertThat(BasicParsing.parseFloatSafe("2147483648", -1)).isEqualTo(2147483648.0f);
        assertThat(BasicParsing.parseFloatSafe("-2147483649", -1)).isEqualTo(-2147483649.0f);

        assertThat(BasicParsing.parseFloatSafe("", -1)).isEqualTo(-1f);
        assertThat(BasicParsing.parseFloatSafe("foo", -1)).isEqualTo(-1f);
    }
}
