package io.spbx.util.errors;

/**
 * Negative index translation and Range check.
 *
 * @see io.spbx.util.annotate.NegativeIndexingSupported
 */
public class RangeCheck {
    protected static final int OPEN_END_RANGE     = 0x00000000;
    protected static final int CLOSE_END_RANGE    = 0x00000001;
    protected static final int BEFORE_TRANSLATION = 0x00000002;

    protected static int translateIndex(int i, int length) {
        return i >= 0 ? i : i + length;
    }

    protected static boolean rangeCheck(int i, int len, Object val, int flags) {
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
            default -> throw new InternalError("Range check called with unsupported flags: " + flags);
        }
        return true;
    }

    protected static boolean rangeCheck(int i, int j, int len, Object val, int flags) {
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
            default -> throw new InternalError("Range check called with unsupported flags: " + flags);
        }
        return true;
    }

    protected static boolean outOfRangeCheck(int def, int len, Object val) {
        assert def < 0 || def >= len : "Default index %d must be out of bounds [%d, %d): `%s`".formatted(def, 0, len, val);
        return true;
    }
}
