package io.spbx.util.time;

import com.carrotsearch.hppc.LongArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

import static io.spbx.util.func.LongPredicates.TRUE;
import static io.spbx.util.func.LongPredicates.chain;

/**
 * Represents the log of real-time timestamps (stored as {@code long}s).
 * Real-time means three conditions:
 * <ul>
 *     <li>Timestamps are all positive</li>
 *     <li>Timestamps are increasing (at least non-decreasing)</li>
 *     <li>Timestamps are always appended to the end of the log</li>
 * </ul>
 * <p>
 * The implementation classes may impose further restrictions, e.g.
 * the timestamps must fit in unsigned 32-bit integer, i.e. be in range {@code [0, 2^32 - 1]}.
 */
public interface RealTimeLog {
    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void append(long value);

    <P extends LongPredicate> @NotNull P iterate(@NotNull P predicate);

    default void iterateAll(@NotNull LongConsumer consumer) {
        iterate(chain(consumer, TRUE));
    }

    default @NotNull LongArrayList toArrayList() {
        LongArrayList result = new LongArrayList(size());
        iterateAll(result::add);
        return result;
    }

    <P extends LongPredicate> @NotNull P reverseIterate(@NotNull P predicate);

    default void reverseIterateAll(@NotNull LongConsumer consumer) {
        reverseIterate(chain(consumer, TRUE));
    }

    default @NotNull LongArrayList toReverseArrayList() {
        LongArrayList result = new LongArrayList(size());
        reverseIterateAll(result::add);
        return result;
    }

    default int countGreaterThan(long threshold) {
        return reverseIterate(new LongPredicate() {
            int counter = 0;
            @Override public boolean test(long value) {
                return value >= threshold && ++counter > 0;
            }
        }).counter;
    }

    default int countInRange(long from, long to) {
        assert from <= to : "Invalid range: from=%s to=%s".formatted(from, to);
        return reverseIterate(new LongPredicate() {
            int counter = 0;
            @Override public boolean test(long value) {
                return value >= from && (value <= to && ++counter > 0 || true);
            }
        }).counter;
    }
}
