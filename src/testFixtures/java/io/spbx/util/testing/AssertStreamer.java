package io.spbx.util.testing;

import com.google.common.truth.Ordered;
import io.spbx.util.collect.ToStreamApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.truth.Truth.assertThat;

public class AssertStreamer {
    public static <E> @NotNull ToStreamApiSubject<E> assertStream(@NotNull ToStreamApi<E> streamApi) {
        return new ToStreamApiSubject<>(streamApi);
    }

    public record ToStreamApiSubject<E>(@NotNull ToStreamApi<E> streamApi) {
        public void isEmpty() {
            assertThat(streamApi.toStream()).isEmpty();
        }

        public @SafeVarargs final @NotNull Ordered containsExactly(@Nullable E @Nullable ... expected) {
            return assertThat(streamApi.toStream()).containsExactly((Object[]) expected);
        }

        public @SafeVarargs final void containsExactlyInOrder(@Nullable E @Nullable ... expected) {
            containsExactly(expected).inOrder();
        }

        public @NotNull Ordered containsExactlyElementsIn(@NotNull Iterable<?> expected) {
            return assertThat(streamApi.toStream()).containsExactlyElementsIn(expected);
        }

        public void containsExactlyElementsInOrder(@NotNull Iterable<?> expected) {
            containsExactlyElementsIn(expected).inOrder();
        }
    }
}
