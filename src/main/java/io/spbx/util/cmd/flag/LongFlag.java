package io.spbx.util.cmd.flag;

import io.spbx.util.base.ops.LongOps;
import io.spbx.util.base.str.BasicParsing;
import io.spbx.util.base.str.BasicStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;

@Immutable
public class LongFlag extends DefaultFlag<Long> implements LongSupplier {
    protected LongFlag(@NotNull FlagOptionData data, @Nullable Long defaultValue) {
        super(data, defaultValue);
    }

    public static @NotNull Builder of() {
        return new Builder();
    }

    public static @NotNull Builder defaultValue(long defaultValue) {
        return new Builder().defaultValue(defaultValue);
    }

    @Override
    public long getAsLong() {
        return get();
    }

    @Override
    protected @Nullable Long convertFrom(@NotNull String value) {
        return BasicParsing.parseLongOrNull(value, defaultValue);
    }

    @ThreadSafe
    public static class Builder extends NonNullBuilder<Long, LongFlag, Builder> {
        {
            this.validator.reinit(BasicStrings::isNotEmpty);
        }

        public @NotNull Builder matching(@NotNull LongPredicate validator) {
            return and(val -> validator.test(BasicParsing.parseLong(val)));
        }

        public @NotNull Builder positive() {
            return matching(val -> val > 0);
        }

        public @NotNull Builder nonNegative() {
            return matching(val -> val >= 0);
        }

        public @NotNull Builder min(long min) {
            return matching(val -> val >= min);
        }

        public @NotNull Builder max(long max) {
            return matching(val -> val <= max);
        }

        public @NotNull Builder inRange(long from, long to) {
            return matching(val -> val >= from && val <= to);
        }

        public @NotNull Builder whitelist(long... values) {
            return matching(val -> LongOps.contains(values, val));
        }

        public @NotNull Builder blacklist(long... values) {
            return matching(val -> !LongOps.contains(values, val));
        }

        @Override protected @NotNull LongFlag create(@NotNull FlagOptionData data) {
            return new LongFlag(data, defaultValue.get());
        }
    }
}
