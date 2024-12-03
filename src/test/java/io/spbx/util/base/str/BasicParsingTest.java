package io.spbx.util.base.str;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.str.BasicParsing.*;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBytes.asBytes;

@Tag("fast")
public class BasicParsingTest {
    @Test
    public void parseInt_simple() {
        assertThat(BasicParsing.parseInt("0")).isEqualTo(0);
        assertThat(BasicParsing.parseInt("123")).isEqualTo(123);
        assertThat(BasicParsing.parseInt("-123")).isEqualTo(-123);
        assertFailure(() -> BasicParsing.parseInt("")).throwsNumberFormatException();

        assertThat(BasicParsing.parseInt("0", BINARY)).isEqualTo(0);
        assertThat(BasicParsing.parseInt("111", BINARY)).isEqualTo(7);
        assertThat(BasicParsing.parseInt("-111", BINARY)).isEqualTo(-7);
        assertFailure(() -> BasicParsing.parseInt("", BINARY)).throwsNumberFormatException();

        assertThat(BasicParsing.parseInt(asBytes("0"))).isEqualTo(0);
        assertThat(BasicParsing.parseInt(asBytes("123"))).isEqualTo(123);
        assertThat(BasicParsing.parseInt(asBytes("-123"))).isEqualTo(-123);
        assertFailure(() -> BasicParsing.parseInt(asBytes(""))).throwsNumberFormatException();

        assertThat(BasicParsing.parseInt(asBytes("0"), BINARY)).isEqualTo(0);
        assertThat(BasicParsing.parseInt(asBytes("111"), BINARY)).isEqualTo(7);
        assertThat(BasicParsing.parseInt(asBytes("-111"), BINARY)).isEqualTo(-7);
        assertFailure(() -> BasicParsing.parseInt(asBytes(""), BINARY)).throwsNumberFormatException();

        assertThat(BasicParsing.parseInt(asBytes("111"), 0, 2, BINARY)).isEqualTo(3);
        assertThat(BasicParsing.parseInt(asBytes("-111"), 0, 3, BINARY)).isEqualTo(-3);
        assertFailure(() -> BasicParsing.parseInt(asBytes("foo"), 0, 2, BINARY)).throwsNumberFormatException();
    }

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
    public void parseIntegerOrNull_simple() {
        assertThat(BasicParsing.parseIntegerOrNull("0")).isEqualTo(0);
        assertThat(BasicParsing.parseIntegerOrNull("123")).isEqualTo(123);
        assertThat(BasicParsing.parseIntegerOrNull("-123")).isEqualTo(-123);
        assertThat(BasicParsing.parseIntegerOrNull("foo")).isEqualTo(null);

        assertThat(BasicParsing.parseIntegerOrNull("0", -1)).isEqualTo(0);
        assertThat(BasicParsing.parseIntegerOrNull("123", -1)).isEqualTo(123);
        assertThat(BasicParsing.parseIntegerOrNull("-123", -1)).isEqualTo(-123);

        assertThat(BasicParsing.parseIntegerOrNull("0", BINARY, -1)).isEqualTo(0);
        assertThat(BasicParsing.parseIntegerOrNull("111", BINARY, -1)).isEqualTo(7);
        assertThat(BasicParsing.parseIntegerOrNull("-111", BINARY, -1)).isEqualTo(-7);

        assertThat(BasicParsing.parseIntegerOrNull("", null)).isEqualTo(null);
        assertThat(BasicParsing.parseIntegerOrNull("foo", null)).isEqualTo(null);

        assertThat(BasicParsing.parseIntegerOrNull("", BINARY, -1)).isEqualTo(-1);
        assertThat(BasicParsing.parseIntegerOrNull("foo", BINARY, -1)).isEqualTo(-1);
    }

