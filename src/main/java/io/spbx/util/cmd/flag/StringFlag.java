package io.spbx.util.cmd.flag;

import io.spbx.util.base.str.BasicStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.regex.Pattern;

@Immutable
public class StringFlag extends DefaultFlag<String> {
    protected StringFlag(@NotNull FlagOptionData data, @Nullable String defaultValue) {
        super(data, defaultValue);
    }

    public static @NotNull Builder of() {
        return new Builder();
    }

    public static @NotNull Builder defaultValue(@NotNull String defaultValue) {
        return new Builder().defaultValue(defaultValue);
    }

    @Override
    protected @NotNull String convertFrom(@NotNull String value) {
        return value;
    }

    @ThreadSafe
    public static class Builder extends NonNullBuilder<String, StringFlag, Builder> {
        public @NotNull Builder notEmpty() {
            return and(BasicStrings::isNotEmpty);
        }

        public @NotNull Builder length(int len) {
            return and(val -> val.length() == len);
        }

        public @NotNull Builder minLength(int len) {
            return and(val -> val.length() >= len);
        }

        public @NotNull Builder maxLength(int len) {
            return and(val -> val.length() <= len);
        }

        public @NotNull Builder lengthBetween(int from, int to) {
            return and(val -> val.length() >= from && val.length() <= to);
        }

        public @NotNull Builder matching(@NotNull Pattern pattern) {
            return and(val -> pattern.matcher(val).matches());
        }

        @Override protected @NotNull StringFlag create(@NotNull FlagOptionData data) {
            return new StringFlag(data, defaultValue.get());
        }
    }
}
