package io.spbx.util.base.str;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.lazy.AtomicCacheCompute;
import io.spbx.util.lazy.AtomicLazyInit;
import io.spbx.util.lazy.CacheCompute;
import io.spbx.util.lazy.LazyInit;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Stateless
public class Regex {
    public static @NotNull RegexCache cache() {
        return new RegexCache();
    }

    public static @NotNull RegexDefer defer(@NotNull @RegEx @Language("RegExp") String regex) {
        return new RegexDefer(regex);
    }

    public static @NotNull RegexOps on(@NotNull CharSequence str) {
        return new RegexOps(str);
    }

    @Immutable
    public static class RegexCache {
        private final LazyInit<Pattern> cache = AtomicLazyInit.createUninitialized();

        public @NotNull Pattern cached(@NotNull @RegEx @Language("RegExp") String regex) {
            return cache.initializeIfNotYet(() -> Pattern.compile(regex));
        }

        public @NotNull CachedRegexOps on(@NotNull CharSequence str) {
            return new CachedRegexOps(str, this);
        }
    }

    @Immutable
    public static class RegexDefer {
        private final CacheCompute<Pattern> cache = AtomicCacheCompute.createEmpty();
        private final String regex;

        RegexDefer(@NotNull @RegEx @Language("RegExp") String regex) {
            this.regex = regex;
        }

        public @NotNull Pattern pattern() {
            return cache.getOrCompute(() -> Pattern.compile(regex));
        }

        public @NotNull Matcher on(@NotNull CharSequence str) {
            return pattern().matcher(str);
        }
    }

    @Immutable
    public static class RegexOps {
        private final CharSequence input;

        RegexOps(@NotNull CharSequence input) {
            this.input = input;
        }

        public boolean matches(@NotNull Pattern pattern) {
            return pattern.matcher(input).matches();
        }

        public boolean matches(@NotNull RegexDefer regex) {
            return regex.pattern().matcher(input).matches();
        }

        public boolean finds(@NotNull Pattern pattern) {
            return pattern.matcher(input).find();
        }

        public boolean finds(@NotNull RegexDefer regex) {
            return regex.pattern().matcher(input).find();
        }

        public @NotNull String replaceAll(@NotNull Pattern pattern, @NotNull String replacement) {
            return pattern.matcher(input).replaceAll(replacement);
        }

        public @NotNull String replaceAll(@NotNull RegexDefer regex, @NotNull String replacement) {
            return regex.pattern().matcher(input).replaceAll(replacement);
        }

        public @NotNull String replaceFirst(@NotNull Pattern pattern, @NotNull String replacement) {
            return pattern.matcher(input).replaceFirst(replacement);
        }

        public @NotNull String replaceFirst(@NotNull RegexDefer regex, @NotNull String replacement) {
            return regex.pattern().matcher(input).replaceFirst(replacement);
        }

        public @NotNull String @NotNull[] split(@NotNull Pattern pattern) {
            return pattern.split(input);
        }

        public @NotNull String @NotNull[] split(@NotNull Pattern pattern, int limit) {
            return pattern.split(input, limit);
        }

        public @NotNull String @NotNull[] split(@NotNull RegexDefer regex) {
            return regex.pattern().split(input);
        }

        public @NotNull String @NotNull[] split(@NotNull RegexDefer regex, int limit) {
            return regex.pattern().split(input, limit);
        }

        public @NotNull String @NotNull[] splitWithDelimiters(@NotNull Pattern pattern, int limit) {
            return pattern.splitWithDelimiters(input, limit);
        }

        public @NotNull String @NotNull[] splitWithDelimiters(@NotNull RegexDefer regex, int limit) {
            return regex.pattern().splitWithDelimiters(input, limit);
        }

        public @NotNull Stream<@NotNull String> splitAsStream(@NotNull Pattern pattern) {
            return pattern.splitAsStream(input);
        }

        public @NotNull Stream<@NotNull String> splitAsStream(@NotNull RegexDefer regex) {
            return regex.pattern().splitAsStream(input);
        }

        public <T> @Nullable T matchOrNull(@NotNull Pattern pattern, @NotNull Function<Matcher, T> action) {
            Matcher matcher = pattern.matcher(input);
            return matcher.matches() ? action.apply(matcher) : null;
        }

