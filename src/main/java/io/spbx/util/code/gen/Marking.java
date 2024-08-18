package io.spbx.util.code.gen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface Marking {
    @NotNull String compose(@NotNull Directive directive);

    @Nullable DirectivePosition extract(@NotNull String input);
}
