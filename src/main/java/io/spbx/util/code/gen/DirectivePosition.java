package io.spbx.util.code.gen;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

@Immutable
record DirectivePosition(@NotNull Directive directive, int start, int end) {}