        public <T> @Nullable T matchOrNull(@NotNull RegexDefer regex, @NotNull Function<Matcher, T> action) {
            return matchOrNull(regex.pattern(), action);
        }

        public <T> @NotNull Optional<T> match(@NotNull Pattern pattern, @NotNull Function<Matcher, T> action) {
            Matcher matcher = pattern.matcher(input);
            return matcher.matches() ? Optional.of(action.apply(matcher)) : Optional.empty();
        }

        public <T> @NotNull Optional<T> match(@NotNull RegexDefer regex, @NotNull Function<Matcher, T> action) {
            return match(regex.pattern(), action);
        }

        public <T> @Nullable T findOrNull(@NotNull Pattern pattern, @NotNull Function<Matcher, T> action) {
            Matcher matcher = pattern.matcher(input);
            return matcher.find() ? action.apply(matcher) : null;
        }

        public <T> @Nullable T findOrNull(@NotNull RegexDefer regex, @NotNull Function<Matcher, T> action) {
            return findOrNull(regex.pattern(), action);
        }

        public <T> @NotNull Optional<T> find(@NotNull Pattern pattern, @NotNull Function<Matcher, T> action) {
            Matcher matcher = pattern.matcher(input);
            return matcher.find() ? Optional.of(action.apply(matcher)) : Optional.empty();
        }

        public <T> @NotNull Optional<T> find(@NotNull RegexDefer regex, @NotNull Function<Matcher, T> action) {
            return find(regex.pattern(), action);
        }
    }

    @Immutable
    public static class CachedRegexOps {
        private final CharSequence input;
        private final RegexCache cache;

        CachedRegexOps(@NotNull CharSequence input, @NotNull RegexCache cache) {
            this.input = input;
            this.cache = cache;
        }

        public boolean matches(@NotNull @RegEx @Language("RegExp") String regex) {
            return cache.cached(regex).matcher(input).matches();
        }

        public boolean finds(@NotNull @RegEx @Language("RegExp") String regex) {
            return cache.cached(regex).matcher(input).find();
        }

        public @NotNull String replaceAll(@NotNull @RegEx @Language("RegExp") String regex,
                                          @NotNull String replacement) {
            return cache.cached(regex).matcher(input).replaceAll(replacement);
        }

        public @NotNull String replaceFirst(@NotNull @RegEx @Language("RegExp") String regex,
                                            @NotNull String replacement) {
            return cache.cached(regex).matcher(input).replaceFirst(replacement);
        }

        public @NotNull String @NotNull[] split(@NotNull @RegEx @Language("RegExp") String regex) {
            return cache.cached(regex).split(input);
        }

        public @NotNull String @NotNull[] split(@NotNull @RegEx @Language("RegExp") String regex, int limit) {
            return cache.cached(regex).split(input, limit);
        }

        public @NotNull String @NotNull[] splitWithDelimiters(@NotNull @RegEx @Language("RegExp") String regex, int limit) {
            return cache.cached(regex).splitWithDelimiters(input, limit);
        }

        public @NotNull Stream<@NotNull String> splitAsStream(@NotNull @RegEx @Language("RegExp") String regex) {
            return cache.cached(regex).splitAsStream(input);
        }

        public <T> @Nullable T matchOrNull(@NotNull @RegEx @Language("RegExp") String regex,
                                           @NotNull Function<Matcher, T> action) {
            Matcher matcher = cache.cached(regex).matcher(input);
            return matcher.matches() ? action.apply(matcher) : null;
        }

        public <T> @NotNull Optional<T> match(@NotNull @RegEx @Language("RegExp") String regex,
                                              @NotNull Function<Matcher, T> action) {
            Matcher matcher = cache.cached(regex).matcher(input);
            return matcher.matches() ? Optional.of(action.apply(matcher)) : Optional.empty();
        }

        public <T> @Nullable T findOrNull(@NotNull @RegEx @Language("RegExp") String regex,
                                          @NotNull Function<Matcher, T> action) {
            Matcher matcher = cache.cached(regex).matcher(input);
            return matcher.find() ? action.apply(matcher) : null;
        }

        public <T> @NotNull Optional<T> find(@NotNull @RegEx @Language("RegExp") String regex,
                                             @NotNull Function<Matcher, T> action) {
            Matcher matcher = cache.cached(regex).matcher(input);
            return matcher.find() ? Optional.of(action.apply(matcher)) : Optional.empty();
        }
    }
}
