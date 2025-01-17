package io.spbx.util.base.math;

import com.google.common.base.Strings;
import com.google.common.primitives.UnsignedLong;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.str.BasicParsing;
import io.spbx.util.testing.TestingBasics;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.error.BasicExceptions.newInternalError;
import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.base.lang.EasyCast.castToInt;
import static io.spbx.util.collect.iter.BasicIterables.concatToList;
import static io.spbx.util.testing.TestingBasics.listOf;
import static io.spbx.util.testing.TestingBasics.streamOf;
import static io.spbx.util.testing.TestingBigIntegers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("slow")
@SuppressWarnings("EqualsWithItself")
public class Int128Test {
    // The last one overflows, it's ok.
    private static final long[] POW2 = LongStream.range(0, 64).map(n -> 1L << n).toArray();

    private static final List<Long> SIMPLE_MIN_MAX_VALUES = streamOf(
        Byte.MAX_VALUE, Byte.MIN_VALUE,
        Short.MAX_VALUE, Short.MIN_VALUE,
        Integer.MAX_VALUE, Integer.MIN_VALUE,
        Long.MAX_VALUE, Long.MIN_VALUE
    ).map(Number::longValue).toList();

    // Test convenient comparator: first positive, then negative.
    private static final Comparator<BigInteger> CMP = (lhs, rhs) ->
        lhs.signum() >= 0 && rhs.signum() >= 0 ? lhs.compareTo(rhs) : -lhs.compareTo(rhs);

    private static final List<BigInteger> EDGE_CASE_BIG_INTEGERS = IntStream.range(0, 128)
        .mapToObj($1::shiftLeft)
        .flatMap(b -> streamOf(b, b.subtract($1), b.negate(), b.negate().add($1)))
        .map(RANGE_INT128::fitIn)
        .sorted(CMP).distinct().toList();

    private static final List<Long> EDGE_CASE_LONGS =
        EDGE_CASE_BIG_INTEGERS.stream().map(BigInteger::longValue).distinct().toList();

    // From https://bigprimes.org/
    private static final List<BigInteger> LARGE_PRIME_NUMBERS = streamOf(
        "477431785618589",
        "7734245186249221",
        "313506233314930897",
        "14262866606244585389",
        "9312117738832437088867",
        "643060765953885798980471",
        "86177578550109096835627979",
        "9789690428521166127696463099",
        "511502705152331397313213352309",
        "37010749720546680375917374542419",
        "9619122375485128076391017781203171",
        "729607297303010430697493914704054547",
        "19008080681165741144368765276016110223",
        "134729416280889832795054608867944616583"
    ).map(BigInteger::new).flatMap(b -> streamOf(b, b.negate())).sorted(CMP).toList();

    private static final List<BigInteger> BIG_INTEGERS = concatToList(EDGE_CASE_BIG_INTEGERS, LARGE_PRIME_NUMBERS);

    /** Constants **/

    @Test
    public void const_zero_trivial() {
        assertThat(Int128.ZERO.highBits()).isEqualTo(0);
        assertThat(Int128.ZERO.lowBits()).isEqualTo(0);
        assertThat(Int128.ZERO.toBigInteger()).isEqualTo($0);
        assertThat(Int128.ZERO.longValue()).isEqualTo(0);
        assertThat(Int128.ZERO.intValue()).isEqualTo(0);
        assertThat(Int128.ZERO.toString()).isEqualTo("0");
        assertThat(Int128.ZERO.toBinaryString()).isEqualTo("0".repeat(128));
        assertThat(Int128.ZERO.toHexString()).isEqualTo("0".repeat(32));
        assertThat(Int128.ZERO).isEqualTo(Int128.from(0));
        assertThat(Int128.ZERO.compareTo(Int128.ZERO)).isEqualTo(0);
        assertThat(Int128.ZERO.compareTo(Int128.ONE)).isEqualTo(-1);
        assertThat(Int128.ZERO.compareTo(Int128.MIN_VALUE)).isEqualTo(1);
        assertThat(Int128.ZERO.compareTo(Int128.MAX_VALUE)).isEqualTo(-1);
    }

    @Test
    public void const_one_trivial() {
        assertThat(Int128.ONE.highBits()).isEqualTo(0);
        assertThat(Int128.ONE.lowBits()).isEqualTo(1);
        assertThat(Int128.ONE.toBigInteger()).isEqualTo($1);
        assertThat(Int128.ONE.longValue()).isEqualTo(1);
        assertThat(Int128.ONE.intValue()).isEqualTo(1);
        assertThat(Int128.ONE.toString()).isEqualTo("1");
        assertThat(Int128.ONE.toBinaryString()).isEqualTo("0".repeat(127) + "1");
        assertThat(Int128.ONE.toHexString()).isEqualTo("0".repeat(31) + "1");
        assertThat(Int128.ONE).isEqualTo(Int128.from(1));
        assertThat(Int128.ONE.compareTo(Int128.ZERO)).isEqualTo(1);
        assertThat(Int128.ONE.compareTo(Int128.ONE)).isEqualTo(0);
        assertThat(Int128.ONE.compareTo(Int128.MIN_VALUE)).isEqualTo(1);
        assertThat(Int128.ONE.compareTo(Int128.MAX_VALUE)).isEqualTo(-1);
    }

    @Test
    public void const_max_value_trivial() {
        BigInteger expected = INT128_MAX;  // (1 << 127) - 1
        assertThat(Int128.MAX_VALUE.highBits()).isEqualTo(Long.MAX_VALUE);
        assertThat(Int128.MAX_VALUE.lowBits()).isEqualTo(-1);
        assertThat(Int128.MAX_VALUE.toBigInteger()).isEqualTo(expected);
        assertThat(Int128.MAX_VALUE.longValue()).isEqualTo(expected.longValue());  // -1
        assertThat(Int128.MAX_VALUE.intValue()).isEqualTo(expected.intValue());    // -1
        assertThat(Int128.MAX_VALUE.toString()).isEqualTo(expected.toString());
        assertThat(Int128.MAX_VALUE.toBinaryString()).isEqualTo("0" + "1".repeat(127));
        assertThat(Int128.MAX_VALUE.toHexString()).isEqualTo("7" + "f".repeat(31));
        assertThat(Int128.MAX_VALUE.compareTo(Int128.ZERO)).isEqualTo(1);
        assertThat(Int128.MAX_VALUE.compareTo(Int128.ONE)).isEqualTo(1);
        assertThat(Int128.MAX_VALUE.compareTo(Int128.MIN_VALUE)).isEqualTo(1);
        assertThat(Int128.MAX_VALUE.compareTo(Int128.MAX_VALUE)).isEqualTo(0);
    }

    @Test
    public void const_min_value_trivial() {
        BigInteger expected = INT128_MIN;  // -(1 << 127)
        assertThat(Int128.MIN_VALUE.highBits()).isEqualTo(Long.MIN_VALUE);
        assertThat(Int128.MIN_VALUE.lowBits()).isEqualTo(0);
        assertThat(Int128.MIN_VALUE.toBigInteger()).isEqualTo(expected);
        assertThat(Int128.MIN_VALUE.longValue()).isEqualTo(expected.longValue());  // 0
        assertThat(Int128.MIN_VALUE.intValue()).isEqualTo(expected.intValue());    // 0
        assertThat(Int128.MIN_VALUE.toString()).isEqualTo(expected.toString());
        assertThat(Int128.MIN_VALUE.toBinaryString()).isEqualTo("1" + "0".repeat(127));
        assertThat(Int128.MIN_VALUE.toHexString()).isEqualTo("8" + "0".repeat(31));
        assertThat(Int128.MIN_VALUE.compareTo(Int128.ZERO)).isEqualTo(-1);
        assertThat(Int128.MIN_VALUE.compareTo(Int128.ONE)).isEqualTo(-1);
        assertThat(Int128.MIN_VALUE.compareTo(Int128.MIN_VALUE)).isEqualTo(0);
        assertThat(Int128.MIN_VALUE.compareTo(Int128.MAX_VALUE)).isEqualTo(-1);
    }

    /** {@link Int128#fromBits}}, {@link Int128#from}, {@link Int128#fromHex} **/

