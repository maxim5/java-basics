package io.spbx.util.code.gen;

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
record DirectivePosition(@NotNull Directive directive, int start, int end) {}
