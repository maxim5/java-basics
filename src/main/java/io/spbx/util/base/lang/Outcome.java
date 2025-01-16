package io.spbx.util.base.lang;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Immutable
public abstract class Outcome<T> implements Supplier<Optional<T>> {
    public static <T> @NotNull Outcome<T> success(@NotNull T value) {
        return new SuccessOutcome<>(value);
    }

    public static <T> @NotNull Outcome<T> failure(@NotNull Throwable error) {
        return new FailureOutcome<>(error);
    }

    public static <T> @NotNull Outcome<T> failure(@NotNull String error) {
        return new ErrorMessageOutcome<>(error);
    }

    public abstract boolean isOK();

    public boolean isError() {
        return !isOK();
    }

    public boolean hasException() {
        return isError();
    }

    @Override
    public @NotNull Optional<T> get() {
        return isOK() ? Optional.ofNullable(value()) : Optional.empty();
    }

    public @Nullable T valueOrDefault(@Nullable T def) {
        return isOK() ? value() : def;
    }

    public @NotNull T successValue() {
        assert isOK() : "Outcome is not successful";
        return Objects.requireNonNull(value());
    }

    public abstract @Nullable T value();

    public abstract @Nullable Throwable error();

    public abstract @Nullable String errorMessage();

    private static class SuccessOutcome<T> extends Outcome<T> {
        private final T successValue;

        SuccessOutcome(@NotNull T successValue) {
            this.successValue = successValue;
        }

        @Override public boolean isOK() {
            return true;
        }

        @Override public boolean isError() {
            return false;
        }

        @Override public Throwable error() {
            return null;
        }

        @Override public String errorMessage() {
            return null;
        }

        @Override public T value() {
            return successValue;
        }
    }

    private static class FailureOutcome<T> extends Outcome<T> {
        private final Throwable error;

        FailureOutcome(@NotNull Throwable error) {
            this.error = error;
        }

        @Override public boolean isOK() {
            return false;
        }

        @Override public boolean isError() {
            return true;
        }

        @Override public T value() {
            return null;
        }

        @Override public @NotNull Throwable error() {
            return error;
        }

        @Override public String errorMessage() {
            return error.getMessage();
        }
    }

    private static class ErrorMessageOutcome<T> extends Outcome<T> {
        private final String error;

        ErrorMessageOutcome(@NotNull String error) {
            this.error = error;
        }

        @Override public boolean isOK() {
            return false;
        }

        @Override public boolean isError() {
            return true;
        }

        @Override
        public boolean hasException() {
            return false;
        }

        @Override public T value() {
            return null;
        }

        @Override public Throwable error() {
            return null;
        }

        @Override public @NotNull String errorMessage() {
            return error;
        }
    }
}