    @Test
    public void construction_from_bits_long_trivial() {
        assertThat(Int128.fromBits(0, 0)).isEqualTo(Int128.from($0));
        assertThat(Int128.fromBits(0, 1)).isEqualTo(Int128.from($1));
        assertThat(Int128.fromBits(0, -1)).isEqualTo(Int128.from(UINT64_MAX));

        assertThat(Int128.fromBits(1, 0)).isEqualTo(Int128.from($2_64));
        assertThat(Int128.fromBits(1, 1)).isEqualTo(Int128.from($2_64.add($1)));
        assertThat(Int128.fromBits(1, -1)).isEqualTo(Int128.from($2_65.subtract($1)));

        assertThat(Int128.fromBits(-1, 0)).isEqualTo(Int128.from($2_64.negate()));
        assertThat(Int128.fromBits(-1, 1)).isEqualTo(Int128.from($2_64.negate().add($1)));
        assertThat(Int128.fromBits(-1, -1)).isEqualTo(Int128.from($1.negate()));

        assertThat(Int128.fromBits(0, Long.MAX_VALUE)).isEqualTo(Int128.from(INT64_MAX));
        assertThat(Int128.fromBits(0, Long.MIN_VALUE)).isEqualTo(Int128.from(INT64_MIN.negate()));
        assertThat(Int128.fromBits(Long.MAX_VALUE, 0)).isEqualTo(Int128.from($2_127.subtract($2_64)));
        assertThat(Int128.fromBits(Long.MIN_VALUE, 0)).isEqualTo(Int128.from(INT128_MIN));
    }

