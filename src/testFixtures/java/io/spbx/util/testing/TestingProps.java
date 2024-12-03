package io.spbx.util.testing;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Stateless
@Pure
@CheckReturnValue
public class TestingProps {
    public static void assumePropIfSet(@NotNull String name, @NotNull String expected) {
        assumePropIfSet(name, expected::equals);
    }

    public static void assumePropIfSet(@NotNull String name, @NotNull Predicate<String> condition) {
        String value = System.getProperty(name);
        assumeTrue(value == null || condition.test(value),
                   "Skipping the test because the property is set and does not match test assumption. " +
                   "Provided prop: %s=`%s`".formatted(name, value));
    }
}
