package io.spbx.util.cmd.flag;

import io.spbx.util.base.str.BasicParsing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.BooleanSupplier;

@Immutable
public class BooleanFlag extends DefaultFlag<Boolean> implements BooleanSupplier {
    protected BooleanFlag(@NotNull FlagOptionData data, @Nullable Boolean defaultValue) {
        super(data, defaultValue);
    }

    public static @NotNull Builder of() {
        return new Builder();
    }

    public static @NotNull Builder defaultValue(boolean defaultValue) {
        return new Builder().defaultValue(defaultValue);
    }

    @Override
    public boolean getAsBoolean() {
        return get();
    }

    @Override
    protected @Nullable Boolean convertFrom(@NotNull String value) {
        return value.isEmpty() || BasicParsing.isTrue(value);
    }

    @ThreadSafe
    public static class Builder extends BaseFlag.Builder<Boolean, BooleanFlag, Builder> {
        {
            this.defaultValue.reinit(false);
            this.validator.reinit(val -> val == null || BasicParsing.isTrue(val) || BasicParsing.isFalse(val));
        }

        @Override protected @NotNull BooleanFlag create(@NotNull FlagOptionData data) {
            return new BooleanFlag(data, defaultValue.get());
        }
    }
}
