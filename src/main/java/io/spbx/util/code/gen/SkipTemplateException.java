package io.spbx.util.code.gen;

import io.spbx.util.base.BasicRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkipTemplateException extends BasicRuntimeException {
    public SkipTemplateException(@NotNull String message) {
        super(message);
    }

    public SkipTemplateException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public SkipTemplateException(@Nullable Throwable cause) {
        super(cause);
    }

    private SkipTemplateException(@NotNull String message, @Nullable Object @NotNull[] args) {
        super(message, args);
    }

    public static @NotNull SkipTemplateException newSkipTemplateException(@NotNull String message,
                                                                          @Nullable Object @NotNull... args) {
        return new SkipTemplateException(message, args);
    }
}
