package io.spbx.util.collect;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface ToStreamApi<E> {
    @NotNull Stream<E> toStream();
}
