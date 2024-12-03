package io.spbx.util.extern.guava;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.collect.iter.BasicIterables;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Stateless
@Pure
@CheckReturnValue
public class GuavaImmutables {
    // Helpful for asserting immutability to justify @Immutable annotation
    public static boolean isGuavaImmutable(@NotNull Collection<?> collection) {
        return BasicIterables.isGuavaImmutable(collection);
    }
}
