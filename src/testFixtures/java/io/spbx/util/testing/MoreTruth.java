package io.spbx.util.testing;

import com.google.common.truth.BooleanSubject;
import com.google.common.truth.CustomSubjectBuilder;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.IntegerSubject;
import com.google.common.truth.IterableSubject;
import com.google.common.truth.MapSubject;
import com.google.common.truth.StandardSubjectBuilder;
import com.google.common.truth.StringSubject;
import com.google.common.truth.Subject;
import com.google.common.truth.ThrowableSubject;
import com.google.common.truth.Truth;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.collect.stream.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static java.util.Objects.requireNonNull;

@Stateless
public class MoreTruth {
    @CheckReturnValue
    public static @NotNull StringSubject assertThat(@Nullable CharSequence charSequence) {
        return Truth.assertThat(charSequence != null ? charSequence.toString() : null);
    }

    @CheckReturnValue
    public static @NotNull MoreStringSubject assertThat(@Nullable String str) {
        return Truth.assertAbout(metadata -> new CustomSubjectBuilder(metadata) {
            @NotNull MoreStringSubject that() {
                return new MoreStringSubject(metadata, str);
            }
        }).that();
    }

    @CheckReturnValue
    public static <T> @NotNull IterableSubject assertThat(@Nullable Iterator<T> iterator) {
        Iterable<T> iterable = iterator == null ? null : () -> iterator;
        return Truth.assertThat(iterable);
    }

    @CheckReturnValue
    public static <K, V> @NotNull MoreMapSubject<K, V> assertThat(@Nullable Map<K, V> map) {
        return Truth.assertAbout(metadata -> new CustomSubjectBuilder(metadata) {
            public @NotNull MoreMapSubject<K, V> that() {
                return new MoreMapSubject<>(metadata, map);
            }
        }).that();
    }

    @CheckReturnValue
    public static @NotNull MoreBooleanSubject assertThat(boolean actual) {
        return Truth.assertAbout(metadata -> new CustomSubjectBuilder(metadata) {
            @NotNull MoreBooleanSubject that() {
                return new MoreBooleanSubject(metadata, actual);
            }
        }).that();
    }

    @CheckReturnValue
    public static @NotNull MoreThrowableSubject assertThat(@Nullable Throwable actual) {
        return Truth.assertAbout(metadata -> new CustomSubjectBuilder(metadata) {
            @NotNull MoreThrowableSubject that() {
                return new MoreThrowableSubject(metadata, actual);
            }
        }).that();
    }

    @CheckReturnValue
    public static <T> @NotNull AlsoSubject<T> assertAlso(@Nullable T actual) {
        return new AlsoSubject<>(actual);
    }

    @CheckReturnValue
    public static @NotNull IntegerSubject assertThat(@Nullable FailureMetadata metadata, @Nullable Integer i) {
        return new IntegerSubject(metadata, i) {};
    }

    @CheckReturnValue
    public static @NotNull StringSubject assertThat(@Nullable FailureMetadata metadata, @Nullable String str) {
        return new StringSubject(metadata, str) {};
    }

    @CheckReturnValue
    public static @NotNull IterableSubject assertThat(@Nullable FailureMetadata metadata, @Nullable Iterable<?> iter) {
        return new IterableSubject(metadata, iter) {};
    }

    public static class MoreBooleanSubject extends Subject {
        private final FailureMetadata metadata;
        private final boolean actual;

        protected MoreBooleanSubject(@NotNull FailureMetadata metadata, boolean actual) {
            super(metadata, actual);
            this.metadata = metadata;
            this.actual = actual;
        }

        @CheckReturnValue
        public @NotNull BooleanSubject withMessage(String format, @Nullable Object... args) {
            return assert_(metadata).withMessage(format, args).that(actual);
        }
    }

    public static class MoreStringSubject extends StringSubject {
        private final FailureMetadata metadata;
        private final String str;

        protected MoreStringSubject(@Nullable FailureMetadata metadata, @Nullable String str) {
            super(metadata, str);
            this.metadata = metadata;
            this.str = str;
        }

        public @NotNull MoreStringSubject trimmed() {
            return str != null ? new MoreStringSubject(metadata, str.trim()) : this;
        }

        public void linesMatch(@Nullable String expected) {
            if (expected == null) {
                isNull();
            } else {
                linesMatch(expected.lines().toList());
            }
        }

        public void linesMatch(@Nullable Iterable<String> expected) {
            if (expected == null) {
                isNull();
            } else {
                isNotNull();
                assertThat(metadata, requireNonNull(str).lines().toList())
                    .containsExactlyElementsIn(expected)
                    .inOrder();
            }
        }
    }

    public static class MoreMapSubject<K, V> extends MapSubject {
        private final FailureMetadata metadata;
        private final Map<K, V> map;

        protected MoreMapSubject(@Nullable FailureMetadata metadata, @Nullable Map<K, V> map) {
            super(metadata, map);
            this.metadata = metadata;
            this.map = map;
        }

        public @NotNull MoreMapSubject<K, V> trimmed() {
            return map == null ? this : new MoreMapSubject<>(metadata, Streamer.of(map).filterValues(Objects::nonNull).toMap());
        }
    }

    public static class MoreThrowableSubject extends ThrowableSubject {
        private final FailureMetadata metadata;
        private final Throwable throwable;

        protected MoreThrowableSubject(@Nullable FailureMetadata metadata, @Nullable Throwable throwable) {
            super(metadata, throwable);
            this.metadata = metadata;
            this.throwable = throwable;
        }

        @CanIgnoreReturnValue
        public @NotNull MoreThrowableSubject hasMessageContains(@Nullable CharSequence string) {
            hasMessageThat().contains(string);
            return this;
        }

        @CanIgnoreReturnValue
        public @NotNull MoreThrowableSubject hasMessageMatching(@Nullable String regex) {
            hasMessageThat().matches(regex);
            return this;
        }

        @CanIgnoreReturnValue
        public @NotNull MoreThrowableSubject hasMessageMatching(@Nullable Pattern regex) {
            hasMessageThat().matches(regex);
            return this;
        }
    }

    public record AlsoSubject<T>(@Nullable T actual) {
        public void isEquivalentTo(@Nullable T expected) {
            Truth.assertThat(actual).isEqualTo(expected);
            Truth.assertThat(Objects.toString(actual)).isEqualTo(Objects.toString(expected));
            Truth.assertThat(Objects.hashCode(actual)).isEqualTo(Objects.hashCode(expected));
        }
    }

    static @NotNull StandardSubjectBuilder assert_(@NotNull FailureMetadata metadata) {
        try {
            Constructor<StandardSubjectBuilder> constructor = castAny(StandardSubjectBuilder.class.getDeclaredConstructors()[0]);
            constructor.setAccessible(true);
            return constructor.newInstance(metadata);
        } catch (Throwable e) {
            return Unchecked.rethrow(e);
        }
    }
}
