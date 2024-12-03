package io.spbx.util.testing;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;

@Stateless
public class AssertBasics {
    @CheckReturnValue
    public static <T> @NotNull PredicateSubject<T> assertPredicate(@NotNull Predicate<T> predicate) {
        return new PredicateSubject<>(predicate);
    }

    @CheckReturnValue
    public static @NotNull PredicateSubject<Integer> assertIntPredicate(@NotNull IntPredicate predicate) {
        return new PredicateSubject<>(predicate::test);
    }

    @CanIgnoreReturnValue
    public record PredicateSubject<T>(@NotNull Predicate<T> predicate) {
        public final @SafeVarargs @NotNull PredicateSubject<T> isTrueFor(@Nullable T @NotNull ... values) {
            for (T value : values) {
                assertThat(predicate.test(value)).isTrue();
            }
            return this;
        }

        public final @SafeVarargs @NotNull PredicateSubject<T> isFalseFor(@Nullable T @NotNull... values) {
            for (T value : values) {
                assertThat(predicate.test(value)).isFalse();
            }
            return this;
        }

        public final @SafeVarargs @NotNull PredicateSubject<T> isEquivalentTo(@NotNull Predicate<T> expected,
                                                                              @Nullable T @NotNull... values) {
            for (T value : values) {
                assertThat(predicate.test(value)).isEqualTo(expected.test(value));
            }
            return this;
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

    @CheckReturnValue
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
