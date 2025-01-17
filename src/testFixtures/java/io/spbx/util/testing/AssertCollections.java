package io.spbx.util.testing;

import com.google.common.truth.Truth;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Stateless
public class AssertCollections {
    @CheckReturnValue
    public static <T> @NotNull CollectionSubject<T> assertThat(@NotNull Collection<T> collection) {
        return new CollectionSubject<>(collection);
    }

    @CanIgnoreReturnValue
    public record CollectionSubject<T>(@NotNull Collection<T> collection) {
        public void isEqualTo(@NotNull Collection<T> expected) {
            Truth.assertThat(collection).isEqualTo(expected);
        }

        public void isEqualToExactly(@NotNull Collection<T> expected) {
            Truth.assertThat(collection).isEqualTo(expected);
            Truth.assertThat(collection.getClass()).isEqualTo(expected.getClass());
        }
    }
}
