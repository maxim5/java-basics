package io.spbx.util.collect.stream;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface ToStreamApi<E> {
    @NotNull Stream<E> toStream();
}
