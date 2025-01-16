package io.spbx.util.base.tuple;

import io.spbx.util.collect.container.IntSize;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.Objects;

/**
 * A range interval between two {@code int} bounds.
 */
@Immutable
@Generated(value = "$Type$Range.java", date = "2025-01-14T10:07:33.429110600Z")
public abstract class IntRange implements IntSize, Serializable {
    protected final int left;
    protected final int right;

    protected IntRange(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public static @NotNull IntRange closed(int left, int right) {
        assert left <= right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new ClosedClosed(left, right);
    }

    public static @NotNull IntRange open(int left, int right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new OpenOpen(left, right);
    }

    public static @NotNull IntRange openClosed(int left, int right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new OpenClosed(left, right);
    }

    public static @NotNull IntRange closedOpen(int left, int right) {
        assert left < right : "Invalid range: left=%s right=%s".formatted(left, right);
        return new ClosedOpen(left, right);
    }

    public int leftBound() {
        return left;
    }

    public int rightBound() {
        return right;
    }

    public int intervalLength() {
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

    public abstract boolean contains(int val);

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return false;   // overriden by `OpenOpen` but correct for others
    }

    @Override
    public boolean isNotEmpty() {
        return true;    // overriden by `OpenOpen` but correct for others
    }

    public @NotNull IntPair toPair() {
        return IntPair.of(left, right);
    }

    protected boolean isEqualBounds(IntRange that) {
        return this.left == that.left && this.right == that.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    private static final class ClosedClosed extends IntRange {
        ClosedClosed(int left, int right) { super(left, right); }

        @Override public boolean isLeftOpen() { return false; }
        @Override public boolean isRightOpen() { return false; }
        @Override public boolean contains(int val) { return left <= val && val <= right; }
        @Override public int size() { return right - left + 1; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof ClosedClosed that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "[%s, %s]".formatted(left, right);
        }
    }

    private static final class OpenClosed extends IntRange {
        OpenClosed(int left, int right) { super(left, right); }

        @Override public boolean isLeftOpen() { return true; }
        @Override public boolean isRightOpen() { return false; }
        @Override public boolean contains(int val) { return left < val && val <= right; }
        @Override public int size() { return right - left; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof OpenClosed that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "(%s, %s]".formatted(left, right);
        }
    }

    private static final class ClosedOpen extends IntRange {
        ClosedOpen(int left, int right) { super(left, right); }

        @Override public boolean isLeftOpen() { return false; }
        @Override public boolean isRightOpen() { return true; }
        @Override public boolean contains(int val) { return left <= val && val < right; }
        @Override public int size() { return right - left; }

        @Override public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof ClosedOpen that && this.isEqualBounds(that));
        }
        @Override public String toString() {
            return "[%s, %s)".formatted(left, right);
        }
    }

    private static final class OpenOpen extends IntRange {
        OpenOpen(int left, int right) { super(left, right); }

        @Override public boolean isLeftOpen() { return true; }
        @Override public boolean isRightOpen() { return true; }
        @Override public boolean contains(int val) { return left < val && val < right; }
        @Override public int size() { return right - left - 1; }
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
