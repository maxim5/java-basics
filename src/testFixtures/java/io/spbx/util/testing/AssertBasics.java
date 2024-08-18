package io.spbx.util.testing;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.base.Unchecked;
import io.spbx.util.func.Reversible;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;

public class AssertBasics {
    @CanIgnoreReturnValue
    public static <U, V> @NotNull U assertReversibleRoundtrip(@NotNull Reversible<U, V> reversible, @NotNull U input) {
        V forward = reversible.forward(input);
        U backward = reversible.backward(forward);
        assertThat(backward).isEqualTo(input);
        return backward;
    }

    @CanIgnoreReturnValue
    public static <U, V> @NotNull U assertReversibleRoundtrip(@NotNull Function<U, V> forwardFunc,
                                                              @NotNull Function<V, U> backwardFunc,
                                                              @NotNull U input) {
        V forward = forwardFunc.apply(input);
        U backward = backwardFunc.apply(forward);
        assertThat(backward).isEqualTo(input);
        return backward;
    }

    @CanIgnoreReturnValue
    public static @SafeVarargs <U, V> void assertReversibleRoundtrip(@NotNull Reversible<U, V> rev, @NotNull U @NotNull ... inputs) {
        for (U input : inputs) {
            assertReversibleRoundtrip(rev, input);
        }
    }

    public static <T> void assertPrivateFieldValue(@NotNull T object, @NotNull String name, @NotNull Object expected) {
        assertThat(getPrivateFieldValue(object, name)).isEqualTo(expected);
    }

    public static <T> void assertPrivateFieldClass(@NotNull T object, @NotNull String name, @NotNull Class<?> expected) {
        Object value = getPrivateFieldValue(object, name);
        assertThat(value).isNotNull();
        assertThat(value.getClass()).isEqualTo(expected);
    }

    public static <T> Object getPrivateFieldValue(@NotNull T object, @NotNull String name) {
        List<Field> fields = ReflectionUtils.findFields(object.getClass(),
                                                        field -> field.getName().equals(name),
                                                        ReflectionUtils.HierarchyTraversalMode.BOTTOM_UP);
        assertThat(fields).hasSize(1);
        Field field = fields.getFirst();
        field.setAccessible(true);
        return Unchecked.Suppliers.rethrow(() -> field.get(object)).get();
    }
}
