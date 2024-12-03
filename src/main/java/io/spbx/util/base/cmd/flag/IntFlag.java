package io.spbx.util.base.cmd.flag;

import io.spbx.util.base.ops.IntOps;
import io.spbx.util.base.str.BasicParsing;
import io.spbx.util.base.str.BasicStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

@Immutable
public class IntFlag extends DefaultFlag<Integer> implements IntSupplier {
    protected IntFlag(@NotNull FlagOptionData data, @Nullable Integer defaultValue) {
        super(data, defaultValue);
    }

    public static @NotNull Builder of() {
        return new Builder();
    }

    public static @NotNull Builder defaultValue(int defaultValue) {
        return new Builder().defaultValue(defaultValue);
    }

    @Override
    public int getAsInt() {
        return get();
    }

    @Override
    protected @Nullable Integer convertFrom(@NotNull String value) {
        return BasicParsing.parseIntegerOrNull(value, defaultValue);
    }

    @ThreadSafe
    public static class Builder extends NonNullBuilder<Integer, IntFlag, Builder> {
        {
            this.validator.reinit(BasicStrings::isNotEmpty);
        }

        public @NotNull Builder matching(@NotNull IntPredicate validator) {
            return and(val -> validator.test(BasicParsing.parseInt(val)));
        }

        public @NotNull Builder positive() {
            return matching(val -> val > 0);
        }

        public @NotNull Builder nonNegative() {
            return matching(val -> val >= 0);
        }

        public @NotNull Builder min(int min) {
            return matching(val -> val >= min);
        }

        public @NotNull Builder max(int max) {
            return matching(val -> val <= max);
        }

        public @NotNull Builder inRange(int from, int to) {
            return matching(val -> val >= from && val <= to);
        }

        public @NotNull Builder whitelist(int... values) {
            return matching(val -> IntOps.contains(values, val));
        }

        public @NotNull Builder blacklist(int... values) {
            return matching(val -> !IntOps.contains(values, val));
        }

        @Override protected @NotNull IntFlag create(@NotNull FlagOptionData data) {
            return new IntFlag(data, defaultValue.get());
        }
    }
}