    @Test
    public void isValidInteger_simple() {
        assertThat(BasicParsing.isValidInteger("0")).isTrue();
        assertThat(BasicParsing.isValidInteger("123456789")).isTrue();
        assertThat(BasicParsing.isValidInteger("+123456789")).isTrue();
        assertThat(BasicParsing.isValidInteger("-123456789")).isTrue();
        assertThat(BasicParsing.isValidInteger("")).isFalse();
        assertThat(BasicParsing.isValidInteger("foo")).isFalse();

        assertThat(BasicParsing.isValidInteger("0", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("+123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("-123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("", DECIMAL)).isFalse();
        assertThat(BasicParsing.isValidInteger("foo", DECIMAL)).isFalse();

        assertThat(BasicParsing.isValidInteger("0", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("+123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("-123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("", HEXADECIMAL)).isFalse();
        assertThat(BasicParsing.isValidInteger("foo", HEXADECIMAL)).isFalse();

        assertThat(BasicParsing.isValidInteger("0", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("+123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("-123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidInteger("", OCTAL)).isFalse();
        assertThat(BasicParsing.isValidInteger("foo", OCTAL)).isFalse();

        assertThat(BasicParsing.isValidInteger("0", BINARY)).isTrue();
        assertThat(BasicParsing.isValidInteger("10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidInteger("+10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidInteger("-10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidInteger("", BINARY)).isFalse();
        assertThat(BasicParsing.isValidInteger("foo", BINARY)).isFalse();
    }

    @Test
    public void parseLong_simple() {
        assertThat(BasicParsing.parseLong("0")).isEqualTo(0);
        assertThat(BasicParsing.parseLong("123")).isEqualTo(123);
        assertThat(BasicParsing.parseLong("-123")).isEqualTo(-123);
        assertFailure(() -> BasicParsing.parseLong("")).throwsNumberFormatException();

        assertThat(BasicParsing.parseLong("0", BINARY)).isEqualTo(0);
        assertThat(BasicParsing.parseLong("111", BINARY)).isEqualTo(7);
        assertThat(BasicParsing.parseLong("-111", BINARY)).isEqualTo(-7);
        assertFailure(() -> BasicParsing.parseLong("", BINARY)).throwsNumberFormatException();

        assertThat(BasicParsing.parseLong(asBytes("0"))).isEqualTo(0);
        assertThat(BasicParsing.parseLong(asBytes("123"))).isEqualTo(123);
        assertThat(BasicParsing.parseLong(asBytes("-123"))).isEqualTo(-123);
        assertFailure(() -> BasicParsing.parseLong(asBytes(""))).throwsNumberFormatException();

        assertThat(BasicParsing.parseLong(asBytes("0"), BINARY)).isEqualTo(0);
        assertThat(BasicParsing.parseLong(asBytes("111"), BINARY)).isEqualTo(7);
        assertThat(BasicParsing.parseLong(asBytes("-111"), BINARY)).isEqualTo(-7);
        assertFailure(() -> BasicParsing.parseLong(asBytes(""), BINARY)).throwsNumberFormatException();

        assertThat(BasicParsing.parseLong(asBytes("111"), 0, 2, BINARY)).isEqualTo(3);
        assertThat(BasicParsing.parseLong(asBytes("-111"), 0, 3, BINARY)).isEqualTo(-3);
        assertFailure(() -> BasicParsing.parseLong(asBytes("foo"), 0, 2, BINARY)).throwsNumberFormatException();
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
    public void parseLongOrNull_simple() {
        assertThat(BasicParsing.parseLongOrNull("0")).isEqualTo(0);
        assertThat(BasicParsing.parseLongOrNull("123")).isEqualTo(123);
        assertThat(BasicParsing.parseLongOrNull("-123")).isEqualTo(-123);
        assertThat(BasicParsing.parseLongOrNull("foo")).isEqualTo(null);

        assertThat(BasicParsing.parseLongOrNull("0", -1L)).isEqualTo(0);
        assertThat(BasicParsing.parseLongOrNull("123", -1L)).isEqualTo(123);
        assertThat(BasicParsing.parseLongOrNull("-123", -1L)).isEqualTo(-123);

        assertThat(BasicParsing.parseLongOrNull("0", BINARY, -1L)).isEqualTo(0);
        assertThat(BasicParsing.parseLongOrNull("111", BINARY, -1L)).isEqualTo(7);
        assertThat(BasicParsing.parseLongOrNull("-111", BINARY, -1L)).isEqualTo(-7);

        assertThat(BasicParsing.parseLongOrNull("", null)).isEqualTo(null);
        assertThat(BasicParsing.parseLongOrNull("foo", null)).isEqualTo(null);

        assertThat(BasicParsing.parseLongOrNull("", BINARY, -1L)).isEqualTo(-1);
        assertThat(BasicParsing.parseLongOrNull("foo", BINARY, -1L)).isEqualTo(-1);
    }

    @Test
    public void isValidLong_simple() {
        assertThat(BasicParsing.isValidLong("0")).isTrue();
        assertThat(BasicParsing.isValidLong("123456789")).isTrue();
        assertThat(BasicParsing.isValidLong("+123456789")).isTrue();
        assertThat(BasicParsing.isValidLong("-123456789")).isTrue();
        assertThat(BasicParsing.isValidLong("")).isFalse();
        assertThat(BasicParsing.isValidLong("foo")).isFalse();

        assertThat(BasicParsing.isValidLong("0", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("+123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("-123456789", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("", DECIMAL)).isFalse();
        assertThat(BasicParsing.isValidLong("foo", DECIMAL)).isFalse();

        assertThat(BasicParsing.isValidLong("0", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("+123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("-123456FF", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidLong("", HEXADECIMAL)).isFalse();
        assertThat(BasicParsing.isValidLong("foo", HEXADECIMAL)).isFalse();

        assertThat(BasicParsing.isValidLong("0", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidLong("123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidLong("+123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidLong("-123455670", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidLong("", OCTAL)).isFalse();
        assertThat(BasicParsing.isValidLong("foo", OCTAL)).isFalse();

        assertThat(BasicParsing.isValidLong("0", BINARY)).isTrue();
        assertThat(BasicParsing.isValidLong("10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidLong("+10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidLong("-10110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidLong("", BINARY)).isFalse();
        assertThat(BasicParsing.isValidLong("foo", BINARY)).isFalse();
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
    public void isValidByte_simple() {
        assertThat(BasicParsing.isValidByte("0")).isTrue();
        assertThat(BasicParsing.isValidByte("123")).isTrue();
        assertThat(BasicParsing.isValidByte("+123")).isTrue();
        assertThat(BasicParsing.isValidByte("-123")).isTrue();
        assertThat(BasicParsing.isValidByte("")).isFalse();
        assertThat(BasicParsing.isValidByte("foo")).isFalse();

        assertThat(BasicParsing.isValidByte("0", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("123", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("+123", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("-123", DECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("", DECIMAL)).isFalse();
        assertThat(BasicParsing.isValidByte("foo", DECIMAL)).isFalse();

        assertThat(BasicParsing.isValidByte("0", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("0f", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("+0f", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("-0f", HEXADECIMAL)).isTrue();
        assertThat(BasicParsing.isValidByte("", HEXADECIMAL)).isFalse();
        assertThat(BasicParsing.isValidByte("foo", HEXADECIMAL)).isFalse();

        assertThat(BasicParsing.isValidByte("0", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidByte("12", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidByte("+12", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidByte("-12", OCTAL)).isTrue();
        assertThat(BasicParsing.isValidByte("", OCTAL)).isFalse();
        assertThat(BasicParsing.isValidByte("foo", OCTAL)).isFalse();

        assertThat(BasicParsing.isValidByte("0", BINARY)).isTrue();
        assertThat(BasicParsing.isValidByte("110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidByte("+110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidByte("-110111", BINARY)).isTrue();
        assertThat(BasicParsing.isValidByte("", BINARY)).isFalse();
        assertThat(BasicParsing.isValidByte("foo", BINARY)).isFalse();
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
    public void isValidBoolean_simple() {
        assertThat(BasicParsing.isValidBoolean("true")).isTrue();
        assertThat(BasicParsing.isValidBoolean("True")).isTrue();
        assertThat(BasicParsing.isValidBoolean("TRUE")).isTrue();

        assertThat(BasicParsing.isValidBoolean("false")).isTrue();
        assertThat(BasicParsing.isValidBoolean("False")).isTrue();
        assertThat(BasicParsing.isValidBoolean("FALSE")).isTrue();

        assertThat(BasicParsing.isValidBoolean("")).isFalse();
        assertThat(BasicParsing.isValidBoolean("foo")).isFalse();
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
    public void isValidDouble_simple() {
        assertThat(BasicParsing.isValidDouble("0")).isTrue();
        assertThat(BasicParsing.isValidDouble("0.")).isTrue();
        assertThat(BasicParsing.isValidDouble("0.0")).isTrue();
        assertThat(BasicParsing.isValidDouble("+123")).isTrue();
        assertThat(BasicParsing.isValidDouble("-123")).isTrue();
        assertThat(BasicParsing.isValidDouble("1e123")).isTrue();

        assertThat(BasicParsing.isValidDouble("")).isFalse();
        assertThat(BasicParsing.isValidDouble("foo")).isFalse();
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

    @Test
    public void isValidFloat_simple() {
        assertThat(BasicParsing.isValidFloat("0")).isTrue();
        assertThat(BasicParsing.isValidFloat("0.")).isTrue();
        assertThat(BasicParsing.isValidFloat("0.0")).isTrue();
        assertThat(BasicParsing.isValidFloat("+123")).isTrue();
        assertThat(BasicParsing.isValidFloat("-123")).isTrue();
        assertThat(BasicParsing.isValidFloat("1e123")).isTrue();

        assertThat(BasicParsing.isValidFloat("")).isFalse();
        assertThat(BasicParsing.isValidFloat("foo")).isFalse();
    }
}
