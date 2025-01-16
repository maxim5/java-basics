package io.spbx.util.base.tuple;

import io.spbx.util.collect.container.LongSize;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.Objects;

/**
 * A range interval between two {@code long} bounds.
 */
@Immutable
@Generated(value = "$Type$Range.java", date = "2025-01-14T10:07:33.429110600Z")
public abstract class LongRange implements LongSize, Serializable {
    protected final long left;
    protected final long right;

    protected LongRange(long left, long right) {
        this.left = left;
        this.right = right;
    }

    public static @NotNull LongRange closed(long left, long right) {
        assert left <= right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new ClosedClosed(left, right);
    }

    public static @NotNull LongRange open(long left, long right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new OpenOpen(left, right);
    }

    public static @NotNull LongRange openClosed(long left, long right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new OpenClosed(left, right);
    }

    public static @NotNull LongRange closedOpen(long left, long right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new ClosedOpen(left, right);
    }

    public long leftBound() {
        return left;
    }

    public long rightBound() {
        return right;
    }

    public long intervalLength() {
        return right - left;
    }

    public abstract boolean isLeftOpen();

    public abstract boolean isRightOpen();

    public boolean isLeftClosed() {
        return !isLeftOpen();
    }

    public boolean isRightClosed() {
        return !isRightOpen();
    }

    public abstract boolean contains(long val);

    @Override
    public abstract long size();

    @Override
    public boolean isEmpty() {
        return false;   // overriden by `OpenOpen` but correct for others
    }

    @Override
    public boolean isNotEmpty() {
        return true;    // overriden by `OpenOpen` but correct for others
    }

    public @NotNull LongPair toPair() {
        return LongPair.of(left, right);
    }

    protected boolean isEqualBounds(LongRange that) {
        return this.left == that.left && this.right == that.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    private static final class ClosedClosed extends LongRange {
        ClosedClosed(long left, long right) { super(left, right); }

        @Override public boolean isLeftOpen() { return false; }
        @Override public boolean isRightOpen() { return false; }
        @Override public boolean contains(long val) { return left <= val && val <= right; }
        @Override public long size() { return right - left + 1; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof ClosedClosed that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "[%s, %s]".formatted(left, right);
        }
    }

    private static final class OpenClosed extends LongRange {
        OpenClosed(long left, long right) { super(left, right); }

        @Override public boolean isLeftOpen() { return true; }
        @Override public boolean isRightOpen() { return false; }
        @Override public boolean contains(long val) { return left < val && val <= right; }
        @Override public long size() { return right - left; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof OpenClosed that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "(%s, %s]".formatted(left, right);
        }
    }

    private static final class ClosedOpen extends LongRange {
        ClosedOpen(long left, long right) { super(left, right); }

        @Override public boolean isLeftOpen() { return false; }
        @Override public boolean isRightOpen() { return true; }
        @Override public boolean contains(long val) { return left <= val && val < right; }
        @Override public long size() { return right - left; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof ClosedOpen that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "[%s, %s)".formatted(left, right);
        }
    }

    private static final class OpenOpen extends LongRange {
        OpenOpen(long left, long right) { super(left, right); }

        @Override public boolean isLeftOpen() { return true; }
        @Override public boolean isRightOpen() { return true; }
        @Override public boolean contains(long val) { return left < val && val < right; }
        @Override public long size() { return right - left - 1; }
        @Override public boolean isEmpty() { return left == right - 1; }
        @Override public boolean isNotEmpty() { return left != right - 1; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof OpenOpen that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "(%s, %s)".formatted(left, right);
        }
    }
}
