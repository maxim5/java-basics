package io.spbx.util.base.math;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class MathOpsTest {
    @Test
    public void modInverse16_ultimate() {
        short[] cases = { 1, 11, 101, 201, 1001, 12345, -1, -10001, Short.MAX_VALUE, -Short.MAX_VALUE };
        for (short d : cases) {
            short inv = MathOps.modInverse16(d);
            short mult = (short) (d * inv);
            assertThat(mult).isEqualTo(1);
        }
    }

    @Test
    public void modInverse32_ultimate() {
        int[] cases = { 1, 11, 101, 201, 1001, 12345, -1, -10001, 0xffff, -0xffff, Integer.MAX_VALUE, -Integer.MAX_VALUE };
        for (int d : cases) {
            int inv = MathOps.modInverse32(d);
            int mult = d * inv;
            assertThat(mult).isEqualTo(1);
        }
    }

    @Test
    public void modInverse64_ultimate() {
        long[] cases = {
            1, 11, 101, 201, 1001, 12345, -1, -10001, 0xffff, -0xffff,
            0x7fff_ffff, -0x7fff_ffff, 0x1234_ffff_ffffL, -0x1234_ffff_ffffL, Long.MAX_VALUE, -Long.MAX_VALUE
        };
        for (long d : cases) {
            long inv = MathOps.modInverse64(d);
            long mult = d * inv;
            assertThat(mult).isEqualTo(1L);
        }
    }

    @Test
    public void multiplyHighSigned_simple() {
        assertThat(MathOps.multiplyHighSigned(0, 0)).isEqualTo(0);
        assertThat(MathOps.multiplyHighSigned(12345, 67890)).isEqualTo(0);
        assertThat(MathOps.multiplyHighSigned(Long.MAX_VALUE, 0)).isEqualTo(0);
        assertThat(MathOps.multiplyHighSigned(Long.MAX_VALUE, 1)).isEqualTo(0);

        assertThat(MathOps.multiplyHighSigned(1L << 62, 2)).isEqualTo(0);
        assertThat(MathOps.multiplyHighSigned(1L << 62, 4)).isEqualTo(1);
        assertThat(MathOps.multiplyHighSigned(1L << 62, 8)).isEqualTo(2);
        assertThat(MathOps.multiplyHighSigned(1L << 62, 1L << 32)).isEqualTo(1L << 30);
        assertThat(MathOps.multiplyHighSigned(1L << 62, 1L << 62)).isEqualTo(1L << 60);

        assertThat(MathOps.multiplyHighSigned(Long.MIN_VALUE, 2)).isEqualTo(-1);
        assertThat(MathOps.multiplyHighSigned(Long.MIN_VALUE, 4)).isEqualTo(-2);
        assertThat(MathOps.multiplyHighSigned(Long.MIN_VALUE, 8)).isEqualTo(-4);

        assertThat(MathOps.multiplyHighSigned(-1, 2)).isEqualTo(-1);
        assertThat(MathOps.multiplyHighSigned(-1, 4)).isEqualTo(-1);
        assertThat(MathOps.multiplyHighSigned(-1, 8)).isEqualTo(-1);
    }

    @Test
    public void multiplyHighSigned_ultimate() {
        long[] cases = {
            1, 11, 101, 201, 1001, 12345, -1, -10001, 0xffff, -0xffff,
            0x7fff_ffff, -0x7fff_ffff, 0x1234_ffff_ffffL, -0x1234_ffff_ffffL, Long.MAX_VALUE, -Long.MAX_VALUE
        };
        for (long a : cases) {
            for (long b : cases) {
                assertThat(MathOps.multiplyHighSigned(a, b)).isEqualTo(Math.multiplyHigh(a, b));
            }
        }
    }

    @Test
    public void multiplyHighUnsigned_simple() {
        assertThat(MathOps.multiplyHighUnsigned(0, 0)).isEqualTo(0);
        assertThat(MathOps.multiplyHighUnsigned(12345, 67890)).isEqualTo(0);
        assertThat(MathOps.multiplyHighUnsigned(Long.MAX_VALUE, 0)).isEqualTo(0);
        assertThat(MathOps.multiplyHighUnsigned(Long.MAX_VALUE, 1)).isEqualTo(0);

        assertThat(MathOps.multiplyHighUnsigned(1L << 62, 2)).isEqualTo(0);
        assertThat(MathOps.multiplyHighUnsigned(1L << 62, 4)).isEqualTo(1);
        assertThat(MathOps.multiplyHighUnsigned(1L << 62, 8)).isEqualTo(2);
        assertThat(MathOps.multiplyHighUnsigned(1L << 62, 1L << 32)).isEqualTo(1L << 30);
        assertThat(MathOps.multiplyHighUnsigned(1L << 62, 1L << 62)).isEqualTo(1L << 60);

        assertThat(MathOps.multiplyHighUnsigned(Long.MIN_VALUE, 2)).isEqualTo(1);
        assertThat(MathOps.multiplyHighUnsigned(Long.MIN_VALUE, 4)).isEqualTo(2);
        assertThat(MathOps.multiplyHighUnsigned(Long.MIN_VALUE, 8)).isEqualTo(4);

        assertThat(MathOps.multiplyHighUnsigned(-1, 2)).isEqualTo(1);
        assertThat(MathOps.multiplyHighUnsigned(-1, 4)).isEqualTo(3);
        assertThat(MathOps.multiplyHighUnsigned(-1, 8)).isEqualTo(7);
    }

    @Test
    public void multiplyHighUnsigned_ultimate() {
        long[] cases = {
            1, 11, 101, 201, 1001, 12345, -1, -10001, 0xffff, -0xffff,
            0x7fff_ffff, -0x7fff_ffff, 0x1234_ffff_ffffL, -0x1234_ffff_ffffL, Long.MAX_VALUE, -Long.MAX_VALUE
        };
        for (long a : cases) {
            for (long b : cases) {
                assertThat(MathOps.multiplyHighUnsigned(a, b)).isEqualTo(Math.unsignedMultiplyHigh(a, b));
            }
        }
    }

    @Test
    public void udivide128By64_simple() {
        assertThat(MathOps.udivide128By64(0, 12345, 1)).isEqualTo(12345);
        assertThat(MathOps.udivide128By64(0, 12345, 2)).isEqualTo(12345 / 2);
        assertThat(MathOps.udivide128By64(0, 12345, 3)).isEqualTo(12345 / 3);
        assertThat(MathOps.udivide128By64(0, 12345, 5)).isEqualTo(12345 / 5);
        assertThat(MathOps.udivide128By64(0, 12345, 6)).isEqualTo(12345 / 6);
        assertThat(MathOps.udivide128By64(0, 12345, 7)).isEqualTo(12345 / 7);

        assertThat(MathOps.udivide128By64(1, 0, 2)).isEqualTo(1L << 63);
        assertThat(MathOps.udivide128By64(1, 0, 4)).isEqualTo(1L << 62);
        assertThat(MathOps.udivide128By64(1, 0, 8)).isEqualTo(1L << 61);
        assertThat(MathOps.udivide128By64(2, 0, 8)).isEqualTo(1L << 62);
        assertThat(MathOps.udivide128By64(4, 0, 8)).isEqualTo(1L << 63);

        assertThat(MathOps.udivide128By64(1, 16, 8)).isEqualTo((1L << 61) + (16 / 8));
        assertThat(MathOps.udivide128By64(1, 64, 8)).isEqualTo((1L << 61) + (64 / 8));
        assertThat(MathOps.udivide128By64(2, 64, 8)).isEqualTo((1L << 62) + (64 / 8));
        assertThat(MathOps.udivide128By64(4, 16, 8)).isEqualTo((1L << 63) + (16 / 8));
    }

    @Test
    public void udivide128By64_ultimate() {
        long[] highs = { 0, 1, 2, 3, 9, 77, 777, 2025 };
        long[] lows = { 0, 1, 2, 77, 777, 2025, 12345, 12345678 };
        long[] divs = { 3, 5, 7, 256, 1000, 12345, 1 << 16, Integer.MAX_VALUE };

        for (long high : highs) {
            for (long low : lows) {
                for (long div : divs) {
                    BigInteger a = Int128.fromBits(high, low).toBigInteger();
                    BigInteger b = BigInteger.valueOf(div);
                    BigInteger expected = a.divide(b);
                    if (expected.bitLength() > 63) {
                        continue;
                    }
                    long actual = MathOps.udivide128By64(high, low, div);
                    assertThat(actual).isEqualTo(expected.longValueExact());
                }
            }
        }
    }

    @Test
    public void udivide128By64WithRemainder_simple() {
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 1)).asList().containsExactly(12345L, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 2)).asList().containsExactly(12345L / 2, 1L);
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 3)).asList().containsExactly(12345L / 3, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 5)).asList().containsExactly(12345L / 5, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 7)).asList().containsExactly(12345L / 7, 4L);
        assertThat(MathOps.udivide128By64WithRemainder(0, 12345, 9)).asList().containsExactly(12345L / 9, 6L);

        assertThat(MathOps.udivide128By64WithRemainder(1, 0, 2)).asList().containsExactly(1L << 63, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(1, 0, 4)).asList().containsExactly(1L << 62, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(1, 0, 8)).asList().containsExactly(1L << 61, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(1, 1, 8)).asList().containsExactly(1L << 61, 1L);
        assertThat(MathOps.udivide128By64WithRemainder(1, 5, 8)).asList().containsExactly(1L << 61, 5L);
        assertThat(MathOps.udivide128By64WithRemainder(1, 7, 8)).asList().containsExactly(1L << 61, 7L);

        assertThat(MathOps.udivide128By64WithRemainder(2, 0, 8)).asList().containsExactly(1L << 62, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(2, 3, 8)).asList().containsExactly(1L << 62, 3L);
        assertThat(MathOps.udivide128By64WithRemainder(2, 4, 8)).asList().containsExactly(1L << 62, 4L);
        assertThat(MathOps.udivide128By64WithRemainder(2, 6, 8)).asList().containsExactly(1L << 62, 6L);

        assertThat(MathOps.udivide128By64WithRemainder(4, 0, 8)).asList().containsExactly(1L << 63, 0L);
        assertThat(MathOps.udivide128By64WithRemainder(4, 1, 8)).asList().containsExactly(1L << 63, 1L);
        assertThat(MathOps.udivide128By64WithRemainder(4, 2, 8)).asList().containsExactly(1L << 63, 2L);
        assertThat(MathOps.udivide128By64WithRemainder(4, 5, 8)).asList().containsExactly(1L << 63, 5L);
    }

    @Test
    public void udivide128By64WithRemainder_ultimate() {
        long[] highs = { 0, 1, 2, 3, 9, 77, 777, 2025 };
        long[] lows = { 0, 1, 2, 77, 777, 2025, 12345, 12345678 };
        long[] divs = { 3, 5, 7, 256, 1000, 12345, 1 << 16, Integer.MAX_VALUE };

        for (long high : highs) {
            for (long low : lows) {
                for (long div : divs) {
                    BigInteger a = Int128.fromBits(high, low).toBigInteger();
                    BigInteger b = BigInteger.valueOf(div);
                    BigInteger[] expected = a.divideAndRemainder(b);
                    if (expected[0].bitLength() > 63) {
                        continue;
                    }
                    long[] actual = MathOps.udivide128By64WithRemainder(high, low, div);
                    assertThat(actual).asList().containsExactly(expected[0].longValueExact(), expected[1].longValueExact());
                }
            }
        }
    }
}
