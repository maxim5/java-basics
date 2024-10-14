package io.spbx.util.testing;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.func.Reversible;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;

public class AssertReverse {
    @CanIgnoreReturnValue
    public static <U, V> @NotNull V assertRoundtrip(@NotNull Reversible<U, V> reversible, @NotNull U input) {
        V forward = assertOneWayRoundtrip(reversible, input);
        assertOneWayRoundtrip(reversible.reverse(), forward);
        assertThat(reversible.reverse().reverse()).isSameInstanceAs(reversible);
        return forward;
    }

    @CanIgnoreReturnValue
    public static <U, V> @NotNull V assertRoundtrip(@NotNull Function<U, V> forwardFunc,
                                                    @NotNull Function<V, U> backwardFunc,
                                                    @NotNull U input) {
        V forward = assertOneWayRoundtrip(forwardFunc, backwardFunc, input);
        assertOneWayRoundtrip(backwardFunc, forwardFunc, forward);
        return forward;
    }

    @CanIgnoreReturnValue
    public static @SafeVarargs <U, V> void assertRoundtrip(@NotNull Reversible<U, V> rev,
                                                           @NotNull U @NotNull... inputs) {
        for (U input : inputs) {
            assertRoundtrip(rev, input);
        }
    }

    public static <U, V> @NotNull V assertOneWayRoundtrip(@NotNull Reversible<U, V> reversible, @NotNull U input) {
        V forward = reversible.forward(input);
        U backward = reversible.backward(forward);
        assertThat(backward).isEqualTo(input);
        return forward;
    }

    public static <U, V> @NotNull V assertOneWayRoundtrip(@NotNull Function<U, V> forwardFunc,
                                                          @NotNull Function<V, U> backwardFunc,
                                                          @NotNull U input) {
        V forward = forwardFunc.apply(input);
        U backward = backwardFunc.apply(forward);
        assertThat(backward).isEqualTo(input);
        return forward;
    }
}
