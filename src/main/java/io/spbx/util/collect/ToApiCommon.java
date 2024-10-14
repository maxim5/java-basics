package io.spbx.util.collect;

import io.spbx.util.func.ThrowUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

class ToApiCommon {
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface Contract {
        String comments() default "";
        boolean acceptsNulls();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface AcceptsAllStreams {
        String comments() default "";
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface AcceptsNulls {
        String comments() default "";
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface DoesNotAcceptNulls {
        String comments() default "";
    }

    @FunctionalInterface
    interface StreamOperator<E> extends ThrowUnaryOperator<Stream<E>, RuntimeException> {
    }

    static <E> @NotNull StreamOperator<E> assertNonNull(@NotNull String name) {
        return stream -> stream.peek(it -> {
            assert it != null : "The stream contains nulls. `%s` is not supported".formatted(name);
        });
    }

    static <E> @NotNull StreamOperator<E> assertNonNull(@NotNull String name,
                                                        @NotNull Function<? super E, ?> key,
                                                        @NotNull Function<? super E, ?> val) {
        return stream -> stream.peek(it -> {
            assert it != null : "The stream contains nulls. `%s` is not supported".formatted(name);
            assert key.apply(it) != null : "The stream contains null keys: `%s`. `%s` is not supported".formatted(it, name);
            assert val.apply(it) != null : "The stream contains null values: `%s`. `%s` is not supported".formatted(it, name);
        });
    }

    static <E extends Entry<?, ?>> @NotNull StreamOperator<E> assertKvNonNull(@NotNull String name) {
        return stream -> stream.peek(e -> {
            assert e != null : "The key-value stream contains null entries. `%s` is not supported".formatted(name);
            assert e.getKey() != null : "The key-value stream contains null keys: `%s`. `%s` is not supported".formatted(e, name);
            assert e.getValue() != null : "The key-value stream contains null values: `%s`. `%s` is not supported".formatted(e, name);
        });
    }
}