    @Test
    public void construction_from_unsigned_long_trivial() {
        assertThat(Int128.from(UnsignedLong.ZERO)).isEqualTo(Int128.ZERO);
        assertThat(Int128.from(UnsignedLong.ONE)).isEqualTo(Int128.ONE);
        assertThat(Int128.from(UnsignedLong.fromLongBits(1L << 63))).isEqualTo(Int128.from($2_63));

        assertThat(Int128.from(UnsignedLong.MAX_VALUE)).isEqualTo(Int128.from(UINT64_MAX));
        assertThat(Int128.from(UnsignedLong.fromLongBits(-2))).isEqualTo(Int128.from(UINT64_MAX).subtract(Int128.ONE));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0", "1", "2147483647", "2147483648", "9223372036854775807", "9223372036854775808",
        "-1", "-2147483647", "-2147483648", "-9223372036854775807", "-9223372036854775808",
        "99999999999999999", "999999999999999999", "9999999999999999999",
        "-99999999999999999", "-999999999999999999", "-9999999999999999999",
    })
    public void construction_from_string_simple(String num) {
        assertConstruction(num);
    }

    @Test
    public void construction_from_string_ultimate() {
        for (BigInteger num : BIG_INTEGERS) {
            assertConstruction(num.toString());
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {
        0, 1, -1, 0.5, -0.5, 2147483647.0, 2147483648.0, -2147483647.0, -2147483648.0,
        9223372036854775807.0, 9223372036854775808.0, -9223372036854775807.0, -9223372036854775808.0,
    })
    public void construction_from_double_simple(double num) {
        assertConstructionFromDouble(num);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0", "1", "10", "ff", "7f", "aaa", "abcd", "ffff_ffff", "0_0_0_0", "____0"
    })
    public void construction_from_hex_simple_long(String hex) {
        long num = Long.parseLong(hex.replaceAll("_", ""), 16);
        Int128 expected = Int128.from(num);

        assertThat(Int128.fromHex(hex)).isEqualTo(expected);
        assertThat(Int128.fromHex(hex.toUpperCase())).isEqualTo(expected);
        assertThat(Int128.fromHex("0x" + hex)).isEqualTo(expected);
        assertThat(Int128.fromHex("0x0000" + hex)).isEqualTo(expected);

        assertThat(Int128.fromHex("-" + hex)).isEqualTo(expected.negate());
        assertThat(Int128.fromHex("-" + hex.toUpperCase())).isEqualTo(expected.negate());
        assertThat(Int128.fromHex("-0x" + hex)).isEqualTo(expected.negate());
        assertThat(Int128.fromHex("-0x0000" + hex)).isEqualTo(expected.negate());
    }

    @Test
    public void construction_from_hex_ultimate() {
        for (BigInteger num : BIG_INTEGERS) {
            assertThat(Int128.fromHex(num.toString(16))).isEqualTo(Int128.from(num));
        }
    }

    /** Internal Consistency **/

    @ParameterizedTest
    @MethodSource
    public void internal_consistency_simple(Int128 val) {
        assertThatInt128(val).internalConsistency();
    }

    private static @NotNull List<Int128> internal_consistency_simple() {
        return TestingBasics.flatListOf(
            Int128.ZERO, Int128.ONE,
            Int128.MIN_VALUE, Int128.MAX_VALUE,
            SIMPLE_MIN_MAX_VALUES.stream().map(Int128::from)
        );
    }

    @Test
    public void internal_consistency_ultimate() {
        for (BigInteger num : BIG_INTEGERS) {
            assertThatInt128(Int128.from(num)).internalConsistency();
        }
        for (long num : EDGE_CASE_LONGS) {
            assertThatInt128(Int128.from(num)).internalConsistency();
        }
    }

    /** {@link Int128#compareTo} **/

    private static final BiOpTester<Integer, Integer> COMPARE_TO = test(Int128::compareTo, BigInteger::compareTo);

    @Test
    public void compare_ultimate() {
        COMPARE_TO.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#increment()} **/

    private static final UnOpTester<Int128, BigInteger> INCREMENT = test(Int128::increment, b -> b.add($1));

    @ParameterizedTest
    @ValueSource(longs = { 0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, Long.MAX_VALUE - 1 })
    public void increment_simple_long(long num) {
        assertThat(Int128.from(num).increment()).isEqualTo(Int128.from(num + 1));
    }

    @Test
    public void increment_ultimate() {
        INCREMENT.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#decrement()} **/

    private static final UnOpTester<Int128, BigInteger> DECREMENT = test(Int128::decrement, b -> b.subtract($1));

    @ParameterizedTest
    @ValueSource(longs = { 0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE + 1, Long.MAX_VALUE })
    public void decrement_simple_long(long num) {
        assertThat(Int128.from(num).decrement()).isEqualTo(Int128.from(num - 1));
    }

    @Test
    public void decrement_ultimate() {
        DECREMENT.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#add(Int128)}, {@link Int128#add(long)} **/

    private static final BiOpTester<Int128, BigInteger> ADD = test(Int128::add, BigInteger::add);
    private static final LongOpTester<Int128, BigInteger> ADD_LONG = testLong(Int128::add, (a, l) -> a.add($(l)));

    @Test
    public void add_ultimate() {
        ADD.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void add_long_ultimate() {
        ADD_LONG.assertMatchAll(BIG_INTEGERS, EDGE_CASE_LONGS);
    }

    /** {@link Int128#subtract(Int128)}, {@link Int128#subtract(long)} **/

    private static final BiOpTester<Int128, BigInteger> SUBTRACT = test(Int128::subtract, BigInteger::subtract);
    private static final LongOpTester<Int128, BigInteger> SUBTRACT_LONG = testLong(Int128::subtract, (a, l) -> a.subtract($(l)));

    @Test
    public void subtract_ultimate() {
        SUBTRACT.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void subtract_long_ultimate() {
        SUBTRACT_LONG.assertMatchAll(BIG_INTEGERS, EDGE_CASE_LONGS);
    }

    /** {@link Int128#is64Bit()}, {@link Int128#is64BitUnsigned()} **/

    private static final UnOpTester<Boolean, Boolean> IS_64_BIT = test(Int128::is64Bit, RANGE_INT64::contains);
    private static final UnOpTester<Boolean, Boolean> IS_64_BIT_UNSIGNED = test(Int128::is64BitUnsigned, RANGE_UINT64::contains);
    private static final List<Int128> FIT_INTO_LONG =
        listOf(Int128.ZERO, Int128.ONE, Int128.from(Long.MAX_VALUE), Int128.from(Long.MIN_VALUE));
    private static final List<Int128> NOT_FIT_INTO_LONG = listOf(
        Int128.MAX_VALUE, Int128.MIN_VALUE,
        Int128.from("9223372036854775808"), Int128.from("-9223372036854775809")
    );

    @Test
    public void is64Bit_simple() {
        assertThat(Int128.ZERO.is64Bit()).isTrue();
        assertThat(Int128.ONE.is64Bit()).isTrue();
        assertThat(Int128.from(Long.MAX_VALUE).is64Bit()).isTrue();
        assertThat(Int128.from(Long.MIN_VALUE).is64Bit()).isTrue();

        assertThat(Int128.MAX_VALUE.is64Bit()).isFalse();
        assertThat(Int128.MIN_VALUE.is64Bit()).isFalse();
        assertThat(Int128.from("9223372036854775808").is64Bit()).isFalse();
        assertThat(Int128.from("-9223372036854775809").is64Bit()).isFalse();
    }

    @Test
    public void is64BitUnsigned_simple() {
        assertThat(Int128.ZERO.is64BitUnsigned()).isTrue();
        assertThat(Int128.ONE.is64BitUnsigned()).isTrue();
        assertThat(Int128.from(Long.MAX_VALUE).is64BitUnsigned()).isTrue();
        assertThat(Int128.from(Long.MIN_VALUE).is64BitUnsigned()).isFalse();

        assertThat(Int128.MAX_VALUE.is64BitUnsigned()).isFalse();
        assertThat(Int128.MIN_VALUE.is64BitUnsigned()).isFalse();
        assertThat(Int128.from("9223372036854775808").is64BitUnsigned()).isTrue();
        assertThat(Int128.from("-9223372036854775809").is64BitUnsigned()).isFalse();
    }

    @Test
    public void is64Bit_ultimate() {
        IS_64_BIT.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void is64BitUnsigned_ultimate() {
        IS_64_BIT_UNSIGNED.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#multiply(Int128)} **/

    private static final BiOpTester<Int128, BigInteger> MULTIPLY = test(Int128::multiply, BigInteger::multiply);
    private static final LongOpTester<Int128, BigInteger> MULTIPLY_LONG = testLong(Int128::multiply, (a, l) -> a.multiply($(l)));

    @Test
    public void multiply_simple() {
        assertThat(Int128.ZERO.multiply(Int128.ZERO)).isEqualTo(Int128.ZERO);
        assertThat(Int128.ZERO.multiply(Int128.ONE)).isEqualTo(Int128.ZERO);
        assertThat(Int128.ONE.multiply(Int128.ONE)).isEqualTo(Int128.ONE);
        assertThat(Int128.ONE.multiply(Int128.from(2))).isEqualTo(Int128.from(2));
        assertThat(Int128.from(2).multiply(Int128.from(2))).isEqualTo(Int128.from(4));
    }

    @Test
    public void multiply_positive() {
        MULTIPLY.assertMatch(Int128.fromBits(0, POW2[36] + 1), Int128.fromBits(0, POW2[36] + 2));
        MULTIPLY.assertMatch(Int128.fromBits(POW2[16], POW2[24] + 1), Int128.fromBits(POW2[10], POW2[18] + 2));
        MULTIPLY.assertMatch(Int128.fromBits(POW2[20] + 1, POW2[30] + 1), Int128.fromBits(POW2[20] - 1, POW2[18] - 1));
        MULTIPLY.assertMatch(Int128.fromBits(POW2[30] - 1, POW2[24] - 1), Int128.fromBits(POW2[15] - 1, POW2[12] - 1));
        MULTIPLY.assertMatch(Int128.from(Integer.MAX_VALUE), Int128.from(Integer.MAX_VALUE));
        MULTIPLY.assertMatch(Int128.from(Long.MAX_VALUE), Int128.from(Long.MAX_VALUE));
    }

    @Test
    public void multiply_negative() {
        MULTIPLY.assertMatch(Int128.from(-1), Int128.from(1));
        MULTIPLY.assertMatch(Int128.from(-1), Int128.from(-1));
        MULTIPLY.assertMatch(Int128.from(Integer.MAX_VALUE), Int128.from(Integer.MIN_VALUE));
        MULTIPLY.assertMatch(Int128.from(Integer.MIN_VALUE), Int128.from(Integer.MIN_VALUE));
        MULTIPLY.assertMatch(Int128.from(Long.MAX_VALUE), Int128.from(Long.MIN_VALUE));
        MULTIPLY.assertMatch(Int128.from(Long.MIN_VALUE), Int128.from(Long.MIN_VALUE));
    }

    @Test
    public void multiply_ultimate() {
        MULTIPLY.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void multiply_long_ultimate() {
        MULTIPLY_LONG.assertMatchAll(BIG_INTEGERS, EDGE_CASE_LONGS);
    }

    /** {@link Int128#divide(Int128)}, {@link Int128#divide(long)} **/

    private static final BiOpTester<Int128, BigInteger> DIVIDE = test(
        (a, b) -> b.isZero() ? willThrow(() -> a.divide(b)) : a.divide(b),
        (a, b) -> b.equals($0) ? null : a.divide(b)
    );
    private static final LongOpTester<Int128, BigInteger> DIVIDE_LONG = testLong(
        (a, l) -> l == 0 ? willThrow(() -> a.divide(l)) : a.divide(l),
        (a, l) -> l == 0 ? null : a.divide($(l))
    );

    private static final BigInteger $2_20 = $pow2(20);              // (1 << 20)
    private static final BigInteger $2_36 = $pow2(36);              // (1 << 36)
    private static final BigInteger $2_48 = $pow2(48);              // (1 << 48)
    private static final BigInteger $2_61 = $pow2(61);              // (1 << 61)
    private static final BigInteger $2_62 = $pow2(62);              // (1 << 62)
    private static final BigInteger $2_65 = $pow2(65);              // (1 << 65)
    private static final BigInteger $2_126 = $pow2(126);            // (1 << 126)

    private static final List<BigInteger> SIMPLE_POSITIVE_INTEGERS = listOf(
        $0, $1, $2, $10, $(20), $(100_000), $(100_000_000),
        INT8_MAX, INT16_MAX, INT32_MAX, INT64_MAX, UINT8_MAX, UINT16_MAX, UINT32_MAX,
        $2_20.subtract($1), $2_36, $2_36.add($1), $2_36.subtract($1), $2_48, $2_61, $2_62, $2_63
    );
    private static final List<BigInteger> SIMPLE_NEGATIVE_INTEGERS =
        SIMPLE_POSITIVE_INTEGERS.stream().map(BigInteger::negate).toList();

    @Test
    public void divide_trivial() {
        DIVIDE.assertMatch($2_64, $2_64);
        DIVIDE.assertMatch($2_65, $2_64);
        DIVIDE.assertMatch($2_65, $2_65);
        DIVIDE.assertMatch($2_65, $2_65.subtract($1));

        DIVIDE.assertMatch($2_126, $2_64);
        DIVIDE.assertMatch($2_126, $2_64.subtract($1));
        DIVIDE.assertMatch($2_126, $2_65);
        DIVIDE.assertMatch($2_126, $2_65.subtract($1));

        DIVIDE.assertMatch($("42133646023572558111"), $("16507721535087845730"));
        DIVIDE.assertMatch($("4342362639829079566657"), $("51932486189888438497"));
        DIVIDE.assertMatch($("37857809859903597488921"), $("985238115584182412665"));
        DIVIDE.assertMatch($("36571839423113907750982785311236"), $("31680056495764282357948599"));
    }

    @Test
    public void divide_simple_positive_only() {
        DIVIDE.assertMatchAll(SIMPLE_POSITIVE_INTEGERS);
    }

    @Test
    public void divide_simple_negative_only() {
        DIVIDE.assertMatchAll(SIMPLE_NEGATIVE_INTEGERS);
    }

    @Test
    public void divide_simple_positive_and_negative() {
        List<BigInteger> list = concatToList(SIMPLE_POSITIVE_INTEGERS, SIMPLE_NEGATIVE_INTEGERS);
        DIVIDE.assertMatchAll(list);
    }

    @Test
    public void divide_ultimate() {
        DIVIDE.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void divide_long_trivial() {
        // (hi, lo) / num:  hi is close to num
        DIVIDE_LONG.assertMatch(Int128.fromBits(1, 63), 3L);
        DIVIDE_LONG.assertMatch(Int128.fromBits(2, 63), 3L);
        DIVIDE_LONG.assertMatch(Int128.fromBits(3, 63), 3L);
        DIVIDE_LONG.assertMatch(Int128.fromBits(128, 63), 127L);
        DIVIDE_LONG.assertMatch(Int128.fromBits(127, 63), 127L);
        DIVIDE_LONG.assertMatch(Int128.fromBits(126, 63), 127L);

        // (hi, lo) / num:  hi is large signed
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, 3L);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, 11L);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, 12L);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, 127L);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE.subtract(1), 127L);

        // (hi, lo) / num:  hi and num are large signed
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, Long.MAX_VALUE);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, Long.MAX_VALUE - 1);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE, Long.MAX_VALUE - 2);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE.subtract(1), Long.MAX_VALUE - 1);
        DIVIDE_LONG.assertMatch(Int128.MAX_VALUE.subtract(1), Long.MAX_VALUE);

        // (hi, lo) / num:  hi is large unsigned (negative signed)
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(1), 127L);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(2), 127L);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(1), -1L);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(2), -1L);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(1), -2L);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(2), -2L);

        // (hi, lo) / num:  hi and num are large unsigned (negative signed)
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(1), Long.MIN_VALUE);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(1), Long.MIN_VALUE + 1);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(2), Long.MIN_VALUE);
        DIVIDE_LONG.assertMatch(Int128.MIN_VALUE.add(2), Long.MIN_VALUE + 1);

        // Various
        DIVIDE_LONG.assertMatch(Long.MAX_VALUE, 1000L);
        DIVIDE_LONG.assertMatch(118209477210L, 1985L);
        DIVIDE_LONG.assertMatch($("42133646023572558111"), 17L);
        DIVIDE_LONG.assertMatch($("4342362639829079566657"), 401824800369421L);
        DIVIDE_LONG.assertMatch($("37857809859903597488921"), 600832153508784573L);
        DIVIDE_LONG.assertMatch($("36571839423113907750982785311236"), 5333509404996611L);
    }

    @Test
    public void divide_long_ultimate() {
        DIVIDE_LONG.assertMatchAll(BIG_INTEGERS, EDGE_CASE_LONGS);
    }

    /** {@link Int128#remainder(Int128)}, {@link Int128#remainder(long)} **/

    private static final BiOpTester<Int128, BigInteger> REMAINDER = test(
        (a, b) -> b.isZero() ? willThrow(() -> a.remainder(b)) : a.remainder(b),
        (a, b) -> b.equals($0) ? null : a.remainder(b)
    );
    private static final LongOpTester<Int128, BigInteger> REMAINDER_LONG = testLong(
        (a, l) -> l == 0 ? willThrow(() -> a.remainder(l)) : a.remainder(l),
        (a, l) -> l == 0 ? null : a.remainder($(l))
    );

    @Test
    public void remainder_simple_positive_only() {
        REMAINDER.assertMatchAll(SIMPLE_POSITIVE_INTEGERS);
    }

    @Test
    public void remainder_simple_negative_only() {
        REMAINDER.assertMatchAll(SIMPLE_NEGATIVE_INTEGERS);
    }

    @Test
    public void remainder_simple_positive_and_negative() {
        List<BigInteger> list = concatToList(SIMPLE_POSITIVE_INTEGERS, SIMPLE_NEGATIVE_INTEGERS);
        REMAINDER.assertMatchAll(list);
    }

    @Test
    public void remainder_ultimate() {
        REMAINDER.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void remainder_long_ultimate() {
        REMAINDER_LONG.assertMatchAll(BIG_INTEGERS, EDGE_CASE_LONGS);
    }

    /** {@link Int128#negate()}, {@link Int128#and}, {@link Int128#andNot}, {@link Int128#or}, {@link Int128#xor} **/

    private static final UnOpTester<Int128, BigInteger> NEGATE = test(Int128::negate, BigInteger::negate);
    private static final UnOpTester<Int128, BigInteger> NOT = test(Int128::not, BigInteger::not);
    private static final BiOpTester<Int128, BigInteger> AND = test(Int128::and, BigInteger::and).noFitIn128();
    private static final BiOpTester<Int128, BigInteger> AND_NOT = test(Int128::andNot, BigInteger::andNot).noFitIn128();
    private static final BiOpTester<Int128, BigInteger> OR = test(Int128::or, BigInteger::or).noFitIn128();
    private static final BiOpTester<Int128, BigInteger> XOR = test(Int128::xor, BigInteger::xor).noFitIn128();

    @Test
    public void negate_simple() {
        assertThat(Int128.ZERO.negate()).isEqualTo(Int128.ZERO);
        assertThat(Int128.ONE.negate()).isEqualTo(Int128.from(-1));
        assertThat(Int128.MAX_VALUE.negate()).isEqualTo(Int128.MIN_VALUE.add(Int128.ONE));
        assertThat(Int128.MIN_VALUE.negate()).isEqualTo(Int128.MIN_VALUE);  // Same!!!
    }

    @Test
    public void negate_ultimate() {
        NEGATE.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void not_ultimate() {
        NOT.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void and_ultimate() {
        AND.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void andNot_ultimate() {
        AND_NOT.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void or_ultimate() {
        OR.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void xor_ultimate() {
        XOR.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#shiftLeft(int)}, {@link Int128#shiftRight(int)}, {@link Int128#shiftRightUnsigned(int)} **/

    private static final IntOpTester<Int128, BigInteger> SHIFT_LEFT = testInt(Int128::shiftLeft, BigInteger::shiftLeft);
    private static final IntOpTester<Int128, BigInteger> SHIFT_RIGHT = testInt(Int128::shiftRight, BigInteger::shiftRight);
    private static final IntOpTester<Int128, BigInteger> SHIFT_RIGHT_UNSIGNED = testInt(
        Int128::shiftRightUnsigned,
        (a, n) -> a.compareTo($0) >= 0 ? a.shiftRight(n) : a.add($2_128).shiftRight(n)
    );

    private static final List<Integer> POSITIONS = listOf(0, 1, 10, 32, 63, 64, 90, 127, -1, -2, -63, -64, -127);

    @Test
    public void shiftLeft_simple() {
        assertThat(Int128.ONE.shiftLeft(5)).isEqualTo(Int128.from(POW2[5]));
        assertThat(Int128.ONE.shiftLeft(4)).isEqualTo(Int128.from(POW2[4]));
        assertThat(Int128.ONE.shiftLeft(2)).isEqualTo(Int128.from(POW2[2]));
        assertThat(Int128.ONE.shiftLeft(1)).isEqualTo(Int128.from(POW2[1]));
        assertThat(Int128.ONE.shiftLeft(0)).isEqualTo(Int128.ONE);

        assertThat(Int128.fromBits(1, 1).shiftLeft(5)).isEqualTo(Int128.fromBits(POW2[5], POW2[5]));
        assertThat(Int128.fromBits(0, -1).shiftLeft(5)).isEqualTo(Int128.fromBits(POW2[5] - 1, -POW2[5]));
        assertThat(Int128.fromBits(0, -1).shiftLeft(8)).isEqualTo(Int128.fromBits(POW2[8] - 1, -POW2[8]));
        assertThat(Int128.fromBits(0, -1).shiftLeft(20)).isEqualTo(Int128.fromBits(POW2[20] - 1, -POW2[20]));
        assertThat(Int128.fromBits(0, -1).shiftLeft(60)).isEqualTo(Int128.fromBits(POW2[60] - 1, -POW2[60]));
        assertThat(Int128.fromBits(0, -1).shiftLeft(63)).isEqualTo(Int128.fromBits(POW2[63] - 1, -POW2[63]));
    }

    @Test
    public void shiftRight_simple() {
        assertThat(Int128.ONE.shiftRight(0)).isEqualTo(Int128.ONE);
        assertThat(Int128.ONE.shiftRight(1)).isEqualTo(Int128.ZERO);

        assertThat(Int128.from(POW2[5]).shiftRight(4)).isEqualTo(Int128.from(POW2[1]));
        assertThat(Int128.from(POW2[5]).shiftRight(5)).isEqualTo(Int128.ONE);

        assertThat(Int128.fromBits(1, 1).shiftRight(1)).isEqualTo(Int128.fromBits(0, POW2[63]));
        assertThat(Int128.fromBits(3, 3).shiftRight(1)).isEqualTo(Int128.fromBits(1, POW2[63] + 1));
    }

    @Test
    public void shiftRightUnsigned_simple() {
        assertThat(Int128.ONE.shiftRightUnsigned(0)).isEqualTo(Int128.ONE);
        assertThat(Int128.ONE.shiftRightUnsigned(1)).isEqualTo(Int128.ZERO);

        assertThat(Int128.from(POW2[5]).shiftRightUnsigned(4)).isEqualTo(Int128.from(POW2[1]));
        assertThat(Int128.from(POW2[5]).shiftRightUnsigned(5)).isEqualTo(Int128.ONE);

        assertThat(Int128.fromBits(1, 1).shiftRightUnsigned(1)).isEqualTo(Int128.fromBits(0, POW2[63]));
        assertThat(Int128.fromBits(3, 3).shiftRightUnsigned(1)).isEqualTo(Int128.fromBits(1, POW2[63] + 1));

        assertThat(Int128.from(-1).shiftRightUnsigned(0)).isEqualTo(Int128.fromBits(-1L, -1L));
        assertThat(Int128.from(-1).shiftRightUnsigned(1)).isEqualTo(Int128.fromBits(-1L >>> 1, -1L));
        assertThat(Int128.from(-1).shiftRightUnsigned(2)).isEqualTo(Int128.fromBits(-1L >>> 2, -1L));
        assertThat(Int128.from(-1).shiftRightUnsigned(63)).isEqualTo(Int128.fromBits(-1L >>> 63, -1L));
        assertThat(Int128.from(-1).shiftRightUnsigned(64)).isEqualTo(Int128.fromBits(0, -1L));
        assertThat(Int128.from(-1).shiftRightUnsigned(65)).isEqualTo(Int128.fromBits(0, -1L >>> 1));
        assertThat(Int128.from(-1).shiftRightUnsigned(127)).isEqualTo(Int128.fromBits(0, -1L >>> 63));
    }

    @Test
    public void shiftLeft_ultimate() {
        SHIFT_LEFT.assertMatchAll(BIG_INTEGERS, POSITIONS);
    }

    @Test
    public void shiftRight_ultimate() {
        SHIFT_RIGHT.assertMatchAll(BIG_INTEGERS, POSITIONS);
    }

    @Test
    public void shiftRightUnsigned_ultimate() {
        SHIFT_RIGHT_UNSIGNED.assertMatchAll(BIG_INTEGERS, POSITIONS);
    }

    /** {@link Int128#bitAt}, {@link Int128#setBitAt}, {@link Int128#clearBitAt}, {@link Int128#flipBitAt} **/

    private static final IntOpTester<Integer, Integer>
        BIT_AT = testInt(Int128::bitAt, (a, n) -> castToInt(a.testBit(n)));
    private static final IntOpTester<Int128, BigInteger>
        SET_BIT_AT = testInt(Int128::setBitAt, BigInteger::setBit).noFitIn128();
    private static final IntOpTester<Int128, BigInteger>
        CLEAR_BIT_AT = testInt(Int128::clearBitAt, BigInteger::clearBit).noFitIn128();
    private static final IntOpTester<Int128, BigInteger>
        FLIP_BIT_AT = testInt(Int128::flipBitAt, BigInteger::flipBit).noFitIn128();

    @Test
    public void bitAt_simple() {
        assertThat(Int128.fromBits(0, 0x0f).bitAt(0)).isEqualTo(1);
        assertThat(Int128.fromBits(0, 0x0f).bitAt(1)).isEqualTo(1);
        assertThat(Int128.fromBits(0, 0x0f).bitAt(3)).isEqualTo(1);
        assertThat(Int128.fromBits(0, 0x0f).bitAt(4)).isEqualTo(0);
        assertThat(Int128.fromBits(-1, -100).bitAt(0)).isEqualTo(0);
        assertThat(Int128.fromBits(-1, -100).bitAt(1)).isEqualTo(0);
        assertThat(Int128.fromBits(-1, -100).bitAt(2)).isEqualTo(1);
        assertThat(Int128.fromBits(-1, -100).bitAt(6)).isEqualTo(0);
    }

    @Test
    public void setBitAt_simple() {
        assertThat(Int128.fromBits(0, 0).setBitAt(0)).isEqualTo(Int128.fromBits(0, 1));
        assertThat(Int128.fromBits(0, 0).setBitAt(1)).isEqualTo(Int128.fromBits(0, 2));
        assertThat(Int128.fromBits(0, 0).setBitAt(2)).isEqualTo(Int128.fromBits(0, 4));
        assertThat(Int128.fromBits(0, 0).setBitAt(5)).isEqualTo(Int128.fromBits(0, 32));
        assertThat(Int128.fromBits(0, 100).setBitAt(0)).isEqualTo(Int128.fromBits(0, 101));
        assertThat(Int128.fromBits(0, 100).setBitAt(1)).isEqualTo(Int128.fromBits(0, 102));
        assertThat(Int128.fromBits(0, 100).setBitAt(4)).isEqualTo(Int128.fromBits(0, 116));
        assertThat(Int128.fromBits(0, 100).setBitAt(5)).isEqualTo(Int128.fromBits(0, 100));
        assertThat(Int128.fromBits(-1, -100).setBitAt(0)).isEqualTo(Int128.fromBits(-1, -99));
        assertThat(Int128.fromBits(-1, -100).setBitAt(1)).isEqualTo(Int128.fromBits(-1, -98));
        assertThat(Int128.fromBits(-1, -100).setBitAt(2)).isEqualTo(Int128.fromBits(-1, -100));
        assertThat(Int128.fromBits(-1, -100).setBitAt(6)).isEqualTo(Int128.fromBits(-1, -36));
    }

    @Test
    public void bitAt_ultimate() {
        BIT_AT.assertMatchAll(BIG_INTEGERS, listOf(0, 1, 2, 10, 20, 32, 63, 64, 70, 80, 100, 120, 125, 126, 127));
    }

    @Test
    public void setBitAt_ultimate() {
        SET_BIT_AT.assertMatchAll(BIG_INTEGERS, listOf(0, 1, 2, 10, 20, 32, 63, 64, 70, 80, 100, 120, 125, 126));
    }

    @Test
    public void clearBitAt_ultimate() {
        CLEAR_BIT_AT.assertMatchAll(BIG_INTEGERS, listOf(0, 1, 2, 10, 20, 32, 63, 64, 70, 80, 100, 120, 125, 126));
    }

    @Test
    public void flipBitAt_ultimate() {
        FLIP_BIT_AT.assertMatchAll(BIG_INTEGERS, listOf(0, 1, 2, 10, 20, 32, 63, 64, 70, 80, 100, 120, 125, 126));
    }

    @Test
    public void bit_operations_with_sign_bit_ultimate() {
        int pos = Int128.BITS - 1;
        Int128 pow127 = Int128.fromBits(POW2[63], 0);
        for (BigInteger num : BIG_INTEGERS) {
            Int128 value = Int128.from(num);
            boolean bit = value.bitAt(pos) == 1;
            assertThat(value.setBitAt(pos)).isEqualTo(bit ? value : value.add(pow127));
            assertThat(value.clearBitAt(pos)).isEqualTo(bit ? value.subtract(pow127) : value);
            assertThat(value.flipBitAt(pos)).isEqualTo(bit ? value.subtract(pow127) : value.add(pow127));
        }
    }

    /** {@link Int128#numberOfLeadingZeros()}, {@link Int128#numberOfTrailingZeros()}, {@link Int128#bitCount()} **/

    private static final UnOpTester<Integer, Integer> LEADING_ZEROS = test(
        Int128::numberOfLeadingZeros,
        a -> a.compareTo($0) >= 0 ? 128 - a.bitLength() : 0
    );
    private static final UnOpTester<Integer, Integer> TRAILING_ZEROS = test(
        Int128::numberOfTrailingZeros,
        a -> a.compareTo($0) == 0 ? 128 : a.getLowestSetBit()
    );
    private static final UnOpTester<Integer, Integer> BIT_COUNT = test(
        Int128::bitCount,
        a -> a.compareTo($0) >= 0 ? a.bitCount() : 128 - a.bitCount()
    );

    @Test
    public void numberOfLeadingZeros_simple() {
        LEADING_ZEROS.assertMatch($0);
        LEADING_ZEROS.assertMatch($1);
        LEADING_ZEROS.assertMatch($10);
        LEADING_ZEROS.assertMatch($2_31);
        LEADING_ZEROS.assertMatch($2_32);
        LEADING_ZEROS.assertMatch($2_63);
        LEADING_ZEROS.assertMatch($2_64);
        LEADING_ZEROS.assertMatch($(-1));
        LEADING_ZEROS.assertMatch($(-100));
    }

    @Test
    public void numberOfTrailingZeros_simple() {
        TRAILING_ZEROS.assertMatch($0);
        TRAILING_ZEROS.assertMatch($1);
        TRAILING_ZEROS.assertMatch($10);
        TRAILING_ZEROS.assertMatch($2_31);
        TRAILING_ZEROS.assertMatch($2_32);
        TRAILING_ZEROS.assertMatch($2_63);
        TRAILING_ZEROS.assertMatch($2_64);
        TRAILING_ZEROS.assertMatch($(-1));
        TRAILING_ZEROS.assertMatch($(-100));
    }

    @Test
    public void bitCount_simple() {
        BIT_COUNT.assertMatch($0);
        BIT_COUNT.assertMatch($1);
        BIT_COUNT.assertMatch($10);
        BIT_COUNT.assertMatch($2_31);
        BIT_COUNT.assertMatch($2_63);
        BIT_COUNT.assertMatch($2_64);
        BIT_COUNT.assertMatch($(-1));
        BIT_COUNT.assertMatch($(-100));
    }

    @Test
    public void numberOfLeadingZeros_ultimate() {
        LEADING_ZEROS.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void numberOfTrailingZeros_ultimate() {
        TRAILING_ZEROS.assertMatchAll(BIG_INTEGERS);
    }

    @Test
    public void bitCount_ultimate() {
        BIT_COUNT.assertMatchAll(BIG_INTEGERS);
    }

    /** {@link Int128#fastZeroOrValue}, {@link Int128#fastZeroOrMinusOne}, {@link Int128#fastZeroOrOne} **/

    @Test
    public void fastZeroOrValue_ultimate() {
        for (long a : EDGE_CASE_LONGS) {
            for (long b : EDGE_CASE_LONGS) {
                assertThat(Int128.fastZeroOrValue(a, b)).isEqualTo(slowZeroOrValue(a, b));
            }
        }
    }

    @Test
    public void fastZeroOrMinusOne_ultimate() {
        for (long a : EDGE_CASE_LONGS) {
            assertThat(Int128.fastZeroOrMinusOne(a)).isEqualTo(slowZeroOrMinusOne(a));
        }
    }

    @Test
    public void fastZeroOrOne_ultimate() {
        for (long a : EDGE_CASE_LONGS) {
            assertThat(Int128.fastZeroOrOne(a)).isEqualTo(slowZeroOrOne(a));
        }
    }

    private static long slowZeroOrValue(long test, long value) {
        return test < 0 ? value : 0;
    }

    private static long slowZeroOrMinusOne(long test) {
        return test < 0 ? -1 : 0;
    }

    private static int slowZeroOrOne(long test) {
        return test == 0 ? 0 : 1;
    }

    /** {@link Int128#toBigInteger(double)} **/

    @Test
    public void toBigInteger_positive() {
        assertThat(Int128.toBigInteger(0.0)).isEqualTo($0);
        assertThat(Int128.toBigInteger(0.1)).isEqualTo($0);
        assertThat(Int128.toBigInteger(0.5)).isEqualTo($0);

        assertThat(Int128.toBigInteger(1.0)).isEqualTo($1);
        assertThat(Int128.toBigInteger(2.0)).isEqualTo($2);
        assertThat(Int128.toBigInteger(10.0)).isEqualTo($10);
        assertThat(Int128.toBigInteger(1_000_000_000.0)).isEqualTo($("1000000000"));
        assertThat(Int128.toBigInteger(1_000_000_000_000.0)).isEqualTo($("1000000000000"));
        assertThat(Int128.toBigInteger(1_000_000_000_001.0)).isEqualTo($("1000000000001"));

        assertThat(Int128.toBigInteger(2147483648.0)).isEqualTo($2_31);
        assertThat(Int128.toBigInteger(4294967296.0)).isEqualTo($2_32);
        assertThat(Int128.toBigInteger(9223372036854775808.0)).isEqualTo($2_63);
    }

    @Test
    public void toBigInteger_negative() {
        assertThat(Int128.toBigInteger(-0.0)).isEqualTo($0);
        assertThat(Int128.toBigInteger(-0.1)).isEqualTo($0);
        assertThat(Int128.toBigInteger(-0.5)).isEqualTo($0);

        assertThat(Int128.toBigInteger(-1.0)).isEqualTo($1.negate());
        assertThat(Int128.toBigInteger(-2.0)).isEqualTo($2.negate());
        assertThat(Int128.toBigInteger(-10.0)).isEqualTo($10.negate());
        assertThat(Int128.toBigInteger(-1_000_000_000.0)).isEqualTo($("-1000000000"));
        assertThat(Int128.toBigInteger(-1_000_000_000_000.0)).isEqualTo($("-1000000000000"));
        assertThat(Int128.toBigInteger(-1_000_000_000_001.0)).isEqualTo($("-1000000000001"));

        assertThat(Int128.toBigInteger(-2147483648.0)).isEqualTo($2_31.negate());
        assertThat(Int128.toBigInteger(-4294967296.0)).isEqualTo($2_32.negate());
        assertThat(Int128.toBigInteger(-9223372036854775808.0)).isEqualTo($2_63.negate());
    }

    /** Assertion utils **/

    @CheckReturnValue
    private @NotNull Int128Subject assertThatInt128(@NotNull Int128 actual) {
        return new Int128Subject(actual);
    }

    @CanIgnoreReturnValue
    private record Int128Subject(@NotNull Int128 actual) {
        public @NotNull Int128Subject isEqualTo(@NotNull Int128 other) {
            assertEquality(actual, other);
            return this;
        }

        public @NotNull Int128Subject internalConsistency() {
            return isEqualTo(actual)
                .roundtripLongBits()
                .roundtripByteArrayBits()
                .roundtripIntArrayBits()
                .roundtripLongArrayBits()
                .roundtripString()
                .roundtripBigInteger()
                .roundtripUnsignedLong()
                .roundtripLong()
                .is64BitConsistency()
                .is32BitConsistency()
                .internalConstruction();
        }

        public @NotNull Int128Subject roundtripLongBits() {
            Int128 copy = Int128.fromBits(actual.highBits(), actual.lowBits());
            return isEqualTo(copy);
        }

        public @NotNull Int128Subject roundtripByteArrayBits() {
            byte[] bytes = actual.toByteArray();
            Int128 copy = Int128.fromBits(bytes);
            return isEqualTo(copy);
        }

        public @NotNull Int128Subject roundtripIntArrayBits() {
            int[] ints = actual.toIntArray();
            Int128 copy = Int128.fromBits(ints);
            return isEqualTo(copy);
        }

        public @NotNull Int128Subject roundtripLongArrayBits() {
            long[] longs = actual.toLongArray();
            Int128 copy = Int128.fromBits(longs);
            return isEqualTo(copy);
        }

        public @NotNull Int128Subject roundtripString() {
            String str = actual.toString();
            Int128 copy = Int128.from(str);
            return isEqualTo(copy);
        }

        public @NotNull Int128Subject roundtripBigInteger() {
            BigInteger bigInteger = actual.toBigInteger();
            Int128 copy = Int128.from(bigInteger);
            return isEqualTo(copy);
        }

        private @NotNull Int128Subject roundtripUnsignedLong() {
            if (actual.is64BitUnsigned()) {
                UnsignedLong unsignedLong = actual.toUnsignedLong();
                Int128 copy = Int128.from(unsignedLong);
                return isEqualTo(copy);
            }
            assertThrows(AssertionError.class, () -> actual.toUnsignedLong());
            return this;
        }

        public @NotNull Int128Subject roundtripLong() {
            if (actual.is64Bit()) {
                long parsed = BasicParsing.parseLongSafe(actual.toString());
                Int128 copy = Int128.from(parsed);
                return isEqualTo(copy);
            }
            return this;
        }

        public @NotNull Int128Subject is64BitConsistency() {
            boolean isPositiveOrZero = actual.compareTo(Int128.ZERO) >= 0;
            assertThat(actual.is64Bit()).isEqualTo(RANGE_INT64.contains(actual.toBigInteger()));
            if (actual.is64Bit()) {
                assertThat(actual.longValue()).isEqualTo(Long.parseLong(actual.toString()));
                assertThat(actual.longValueExact()).isEqualTo(actual.toBigInteger().longValueExact());
                assertThat(actual.longValueSaturated()).isEqualTo(actual.toBigInteger().longValueExact());
                assertThat(actual.highBits()).isEqualTo(isPositiveOrZero ? 0 : -1);
                assertThat(actual.compareTo(Int128.from(Long.MAX_VALUE))).isAnyOf(0, -1);
                assertThat(actual.compareTo(Int128.from(Long.MIN_VALUE))).isAnyOf(0, 1);
            } else {
                assertThat(actual.compareTo(Int128.from(Long.MAX_VALUE))).isEqualTo(isPositiveOrZero ? 1 : -1);
                assertThat(actual.compareTo(Int128.from(Long.MIN_VALUE))).isEqualTo(isPositiveOrZero ? 1 : -1);
                assertThrows(NumberFormatException.class, () -> Long.parseLong(actual.toString()));
                assertThrows(AssertionError.class, () -> actual.longValueExact());
                assertThat(actual.longValueSaturated()).isEqualTo(isPositiveOrZero ? Long.MAX_VALUE : Long.MIN_VALUE);
            }
            return this;
        }

        private @NotNull Int128Subject is32BitConsistency() {
            boolean isPositiveOrZero = actual.compareTo(Int128.ZERO) >= 0;
            assertThat(actual.is32Bit()).isEqualTo(RANGE_INT32.contains(actual.toBigInteger()));
            if (actual.is32Bit()) {
                assertThat(actual.intValue()).isEqualTo(Integer.parseInt(actual.toString()));
                assertThat(actual.intValueExact()).isEqualTo(actual.toBigInteger().intValueExact());
                assertThat(actual.intValueSaturated()).isEqualTo(actual.toBigInteger().intValueExact());
                assertThat(actual.highBits()).isEqualTo(isPositiveOrZero ? 0 : -1);
                assertThat(actual.compareTo(Int128.from(Integer.MAX_VALUE))).isAnyOf(0, -1);
                assertThat(actual.compareTo(Int128.from(Integer.MIN_VALUE))).isAnyOf(0, 1);
            } else {
                assertThat(actual.compareTo(Int128.from(Integer.MAX_VALUE))).isEqualTo(isPositiveOrZero ? 1 : -1);
                assertThat(actual.compareTo(Int128.from(Integer.MIN_VALUE))).isEqualTo(isPositiveOrZero ? 1 : -1);
                assertThrows(NumberFormatException.class, () -> Integer.parseInt(actual.toString()));
                assertThrows(AssertionError.class, () -> actual.intValueExact());
                assertThat(actual.intValueSaturated()).isEqualTo(isPositiveOrZero ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            }
            return this;
        }

        public @NotNull Int128Subject internalConstruction() {
            assertConstruction(actual.toString());
            return this;
        }
    }

    private static void assertEquality(@NotNull Int128 lhs, @NotNull Int128 rhs) {
        assertThat(lhs.highBits()).isEqualTo(rhs.highBits());
        assertThat(lhs.lowBits()).isEqualTo(rhs.lowBits());

        assertThat(lhs.equals(rhs)).isTrue();
        assertThat(rhs.equals(lhs)).isTrue();
        assertThat(lhs.hashCode()).isEqualTo(rhs.hashCode());
        assertThat(lhs.toString()).isEqualTo(rhs.toString());

        assertThat(lhs.compareTo(rhs)).isEqualTo(0);
        assertThat(rhs.compareTo(lhs)).isEqualTo(0);
        assertThat(Int128.compare(lhs, rhs)).isEqualTo(0);
        assertThat(Int128.compare(rhs, lhs)).isEqualTo(0);
        assertThat(Int128.COMPARATOR.compare(lhs, rhs)).isEqualTo(0);
        assertThat(Int128.COMPARATOR.compare(rhs, lhs)).isEqualTo(0);
    }

    private static void assertConstruction(@NotNull String str) {
        BigInteger bigInteger = new BigInteger(str);

        assertConstructionFromString(str);
        assertConstructionFromBigInteger(bigInteger);
        assertConstructionFromHexString(bigInteger.toString(16));

        if (RANGE_INT64.contains(bigInteger)) {
            long num = Long.parseLong(str);
            assertConstructionFromLong(num);
            assertConstructionFromLongBits(0, num);
            assertConstructionFromLongArrayBits(new long[] { 0, num });
        } else {
            long high = bigInteger.shiftRight(64).longValue();
            long low = bigInteger.longValue();
            assertConstructionFromLongBits(high, low);
            assertConstructionFromLongArrayBits(new long[] { high, low });
        }
    }

    private static void assertConstructionFromString(@NotNull String str) {
        Int128 value = Int128.from(str);
        assertThat(value.toString()).isEqualTo(str);
        assertThat(value.toBigInteger()).isEqualTo(new BigInteger(str));
    }

    private static void assertConstructionFromHexString(@NotNull String hex) {
        Int128 value = Int128.fromHex(hex);
        assertThat(value.toBigInteger()).isEqualTo(new BigInteger(hex, 16));
        // hex string?
    }

    private static void assertConstructionFromBigInteger(@NotNull BigInteger bigInteger) {
        Int128 value = Int128.from(bigInteger);

        assertThat(value.toBigInteger()).isEqualTo(bigInteger);

        assertThat(value.signum()).isEqualTo(bigInteger.signum());
        assertThat(value.isPositive()).isEqualTo(bigInteger.compareTo($0) > 0);
        assertThat(value.isNegative()).isEqualTo(bigInteger.compareTo($0) < 0);
        assertThat(value.isZero()).isEqualTo(bigInteger.equals($0));

        assertThat(value.longValue()).isEqualTo(bigInteger.longValue());
        assertThat(value.intValue()).isEqualTo(bigInteger.intValue());
        assertThat(value.shortValue()).isEqualTo(bigInteger.shortValue());
        assertThat(value.byteValue()).isEqualTo(bigInteger.byteValue());
        assertThat(value.floatValue()).isEqualTo(bigInteger.floatValue());
        assertThat(value.doubleValue()).isEqualTo(bigInteger.doubleValue());

        assertThat(value.toString()).isEqualTo(bigInteger.toString());
        assertThat(value.toString(2)).isEqualTo(bigInteger.toString(2));
        assertThat(value.toString(8)).isEqualTo(bigInteger.toString(8));
        assertThat(value.toString(16)).isEqualTo(bigInteger.toString(16));

        if (bigInteger.compareTo($0) >= 0) {
            assertThat(value.toBinaryString()).isEqualTo(Strings.padStart(bigInteger.toString(2), 128, '0'));
            assertThat(value.toHexString()).isEqualTo(Strings.padStart(bigInteger.toString(16), 32, '0'));
        }
    }

    private static void assertConstructionFromLong(long num) {
        Int128 value = Int128.from(num);

        assertThat(value.longValue()).isEqualTo(num);
        assertThat(value.intValue()).isEqualTo((int) num);
        assertThat(value.shortValue()).isEqualTo((short) num);
        assertThat(value.byteValue()).isEqualTo((byte) num);
        assertThat(value.floatValue()).isEqualTo((float) num);
        assertThat(value.doubleValue()).isEqualTo((double) num);

        assertThat(value.equals(num)).isTrue();
        assertThat(value.equals(~num)).isFalse();
        assertThat(value.toString()).isEqualTo(String.valueOf(num));
        assertThat(value.toBigInteger()).isEqualTo(new BigInteger(String.valueOf(num)));

        if (num >= 0) {
            assertThat(value.highBits()).isEqualTo(0);
            assertThat(value.lowBits()).isEqualTo(num);
            assertThat(value.toBinaryString()).isEqualTo(Strings.padStart(Long.toBinaryString(num), 128, '0'));
        } else {
            assertThat(value.highBits()).isEqualTo(-1);
            assertThat(value.lowBits()).isEqualTo(num);
            assertThat(value.toBinaryString()).isEqualTo(Strings.padStart(Long.toBinaryString(num), 128, '1'));
        }
    }

    private static void assertConstructionFromDouble(double num) {
        Int128 value = Int128.from(num);

        assertThat(value.doubleValue()).isEqualTo((double) (long) (num));
        assertThat(value.floatValue()).isEqualTo((float) (long) (num));

        if (value.is64Bit()) {
            assertThat(value.longValue()).isEqualTo((long) num);
        }
        if (value.is32Bit()) {
            assertThat(value.intValue()).isEqualTo((int) num);
            assertThat(value.shortValue()).isEqualTo((short) num);
            assertThat(value.byteValue()).isEqualTo((byte) num);
        }

        if (value.is64Bit()) {
            assertThat(value.toString()).isEqualTo(String.valueOf((long) num));
            assertThat(value.toBigInteger()).isEqualTo($((long) num));
        } else {
            BigInteger expected = Int128.toBigInteger(num);  // the only reliable way to convert double to BigInteger
            assertThat(value.toString()).isEqualTo(expected.toString());
            assertThat(value.toBigInteger()).isEqualTo(expected);
        }
    }

    private static void assertConstructionFromLongBits(long high, long low) {
        Int128 value = Int128.fromBits(high, low);
        assertThat(value.highBits()).isEqualTo(high);
        assertThat(value.lowBits()).isEqualTo(low);

        assertThat(value.longValue()).isEqualTo(low);
        assertThat(value.intValue()).isEqualTo((int) low);
        assertThat(value.toBigInteger()).isEqualTo(bitsToBigInteger(high, low));
    }

    private static void assertConstructionFromLongArrayBits(long[] longs) {
        Int128 value = Int128.fromBits(longs);
        assertThat(value.highBits()).isEqualTo(longs[0]);
        assertThat(value.lowBits()).isEqualTo(longs[1]);

        assertThat(value.longValue()).isEqualTo(longs[1]);
        assertThat(value.intValue()).isEqualTo((int) longs[1]);
        assertThat(value.toBigInteger()).isEqualTo(bitsToBigInteger(longs[0], longs[1]));
    }

    private static @NotNull BigInteger bitsToBigInteger(long highBits, long lowBits) {
        return $(highBits).shiftLeft(64).add(UnsignedLong.fromLongBits(lowBits).bigIntegerValue());
    }

    /** Testers **/

    private static <X, Y> @NotNull UnOpTester<X, Y> test(@NotNull Function<Int128, X> op,
                                                         @NotNull Function<BigInteger, Y> $op) {
        return new UnOpTester<>(op, $op);
    }

    private static <X, Y> @NotNull BiOpTester<X, Y> test(@NotNull BiFunction<Int128, Int128, X> op,
                                                         @NotNull BiFunction<BigInteger, BigInteger, Y> $op) {
        return new BiOpTester<>(op, $op);
    }

    private static <X, Y> @NotNull IntOpTester<X, Y> testInt(@NotNull BiFunction<Int128, Integer, X> op,
                                                             @NotNull BiFunction<BigInteger, Integer, Y> $op) {
        return new IntOpTester<>(op, $op);
    }

    private static <X, Y> @NotNull LongOpTester<X, Y> testLong(@NotNull BiFunction<Int128, Long, X> op,
                                                               @NotNull BiFunction<BigInteger, Long, Y> $op) {
        return new LongOpTester<>(op, $op);
    }

    private static abstract class Tester<T extends Tester<T>> {
        protected boolean fitIn128 = true;
        protected Int128 a;
        protected BigInteger $a;

        public @NotNull T fitIn128() {
            fitIn128 = true;
            return castAny(this);
        }

        public @NotNull T noFitIn128() {
            fitIn128 = false;
            return castAny(this);
        }

        protected @NotNull T withA(@NotNull BigInteger bigInteger) {
            a = Int128.from(bigInteger);
            $a = bigInteger;
            return castAny(this);
        }

        protected @NotNull T withA(@NotNull Int128 int128) {
            a = int128;
            $a = int128.toBigInteger();
            return castAny(this);
        }

        protected @NotNull T withA(long value) {
            return withA($(value));
        }

        protected <X, Y> void castAndAssertEquality(X c, Y $c) {
            Object actual = c instanceof Int128 int128 ? int128.toBigInteger() : c;
            Object expected = $c instanceof BigInteger bigInteger && fitIn128 ? RANGE_INT128.fitIn(bigInteger) : $c;
            assertThat(actual).isEqualTo(expected);
        }
    }

    private static class UnOpTester<X, Y> extends Tester<UnOpTester<X, Y>> {
        private final Function<Int128, X> op;
        private final Function<BigInteger, Y> $op;

        public UnOpTester(@NotNull Function<Int128, X> op, @NotNull Function<BigInteger, Y> $op) {
            this.op = op;
            this.$op = $op;
        }

        public void assertMatchAll(@NotNull List<BigInteger> list) {
            for (BigInteger num : list) {
                assertMatch(num);
            }
        }

        public void assertMatch(@NotNull BigInteger num) {
            withA(num).assertMatch();
        }

        public void assertMatch(@NotNull Int128 num) {
            withA(num).assertMatch();
        }

        private void assertMatch() {
            assert a != null && $a != null : newInternalError("Tester not initialized properly");
            X c = op.apply(a);
            Y $c = $op.apply($a);
            castAndAssertEquality(c, $c);
        }
    }

    private static class BiOpTester<X, Y> extends Tester<BiOpTester<X, Y>> {
        private final BiFunction<Int128, Int128, X> op;
        private final BiFunction<BigInteger, BigInteger, Y> $op;
        private Int128 b;
        private BigInteger $b;

        public BiOpTester(@NotNull BiFunction<Int128, Int128, X> op, @NotNull BiFunction<BigInteger, BigInteger, Y> $op) {
            this.op = op;
            this.$op = $op;
        }

        public void assertMatchAll(@NotNull Iterable<BigInteger> list) {
            for (BigInteger left : list) {
                withA(left);
                for (BigInteger right : list) {
                    withB(right);
                    assertMatch();
                }
            }
        }

        public void assertMatch(@NotNull BigInteger left, @NotNull BigInteger right) {
            withA(left).withB(right).assertMatch();
        }

        public void assertMatch(@NotNull Int128 left, @NotNull Int128 right) {
            withA(left).withB(right).assertMatch();
        }

        public void assertMatch(long left, long right) {
            withA(left).withB(right).assertMatch();
        }

        private @NotNull BiOpTester<X, Y> withB(@NotNull BigInteger bigInteger) {
            b = Int128.from(bigInteger);
            $b = bigInteger;
            return this;
        }

        private @NotNull BiOpTester<X, Y> withB(@NotNull Int128 int128) {
            b = int128;
            $b = int128.toBigInteger();
            return this;
        }

        private @NotNull BiOpTester<X, Y> withB(long value) {
            return withB($(value));
        }

        private void assertMatch() {
            assert a != null && b != null && $a != null && $b != null : newInternalError("Tester not initialized properly");
            X c = op.apply(a, b);
            Y $c = $op.apply($a, $b);
            castAndAssertEquality(c, $c);
        }
    }

    private static abstract class NumOpTester<X, Y, N> extends Tester<NumOpTester<X, Y, N>> {
        private final BiFunction<Int128, N, X> op;
        private final BiFunction<BigInteger, N, Y> $op;

        public NumOpTester(@NotNull BiFunction<Int128, N, X> op, @NotNull BiFunction<BigInteger, N, Y> $op) {
            this.op = op;
            this.$op = $op;
        }

        public void assertMatchAll(@NotNull Iterable<BigInteger> list, @NotNull Iterable<N> nums) {
            for (BigInteger a : list) {
                withA(a);
                for (N num : nums) {
                    assertMatch(num);
                }
            }
        }

        public void assertMatch(@NotNull long left, @NotNull N right) {
            withA(left).assertMatch(right);
        }

        public void assertMatch(@NotNull BigInteger left, @NotNull N right) {
            withA(left).assertMatch(right);
        }

        public void assertMatch(@NotNull Int128 left, @NotNull N right) {
            withA(left).assertMatch(right);
        }

        private void assertMatch(@NotNull N num) {
            assert a != null && $a != null : newInternalError("Tester not initialized properly");
            X c = op.apply(a, num);
            Y $c = $op.apply($a, num);
            castAndAssertEquality(c, $c);
        }
    }

    private static class IntOpTester<X, Y> extends NumOpTester<X, Y, Integer> {
        public IntOpTester(@NotNull BiFunction<Int128, Integer, X> op, @NotNull BiFunction<BigInteger, Integer, Y> $op) {
            super(op, $op);
        }
        @Override public @NotNull IntOpTester<X, Y> noFitIn128() {
            return castAny(super.noFitIn128());
        }
    }

    private static class LongOpTester<X, Y> extends NumOpTester<X, Y, Long> {
        public LongOpTester(@NotNull BiFunction<Int128, Long, X> op, @NotNull BiFunction<BigInteger, Long, Y> $op) {
            super(op, $op);
        }
        @Override public @NotNull LongOpTester<X, Y> noFitIn128() {
            return castAny(super.noFitIn128());
        }
    }

    private static <T> T willThrow(@NotNull Executable executable) {
        assertThrows(AssertionError.class, executable);
        return null;
    }
}
