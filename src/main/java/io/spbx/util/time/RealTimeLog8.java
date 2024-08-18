package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.function.LongPredicate;

import static io.spbx.util.base.BasicExceptions.newInternalError;

/**
 * Stores the {@link RealTimeLog} in 8-bit byte array.
 * <p>
 * Supports only unsigned 32-bit timestamp values, i.e. longs in range <code>[0, 2^32 - 1]</code>.
 * <p>
 * Most efficient when the timestamps are close. For example, if the average difference between consecutive
 * timestamps is <code>timestampFreqAvg</code>, the expected memory footprint of {@link RealTimeLog8} to store
 * <code>100_000</code> timestamps is:
 * <ul>
 *     <li>
 *         <code>timestampFreqAvg <= 100</code> and <code>blockSize = 4</code>:
 *         <code>256 Kb</code> (or <code>2.6</code> bytes per value)
 *     </li>
 *     <li>
 *         <code>timestampFreqAvg <= 45</code> and <code>blockSize = 8</code>:
 *         <code>192 Kb</code> (or <code>2.0</code> bytes per value)
 *     </li>
 *     <li>
 *         <code>timestampFreqAvg <= 20</code> and <code>blockSize = 16</code>:
 *         <code>160 Kb</code> (or <code>1.6</code> bytes per value)
 *     </li>
 *     <li>
 *         <code>timestampFreqAvg <= 10</code> and <code>blockSize = 32</code>:
 *         <code>144 Kb</code> (or <code>1.5</code> bytes per value)
 *     </li>
 * </ul>
 */
public class RealTimeLog8 implements RealTimeLog {
    private static final int DEFAULT_BLOCK_SIZE = 8;
    private static final long MAX_UINT32 = (1L << 32) - 1;

    private final int blockSize;
    private final int blockLog2;
    private final int blockMask;
    private byte[] history;
    private int[] spans;
    private long spanValue;
    private int pos;
    private int spanIndex;
    private int size;

    private RealTimeLog8(int blockSize) {
        assert blockSize > 0 : "Must be positive: " + blockSize;
        assert isPowerOfTwo(blockSize) : "Not a power of 2: " + blockSize;
        this.blockSize = blockSize;
        this.blockLog2 = 31 - Integer.numberOfLeadingZeros(blockSize);
        this.blockMask = blockSize - 1;
        this.history = new byte[blockSize * 2];
        this.spans = new int[8];
        this.spanValue = -1;
        this.pos = 0;
        this.spanIndex = -1;
        this.size = 0;
        assert this.blockSize == 1 << this.blockLog2 : newInternalError("Invalid block size: " + blockSize);
    }

    public static @NotNull RealTimeLog8 allocate(int blockSize) {
        return new RealTimeLog8(blockSize);
    }

    public static @NotNull RealTimeLog8 allocate() {
        return RealTimeLog8.allocate(DEFAULT_BLOCK_SIZE);
    }

    @Override
    public int size() {
        return size;
    }

    public int historyCapacity() {
        return history.length;
    }

    public int spansCapacity() {
        return spans.length;
    }

    @Override
    public void append(long value) {
        assert value >= 0 : "Value must be non-negative: " + value;
        assert value <= MAX_UINT32 : "Value is too large: " + value;
        append((int) value);
    }

    public void append(int unsignedIntValue) {
        long value = Integer.toUnsignedLong(unsignedIntValue);
        size++;

        if (spanValue == -1) {
            setNewSpan(unsignedIntValue, value);
            pos = 1;  // history[0] is already 0, which is the stored diff
            return;
        }

        long diff = value - spanValue;
        assert diff >= 0 : "Values may only go forward: current-span=%s, value=%s".formatted(spanValue, value);

        if (diff >= 255 || (pos & blockMask) == 0) {
            setNewSpan(unsignedIntValue, value);
            pos = fillUpBlock() + 1;    // history[pos] is already 0, which is the stored diff
            growHistoryIfNecessary();
        } else {
            history[pos] = (byte) diff;
            pos++;
            growHistoryIfNecessary();
        }
    }

    @Override
    public <P extends LongPredicate> @NotNull P iterate(@NotNull P predicate) {
        long span = -1;
        for (int i = 0, j = 0; i < pos; i++) {
            if ((i & blockMask) == 0) {
                span = Integer.toUnsignedLong(spans[j++]);
            }
            byte stored = history[i];
            if (stored != -1 && !predicate.test(span + Byte.toUnsignedInt(stored))) {
                return predicate;
            }
        }
        return predicate;
    }

    @Override
    public <P extends LongPredicate> @NotNull P reverseIterate(@NotNull P predicate) {
        long span = spanValue;
        for (int i = pos - 1, j = spanIndex - 1; i >= 0; i--) {
            byte stored = history[i];
            if (stored != -1 && !predicate.test(span + Byte.toUnsignedInt(stored))) {
                return predicate;
            }
            if ((i & blockMask) == 0 && i > 0) {
                span = Integer.toUnsignedLong(spans[j--]);
            }
        }
        return predicate;
    }

    @Override
    public String toString() {
        return "RealTimeLog8: " + toArrayList();
    }

    private int fillUpBlock() {
        int nextPos = nextBlockStart(pos - 1);
        Arrays.fill(history, pos, nextPos, (byte) -1);
        return nextPos;
    }

    private void setNewSpan(int unsignedIntValue, long value) {
        assert Integer.toUnsignedLong(unsignedIntValue) == value : newInternalError("Invalid newSpan() args");
        growSpansIfNecessary();
        spanIndex++;
        spans[spanIndex] = unsignedIntValue;
        spanValue = value;
        // Invariant: `spanValue` is the cached `spans[spanIndex]`.
        assert spans[spanIndex] == (int) spanValue : newInternalError("Invariant is broken");
    }

    private void growHistoryIfNecessary() {
        if (pos >= history.length) {
            history = Arrays.copyOf(history, history.length * 2);
        }
    }

    private void growSpansIfNecessary() {
        if (spanIndex >= spans.length - 1) {
            spans = Arrays.copyOf(spans, spans.length * 2);
        }
    }

    @VisibleForTesting
    byte[] internalHistory() {
        return history;
    }

    @VisibleForTesting
    int[] internalSpans() {
        return spans;
    }

    @VisibleForTesting
    int nextBlockStart(int pos) {
        return blockSize + ((pos >> blockLog2) << blockLog2);
    }

    @VisibleForTesting
    static boolean isPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }
}
