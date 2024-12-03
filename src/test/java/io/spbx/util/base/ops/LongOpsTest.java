package io.spbx.util.base.ops;

import com.google.common.primitives.Longs;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertReverse.assertRoundtrip;

@Tag("fast")
public class LongOpsTest {
    /** {@link LongOps#toBigEndianBytes} {@link LongOps#valueOfBigEndianBytes} **/

    private static final long[] EDGE_CASE_LONGS = {
        0, 1, -1, 1 << 15, -1 << 15, 1L << 31, -1L << 31, 1L << 63, -1L << 63, Long.MIN_VALUE, Long.MAX_VALUE
    };

    @Test
    public void toBigEndianBytes_ultimate() {
        for (long value : EDGE_CASE_LONGS) {
            byte[] expected = Longs.toByteArray(value);
            assertThat(LongOps.toBigEndianBytes(new long[] { value })).isEqualTo(expected);
            assertThat(LongOps.toBigEndianBytes(value)).isEqualTo(expected);
            assertThat(LongOps.toBigEndianBytes(value, new byte[8])).isEqualTo(expected);
            assertThat(LongOps.toBigEndianBytes(value, new byte[8], 0)).isEqualTo(expected);
            assertThat(LongOps.toBigEndianBytes(value, new byte[9], 0)).isEqualTo(ByteOps.append(expected, (byte) 0));
            assertThat(LongOps.toBigEndianBytes(value, new byte[9], 1)).isEqualTo(ByteOps.prepend((byte) 0, expected));
        }
    }

    @Test
    public void valueOfBigEndianBytes_ultimate() {
        for (long value : EDGE_CASE_LONGS) {
            byte[] bytes = Longs.toByteArray(value);
            assertThat(LongOps.valueOfBigEndianBytes(bytes)).isEqualTo(value);
            assertThat(LongOps.valueOfBigEndianBytes(bytes, 0)).isEqualTo(value);
            assertThat(LongOps.valueOfBigEndianBytes(ByteOps.append(bytes, (byte) 77), 0)).isEqualTo(value);
            assertThat(LongOps.valueOfBigEndianBytes(ByteOps.prepend((byte) 77, bytes), 1)).isEqualTo(value);
            assertThat(LongOps.valueOfBigEndianBytes(bytes[0], bytes[1], bytes[2], bytes[3],
                                                     bytes[4], bytes[5], bytes[6], bytes[7])).isEqualTo(value);
            assertThat(LongOps.fromBigEndianBytes(bytes)).asList().containsExactly(value);
        }
    }

    @Test
    public void toBigEndianBytes_valueOfBigEndianBytes_roundtrip_ultimate() {
        for (long value : EDGE_CASE_LONGS) {
            assertRoundtrip(LongOps::toBigEndianBytes, LongOps::valueOfBigEndianBytes, value);
        }
    }

    /** {@link LongOps#avg(long, long)} **/

    @Test
    public void avg_simple() {
        assertThat(LongOps.avg(0, 1)).isEqualTo(0);
        assertThat(LongOps.avg(0, 2)).isEqualTo(1);
        assertThat(LongOps.avg(1, 2)).isEqualTo(1);
        assertThat(LongOps.avg(1, 3)).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(longs = { 1L << 60, Long.MAX_VALUE, Long.MAX_VALUE - 1 })
    public void avg_edge_cases_positive(long val) {
        assertThat(LongOps.avg(val, val)).isEqualTo(val);
        assertThat(LongOps.avg(val - 1, val - 1)).isEqualTo(val - 1);
        assertThat(LongOps.avg(val - 1, val)).isEqualTo(val - 1);
        assertThat(LongOps.avg(val - 2, val)).isEqualTo(val - 1);
        assertThat(LongOps.avg(0, val)).isEqualTo(val >> 1);
        assertThat(LongOps.avg(-val, val)).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(longs = { -1L << 60, Long.MIN_VALUE + 1 })
    public void avg_edge_cases_negative(long val) {
        assertThat(LongOps.avg(val, val)).isEqualTo(val);
        assertThat(LongOps.avg(val + 1, val + 1)).isEqualTo(val + 1);
        assertThat(LongOps.avg(val + 1, val)).isEqualTo(val);
        assertThat(LongOps.avg(val + 2, val)).isEqualTo(val + 1);
        assertThat(LongOps.avg(-val, val)).isEqualTo(0);
    }
}
