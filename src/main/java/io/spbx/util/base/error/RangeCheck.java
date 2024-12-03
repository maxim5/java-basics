package io.spbx.util.base.error;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.lang.IntLength;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static io.spbx.util.base.error.BasicExceptions.newInternalError;

/**
 * Negative index translation and Range check.
 *
 * @see io.spbx.util.base.annotate.NegativeIndexingSupported
 */
@Stateless
public class RangeCheck {
    public static final int OPEN_END_RANGE     = 0x00000000;
    public static final int CLOSE_END_RANGE    = 0x00000001;
    public static final int BEFORE_TRANSLATION = 0x00000002;

    public static int translateIndex(@NotNull IntLength val, int i) {
        return LowLevel.translateIndex(i, val.length());
    }

    public static boolean rangeCheck(@NotNull IntLength val, int i, int flags) {
        return LowLevel.rangeCheck(i, val.length(), val, flags);
    }

    public static boolean rangeCheck(@NotNull IntLength val, int i, int j, int flags) {
        return LowLevel.rangeCheck(i, j, val.length(), val, flags);
    }

    public static boolean outOfRangeCheck(@NotNull IntLength val, int def) {
        return LowLevel.outOfRangeCheck(def, val.length(), val);
    }

    public static @NotNull Checker with(int length, @NotNull Supplier<String> toString) {
        return new Checker() {
            @Override public int length() {
                return length;
            }
            @Override public @NotNull String toString() {
                return toString.get();
            }
        };
    }

    public static @NotNull Checker with(int length, @NotNull Object val) {
        return with(length, val::toString);
    }

    @Stateless
    public interface Checker extends IntLength {
        default int translateIndex(int i) {
            return RangeCheck.translateIndex(this, i);
        }

        default boolean rangeCheck(int i, int flags) {
            return RangeCheck.rangeCheck(this, i, flags);
        }

        default boolean rangeCheck(int i, int j, int flags) {
            return RangeCheck.rangeCheck(this, i, j, flags);
        }

        default boolean outOfRangeCheck(int def) {
            return RangeCheck.outOfRangeCheck(this, def);
        }

        @Override @NotNull String toString();
    }

    @Stateless
    public static class LowLevel {
        public static int translateIndex(int i, int len) {
            return i >= 0 ? i : i + len;
        }

        public static boolean rangeCheck(int i, int len, Object val, int flags) {
            switch (flags) {
                case BEFORE_TRANSLATION | OPEN_END_RANGE -> {
                    assert i >= -len && i <  len : "Index %d out of bounds [%d, %d): `%s`".formatted(i, -len, len, val);
                }
                case BEFORE_TRANSLATION | CLOSE_END_RANGE -> {
                    assert i >= -len && i <= len : "Index %d out of bounds [%d, %d]: `%s`".formatted(i, -len, len, val);
                }
                case OPEN_END_RANGE -> {
                    assert i >= 0 && i <  len : "Index %d out of bounds [%d, %d): `%s`".formatted(i, 0, len, val);
                }
                case CLOSE_END_RANGE -> {
                    assert i >= 0 && i <= len : "Index %d out of bounds [%d, %d]: `%s`".formatted(i, 0, len, val);
                }
                default -> throw newInternalError("Range check called with unsupported flags:", flags);
            }
            return true;
        }

        public static boolean rangeCheck(int i, int j, int len, Object val, int flags) {
            switch (flags) {
                case BEFORE_TRANSLATION | OPEN_END_RANGE -> {
                    assert i >= -len && i <  len : "Start index %d out of bounds [%d, %d): `%s`".formatted(i, -len, len, val);
                    assert j >= -len && j <= len : "End index %d out of bounds [%d, %d]: `%s`".formatted(j, -len, len, val);
                    assert translateIndex(i, len) <= translateIndex(j, len) :
                        "Start index can't be larger than end index: %d > %d in `%s`".formatted(i, j, val);
                }
                case BEFORE_TRANSLATION | CLOSE_END_RANGE -> {
                    assert i >= -len && i <= len : "Start index %d out of bounds [%d, %d]: `%s`".formatted(i, -len, len, val);
                    assert j >= -len && j <= len : "End index %d out of bounds [%d, %d]: `%s`".formatted(j, -len, len, val);
                    assert translateIndex(i, len) <= translateIndex(j, len) :
                        "Start index can't be larger than end index: %d > %d in `%s`".formatted(i, j, val);
                }
                case OPEN_END_RANGE -> {
                    assert i >= 0 && i <  len : "Start index %d out of bounds [%d, %d): `%s`".formatted(i, 0, len, val);
                    assert j >= 0 && j <= len : "End index %d out of bounds [%d, %d]: `%s`".formatted(j, 0, len, val);
                    assert i <= j : "Start index can't be larger than end index: %d > %d in `%s`".formatted(i, j, val);
                }
                case CLOSE_END_RANGE -> {
                    assert i >= 0 && i <= len : "Start index %d out of bounds [%d, %d]: `%s`".formatted(i, 0, len, val);
                    assert j >= 0 && j <= len : "End index %d out of bounds [%d, %d]: `%s`".formatted(j, 0, len, val);
                    assert i <= j : "Start index can't be larger than end index: %d > %d in `%s`".formatted(i, j, val);
                }
                default -> throw newInternalError("Range check called with unsupported flags:", flags);
            }
            return true;
        }

        public static boolean outOfRangeCheck(int def, int len, Object val) {
            assert def < 0 || def >= len : "Default index %d must be out of bounds [%d, %d): `%s`".formatted(def, 0, len, val);
            return true;
        }
    }
}
