package io.spbx.util.base.cmd.flag;

import io.spbx.util.base.cmd.CommandLineOptions;
import io.spbx.util.base.error.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.error.BasicExceptions.InternalErrors;
import io.spbx.util.func.Predicates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static io.spbx.util.base.lang.EasyCast.castAny;

@Immutable
public abstract class DefaultFlag<T> extends BaseFlag<T> {
    private final AtomicReference<T> value = new AtomicReference<>();   // lazy nullable

    protected DefaultFlag(@NotNull FlagOptionData data,
                          @Nullable T defaultValue) {
        super(data, defaultValue);
    }

    @Override
    public T get() {
        IllegalStateExceptions.assure(initialized.get(), "Flag is not initialized yet:", this);
        return value.get();
    }

    @Override
    void initialize(@NotNull CommandLineOptions options) {
        super.initialize(options);
        InternalErrors.assure(value.compareAndSet(null, computeValue()), "Flag is already initialized:", this);
    }

    private @Nullable T computeValue() {
        String value = providedValue.get();
        if (value != null) {
            return convertFrom(value);
        }
        return defaultValue;
    }

    protected abstract @Nullable T convertFrom(@NotNull String value);

    @ThreadSafe
    public abstract static class NonNullBuilder<T,
                                                F extends BaseFlag<T>,
                                                B extends NonNullBuilder<T, F, ?>> extends DefaultFlag.Builder<T, F, B> {
        {
            this.validator.reinit(Predicates.isNonNull());
        }

        @Override public @NotNull B validator(@Nullable Predicate<@NotNull String> validator) {
            this.validator.assignGated(validator);
            return castAny(this);
        }

        @Override protected @NotNull B and(@Nullable Predicate<@NotNull String> validator) {
            this.validator.update(current -> Predicates.and(current, validator));
            return castAny(this);
        }

        @Override protected @NotNull B or(@Nullable Predicate<@NotNull String> validator) {
            this.validator.update(current -> Predicates.or(current, validator));
            return castAny(this);
        }
    }
}
