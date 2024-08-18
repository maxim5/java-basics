package io.spbx.util.extern.guava;

import io.spbx.util.collect.BasicIterables;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GuavaImmutables {
    // Helpful for asserting immutability to justify @Immutable annotation
    public static boolean isGuavaImmutable(@NotNull Collection<?> collection) {
        return BasicIterables.isGuavaImmutable(collection);
    }
}
