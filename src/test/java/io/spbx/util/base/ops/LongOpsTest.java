package io.spbx.util.base.ops;

import com.google.common.primitives.Longs;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.LongPredicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertReverse.assertRoundtrip;
import static io.spbx.util.testing.TestingPrimitives.ints;
import static io.spbx.util.testing.TestingPrimitives.longs;

@Tag("fast")
public class LongOpsTest {
    /** {@link LongOps#map} **/

    @Test
    public void map_binary_simple() {
        assertThat(LongOps.map(longs(), (a, i) -> a * i)).asList().isEmpty();
        assertThat(LongOps.map(longs(1), (a, i) -> a * i)).asList().containsExactly(0L);
        assertThat(LongOps.map(longs(1, 2, 3), (a, i) -> a * i)).asList().containsExactly(0L, 2L, 6L);
    }

    /** {@link LongOps#mapInPlace} **/

    @Test
    public void mapInPlace_binary_simple() {
        assertThat(LongOps.mapInPlace(longs(), (a, i) -> a * i)).asList().isEmpty();
        assertThat(LongOps.mapInPlace(longs(1), (a, i) -> a * i)).asList().containsExactly(0L);
        assertThat(LongOps.mapInPlace(longs(1, 2, 3), (a, i) -> a * i)).asList().containsExactly(0L, 2L, 6L);
    }

    /** {@link LongOps#filter} **/

    @Test
    public void filter_binary_simple() {
        assertThat(LongOps.filter(longs(), (a, i) -> a > i)).asList().isEmpty();
        assertThat(LongOps.filter(longs(0), (a, i) -> a > i)).asList().isEmpty();
        assertThat(LongOps.filter(longs(1), (a, i) -> a > i)).asList().containsExactly(1L);
        assertThat(LongOps.filter(longs(1, 1), (a, i) -> a > i)).asList().containsExactly(1L);
        assertThat(LongOps.filter(longs(1, 2), (a, i) -> a > i)).asList().containsExactly(1L, 2L);
    }

    /** {@link LongOps#filterInPlace} **/
    
    @Test
    public void filterInPlace_binary_simple() {
        also(longs(), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(LongOps.realloc(array, n)).asList().isEmpty();
        });
        also(longs(0), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(LongOps.realloc(array, n)).asList().isEmpty();
        });
        also(longs(0, 1), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(LongOps.realloc(array, n)).asList().isEmpty();
        });
        also(longs(2), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(1);
            assertThat(LongOps.realloc(array, n)).asList().containsExactly(2L);
        });
        also(longs(0, 2), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(1);
            assertThat(LongOps.realloc(array, n)).asList().containsExactly(2L);
        });
        also(longs(1, 0, 3, 2), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(2);
            assertThat(LongOps.realloc(array, n)).asList().containsExactly(1L, 3L);
        });
        also(longs(0, 1, 3, 4, 1, 6), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(3);
            assertThat(LongOps.realloc(array, n)).asList().containsExactly(3L, 4L, 6L);
        });
        also(longs(1, 2, 0, 4, 0, 6, 0, 0), array -> {
            int n = LongOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(4);
            assertThat(LongOps.realloc(array, n)).asList().containsExactly(1L, 2L, 4L, 6L);
        });
    }
    
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
