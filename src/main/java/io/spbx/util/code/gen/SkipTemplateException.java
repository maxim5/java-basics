package io.spbx.util.code.gen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkipTemplateException extends RuntimeException {
    protected SkipTemplateException() {
    }

    public SkipTemplateException(@NotNull String message) {
        super(message);
    }

    public SkipTemplateException(@NotNull String message, @Nullable Object @NotNull... args) {
        super(message.formatted(args));
    }

    public SkipTemplateException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public SkipTemplateException(@Nullable Throwable cause) {
        super(cause);
    }
}
