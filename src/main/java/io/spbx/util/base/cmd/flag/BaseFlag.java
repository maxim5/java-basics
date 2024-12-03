package io.spbx.util.base.cmd.flag;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.LazyArg;
import com.google.common.flogger.LazyArgs;
import io.spbx.util.base.cmd.CommandLineOptions;
import io.spbx.util.base.cmd.CommandLineSpec;
import io.spbx.util.base.error.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.error.BasicExceptions.InternalErrors;
import io.spbx.util.base.lang.Gated;
import io.spbx.util.func.Allowed;
import io.spbx.util.func.Predicates;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.spbx.util.base.error.BasicExceptions.runOnlyInDev;
import static io.spbx.util.base.lang.EasyCast.castAny;

@Immutable
public abstract class BaseFlag<T> implements Supplier<T> {
    private static final Logger log = Logger.forEnclosingClass();

    protected final AtomicBoolean initialized = new AtomicBoolean();
    protected final AtomicReference<@Nullable String> providedValue = new AtomicReference<>();
    protected final FlagOptionData data;
    protected final T defaultValue;

    protected BaseFlag(@NotNull FlagOptionData data, @Nullable T defaultValue) {
        this.data = data;
        this.defaultValue = defaultValue;
    }

    public @Nullable T defaultValue() {
        return defaultValue;
    }

    public @Nullable String providedValue() {
        return providedValue.get();
    }

    void initialize(@NotNull CommandLineOptions options) {
        if (initialized.getAndSet(true)) {
            log.warn().log("Flag %s already initialized", data.key());
            return;
        }
        String value = options.getByKey(data.key());
        InternalErrors.assure(value != null || !data.mandatory(), "Mandatory flag not found in options:", data);
        InternalErrors.assure(value == null || providedValue.compareAndSet(null, value), "Value already provided:", providedValue);
        log.trace().log("Flag %s successfully initialized with value: %s", data.key(), value);
    }

    @NotNull CommandLineSpec.ArgSpec toArgSpec() {
        return data.toArgSpec();
    }

    @Immutable
    protected record FlagOptionData(@NotNull String key,
                                    @NotNull ImmutableList<String> aliases,
                                    @NotNull Predicate<@Nullable String> validator,
                                    boolean mandatory,
                                    @NotNull String help) {
        @NotNull CommandLineSpec.ArgSpec toArgSpec() {
            return new CommandLineSpec.ArgSpec(key, aliases, validator, mandatory, help);
        }
    }

    @ThreadSafe
    public abstract static class Builder<T, F extends BaseFlag<T>, B extends Builder<T, F, ?>> {
        protected final Gated<String> key = Gated.of(null);
        protected final Gated<ImmutableList<String>> aliases = Gated.of(ImmutableList.of());
        protected final Gated<Predicate<@Nullable String>> validator = Gated.of(null);
        protected final Gated<T> defaultValue = Gated.of(null);
        protected final Gated<Boolean> mandatory = Gated.of(null);
        protected final Gated<String> help = Gated.of(null);

        public @NotNull B key(@NotNull String key) {
            this.key.assignGated(key);
            return castAny(this);
        }

        public @NotNull B alias(@NotNull Iterable<String> aliases) {
            this.aliases.assignGated(ImmutableList.copyOf(aliases));
            return castAny(this);
        }

        public @NotNull B alias(@NotNull String @NotNull... aliases) {
            return this.alias(ImmutableList.copyOf(aliases));
        }

        public @NotNull B validator(@Nullable Predicate<@Nullable String> validator) {
            this.validator.assignGated(validator);
            return castAny(this);
        }

        protected @NotNull B and(@Nullable Predicate<@Nullable String> validator) {
            this.validator.update(current -> Predicates.and(current, validator));
            return castAny(this);
        }

        protected @NotNull B or(@Nullable Predicate<@Nullable String> validator) {
            this.validator.update(current -> Predicates.or(current, validator));
            return castAny(this);
        }

        public @NotNull B whitelist(@NotNull String @NotNull... values) {
            return this.validator(Allowed.whitelistOf(values));
        }

        public @NotNull B blacklist(@NotNull String @NotNull... values) {
            return this.validator(Allowed.blacklistOf(values));
        }

        public @NotNull B withoutValidation() {
            return this.validator(null);
        }

        public @NotNull B defaultValue(@Nullable T defaultValue) {
            this.defaultValue.assignGated(defaultValue);
            return castAny(this);
        }

        public @NotNull B mandatory(boolean mandatory) {
            this.mandatory.assignGated(mandatory);
            return castAny(this);
        }

        public @NotNull B mandatory() {
            return this.mandatory(true);
        }

        public @NotNull B optional() {
            return this.mandatory(false);
        }

        public @NotNull F build() {
            FlagOptionData data = new FlagOptionData(
                key.with(key -> IllegalStateExceptions.assureNonNull(key, "Flag key is not provided")),
                aliases.nonNull(),
                validator.nonNullOr(Predicates.alwaysTrue()),
                mandatory.nonNullOr(defaultValue.isNull()),     // mandatory if no default is set
                help.nonNullOr("")
            );
            assert runOnlyInDev(() -> checkFlagDataConsistency(data));
            F flag = create(data);
            Flags.Registry.instance().registerFlag(flag);
            return flag;
        }

        protected void checkFlagDataConsistency(@NotNull FlagOptionData data) {
            String def = valueToString(defaultValue.get());
            if (!data.validator().test(def)) {
                LazyArg<String> flag = LazyArgs.lazy(() -> data.toArgSpec().toHumanDescription());
                if (!data.mandatory()) {
                    log.warn().log("Flag %s validator doesn't allow own default value `%s`", flag, def);
                } else if (def != null) {
                    log.info().log("Flag %s validator doesn't allow own default value `%s` (though is mandatory)", flag, def);
                } else {
                    log.trace().log("Flag %s validator doesn't allow own default value `null` (though is mandatory)", flag);
                }
            }
        }

        protected @Nullable String valueToString(@Nullable T value) {
            return value == null ? null : String.valueOf(value);
        }

        protected abstract @NotNull F create(@NotNull FlagOptionData data);
    }
}
