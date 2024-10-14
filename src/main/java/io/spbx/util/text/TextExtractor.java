package io.spbx.util.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import io.spbx.util.array.CharArray;
import io.spbx.util.array.MutableCharArray;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.lazy.AtomicLazyRecycle;
import io.spbx.util.lazy.LazyRecycle;
import io.spbx.util.lazy.Sealed;
import io.spbx.util.lazy.SealedGroup;
import io.spbx.util.lazy.SealedListGroup;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.spbx.util.base.BasicExceptions.newIllegalStateException;
import static io.spbx.util.base.BasicExceptions.newInternalError;
import static io.spbx.util.base.EasyCast.castAny;
import static java.util.Objects.requireNonNull;

@Immutable
public class TextExtractor {
    private static final Logger log = Logger.forEnclosingClass();

    private final ImmutableList<Action> actions;
    private final Sanitizer sanitizer;

    private TextExtractor(@NotNull Iterable<Action> actions, @NotNull Sanitizer sanitizer) {
        this.actions = ImmutableList.copyOf(actions);
        this.sanitizer = sanitizer;
        this.initActions();
    }

    private TextExtractor(@NotNull Iterable<Action> actions) {
        this(actions, Sanitizer.TRIM_SPACES);
    }

    private void initActions() {
        actions.forEach(action -> {
            action.initializeOrDie(this);
            action.sealIfNotYet();
            // auto-assign names?
        });
    }

    public static @NotNull TextExtractor of(@NotNull Action @NotNull ... actions) {
        return TextExtractor.of(ImmutableList.copyOf(actions));
    }

    public static @NotNull TextExtractor of(@NotNull Iterable<Action> actions) {
        return new TextExtractor(ImmutableList.copyOf(actions));
    }

    public @NotNull TextExtractor with(@NotNull Sanitizer sanitizer) {
        return new TextExtractor(actions.stream().map(Action::detach).toList(), sanitizer);
    }

    public @NotNull ExtractedMap extract(@NotNull CharSequence text) {
        MutableCharArray array = MutableCharArray.of(text);
        ExtractedMap result = new ExtractedMap();
        for (Action action : actions) {
            action.apply(array, result::put);
        }
        return result;
    }

    // Actions

    public static @NotNull MatchAction skipTo(@NotNull CharSequence mark) {
        CharArray mark_arr = CharArray.asCharArray(mark);
        return new MatchAction(mark) {
            @Override void applyTo(@NotNull MutableCharArray array) {
                int i = assureNonNegative(array.indexOf(mark_arr), "mark not found `%s`", mark);
                moveTo.get().moveForward(array, i, i + mark_arr.length());
            }
        };
    }

    public static @NotNull MatchAction skipTo(@NotNull Pattern pattern) {
        return new MatchAction(pattern.pattern()) {
            @Override void applyTo(@NotNull MutableCharArray array) {
                Matcher matcher = assureNonNull(array.indexOf(pattern), "pattern not found `%s`", pattern);
                if (matcher != null) {
                    moveTo.get().moveForward(array, matcher.start(), matcher.end());
                }
            }
        };
    }

    public static @NotNull MatchAction skipBackwardTo(@NotNull CharSequence mark) {
        CharArray mark_arr = CharArray.asCharArray(mark);
        return new MatchAction(mark) {
            @Override void applyTo(@NotNull MutableCharArray array) {
                int i = assureNonNegative(array.lastIndexOf(mark_arr), "backward mark not found `%s`", mark);
                moveTo.get().moveBack(array, i + mark_arr.length(), i);
            }
        };
    }

    public static @NotNull IntoRegionAction narrowDownTo(@NotNull CharSequence start, @NotNull CharSequence end) {
        CharArray start_arr = CharArray.asCharArray(start);
        CharArray end_arr = CharArray.asCharArray(end);
        return new IntoRegionAction(start) {
            @Override void applyTo(@NotNull MutableCharArray array) {
                int i = assureNonNegative(array.indexOf(start_arr), "start mark not found `%s`", start);
                int j = assureNonNegative(array.lastIndexOf(end_arr), "end mark not found `%s`", end);
                moveTo.get().moveInto(array, i, i + start_arr.length(), j, j + end_arr.length());
            }
        };
    }

    public static @NotNull RegexMatchAction narrowDownTo(@NotNull Pattern pattern) {
        return new RegexMatchAction(pattern.pattern()) {
            @Override void applyTo(@NotNull MutableCharArray array) {
                Matcher matcher = assureNonNull(array.indexOf(pattern), "pattern not found `%s`", pattern);
                if (matcher != null) {
                    int group = this.group.get();
                    assure(matcher.groupCount() >= group, "pattern `%s` doesn't have group %d", pattern, group);
                    moveTo.get().moveInto(array, matcher.start(), matcher.start(group), matcher.end(group), matcher.end());
                }
            }
        };
    }

    public static @NotNull RegionMatchCapture captureBetween(@NotNull CharSequence start, @NotNull CharSequence end) {
        CharArray start_arr = CharArray.asCharArray(start);
        CharArray end_arr = CharArray.asCharArray(end);
        return new RegionMatchCapture(start) {
            @Override @Nullable CharArray captureFrom(@NotNull MutableCharArray array) {
                int i = assureNonNegative(array.indexOf(start_arr), "start mark not found `%s`", start);
                int j = i < 0 ? -1 : assureNonNegative(array.indexOf(end_arr, i + start_arr.length()),
                                                       "end mark not found `%s`", end);
                if (i >= 0 && j >= 0) {
                    CharArray result = array.substring(i + start_arr.length(), j);
                    moveTo.get().moveForward(array, i, i + start_arr.length(), j, j + end_arr.length());
                    return result;
                }
                return null;
            }
        };
    }

    public static @NotNull MatchMultiCapture capturePattern(@NotNull Pattern pattern) {
        return new MatchMultiCapture(pattern.pattern()) {
            @Override void captureFrom(@NotNull MutableCharArray array, @NotNull Consumer<CharArray> callback) {
                Matcher matcher = assureNonNull(array.indexOf(pattern), "pattern not found `%s`", pattern);
                if (matcher != null) {
                    for (int i = 0; i <= matcher.groupCount(); i++) {
                        CharArray result = array.substring(matcher.start(i), matcher.end(i));
                        callback.accept(result);
                    }
                    moveTo.get().moveForward(array, matcher.start(), matcher.end());
                }
            }
        };
    }

    public static @NotNull RepeatAction repeat(@NotNull Action @NotNull ... actions) {
        return new RepeatAction("repeat:%s".formatted(actions.length)) {
            @Override protected void initializeOrDie(@NotNull TextExtractor extractor) {
                super.initializeOrDie(extractor);
                Stream.of(actions).forEach(action -> action.initializeOrDie(extractor));
            }
            @Override protected void sealIfNotYet() {
                super.sealIfNotYet();
                Stream.of(actions).forEach(Action::sealIfNotYet);
            }
            @Override void applyTo(@NotNull String ignore, @NotNull MutableCharArray array, @NotNull ExtractCallback callback) {
                int maxIterations = this.maxIterations.get() >= 0 ? this.maxIterations.get() : Integer.MAX_VALUE;
                for (int iteration = 0; iteration < maxIterations; iteration++) {
                    int start = array.start();
                    int end = array.end();
                    for (Action action : actions) {
                        action.applyTo("%s:%s".formatted(action.name(), iteration), array, callback);
                    }
                    if (array.start() == start && array.end() == end) {
                        log.debug().log("Actions are stuck at the same positions (%s, %s) after %s iterations",
                                        start, end, iteration);
                        return;
                    }
                }
                applyFallback(() -> "Repeat has not completed after %s iterations".formatted(maxIterations));
            }
        };
    }

    public static abstract @Immutable class Action {
        protected final LazyRecycle<TextExtractor> extractor = AtomicLazyRecycle.createUninitialized();
        protected final SealedGroup sealedGroup = SealedListGroup.empty();
        protected final Sealed<String> name;
        protected final Sealed<Fallback> fallback;

        protected Action(@NotNull CharSequence name, @NotNull Fallback fallback) {
            this.name = sealedGroup.createAndAdd(name.toString().intern());
            this.fallback = sealedGroup.createAndAdd(fallback);
        }

        protected Action(@NotNull CharSequence name) {
            this(name, Fallback.THROW);
        }

        protected void initializeOrDie(@NotNull TextExtractor extractor) {
            this.extractor.initializeOrDie(extractor);
        }

        protected @NotNull Action detach() {
            this.extractor.recycle();
            return this;
        }

        protected void sealIfNotYet() {
            this.sealedGroup.sealAllIfNotYet();
        }

        protected @NotNull String name() {
            return this.name.get();
        }

        public @NotNull Action named(@NotNull String name) {
            this.name.sealOrDie(name);
            return this;
        }

        public @NotNull Action orElse(@NotNull Fallback fallback) {
            this.fallback.sealOrDie(fallback);
            return this;
        }

        public @NotNull Action orElseIgnore() {
            return this.orElse(Fallback.IGNORE);
        }

        public @NotNull Action orElseThrow() {
            return this.orElse(Fallback.THROW);
        }

        protected void checkIsInitializedOrDie() {
            assert this.extractor.isInitialized() : newInternalError("Not initialized: " + this);
            assert this.sealedGroup.isSealed() : newInternalError("Not sealed: " + this);
        }

        protected @NotNull CharArray sanitize(@NotNull CharArray array) {
            return this.extractor.get().sanitizer.sanitize(array);
        }

        void apply(@NotNull MutableCharArray array, @NotNull ExtractCallback callback) {
            applyTo(name(), array, callback);
        }

        abstract void applyTo(@NotNull String name, @NotNull MutableCharArray array, @NotNull ExtractCallback callback);

        int assureNonNegative(int value, @NotNull String message, @Nullable Object @NotNull ... args) {
            assure(value >= 0, message, args);
            return value;
        }

        <R> R assureNonNull(@Nullable R value, @NotNull String message, @Nullable Object @NotNull ... args) {
            assure(value != null, message, args);
            return value;
        }

        void assure(boolean condition, @NotNull String message, @Nullable Object @NotNull ... args) {
            if (!condition) {
                applyFallback(() -> message.formatted(args));
            }
        }

        void applyFallback(@NotNull Supplier<String> message) {
            switch (fallback.get()) {
                case IGNORE -> {}
                case LOG_DEBUG -> log.debug().log(message.get());
                case LOG_INFO -> log.info().log(message.get());
                case LOG_WARN -> log.warn().log(message.get());
                case LOG_ERROR -> log.error().log(message.get());
                case THROW -> throw newIllegalStateException("Extraction failed: " + message.get());
            }
        }
    }

    public static abstract @Immutable class TypedAction<A extends TypedAction<?>> extends Action {
        protected TypedAction(@NotNull CharSequence name, @NotNull Fallback fallback) {
            super(name, fallback);
        }

        public TypedAction(@NotNull CharSequence name) {
            super(name);
        }

        @Override public @NotNull A named(@NotNull String name) { return castAny(super.named(name)); }
        @Override public @NotNull A orElse(@NotNull Fallback fallback) { return castAny(super.orElse(fallback)); }
        @Override public @NotNull A orElseIgnore() { return castAny(super.orElseIgnore()); }
        @Override public @NotNull A orElseThrow() { return castAny(super.orElseThrow()); }
    }

    public static abstract @Immutable class MoveableAction<A extends MoveableAction<?, ?>, M> extends TypedAction<A> {
        protected final Sealed<M> moveTo;

        protected MoveableAction(@NotNull CharSequence name, @NotNull Fallback fallback, @NotNull M moveTo) {
            super(name, fallback);
            this.moveTo = sealedGroup.createAndAdd(moveTo);
        }

        protected MoveableAction(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name);
            this.moveTo = sealedGroup.createAndAdd(moveTo);
        }

        public @NotNull A moveVia(@NotNull M moveTo) {
            this.moveTo.sealOrDie(moveTo);
            return castAny(this);
        }
    }

    public static abstract @Immutable class NonCaptureAction<A extends MoveableAction<?, ?>, M> extends MoveableAction<A, M> {
        protected NonCaptureAction(@NotNull CharSequence name, @NotNull Fallback fallback, @NotNull M moveTo) {
            super(name, fallback, moveTo);
        }

        protected NonCaptureAction(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name, moveTo);
        }

        @Override void applyTo(@NotNull String name, @NotNull MutableCharArray array, @NotNull ExtractCallback callback) {
            checkIsInitializedOrDie();
            applyTo(array);
        }
        abstract void applyTo(@NotNull MutableCharArray array);
    }

    public static abstract @Immutable class MatchAction extends NonCaptureAction<MatchAction, MoveTo> {
        protected MatchAction(@NotNull CharSequence name) {
            super(name, MoveTo.EXCLUDE_MATCH);
        }
    }

    public static abstract @Immutable class RegionMatchAction extends NonCaptureAction<RegionMatchAction, RegionMoveTo> {
        protected RegionMatchAction(@NotNull CharSequence name) {
            super(name, RegionMoveTo.AFTER_END_MATCH);
        }
    }

    public static abstract @Immutable class IntoRegionAction extends NonCaptureAction<IntoRegionAction, MoveIntoRegion> {
        protected IntoRegionAction(@NotNull CharSequence name) {
            super(name, MoveIntoRegion.MOVE_INSIDE_EXCLUDE_MATCH);
        }
    }

    public static abstract @Immutable class RegexMatchAction extends IntoRegionAction {
        protected final Sealed<Integer> group = sealedGroup.createAndAdd(1);

        protected RegexMatchAction(@NotNull CharSequence name) {
            super(name);
        }

        public @NotNull RegexMatchAction captureRegexGroup(int group) {
            this.group.sealOrDie(group);
            return this;
        }
        public @NotNull RegexMatchAction captureWholeRegex() {
            return captureRegexGroup(0);
        }
    }

    public static abstract @Immutable class Convertible<A extends Convertible<?, ?>, M> extends MoveableAction<A, M> {
        protected final Sealed<Function<CharArray, Object>> converter = sealedGroup.createAndAdd(array -> null);

        protected Convertible(@NotNull CharSequence name, @NotNull Fallback fallback, @NotNull M moveTo) {
            super(name, fallback, moveTo);
        }

        protected Convertible(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name, moveTo);
        }

        public @NotNull <R> A convertVia(@NotNull Function<CharArray, R> converter) {
            this.converter.sealOrDie(castAny(converter));
            return castAny(this);
        }

        protected @Nullable Object convert(@NotNull CharArray array) {
            return this.converter.get().apply(array);
        }
    }

    public static abstract @Immutable class Capture<A extends Capture<?, ?>, M> extends Convertible<A, M> {
        protected final Sealed<Consumer<CharArray>> consumer = sealedGroup.createAndAdd(array -> {});

        protected Capture(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name, Fallback.LOG_WARN, moveTo);
        }

        public @NotNull A onCapture(@NotNull Consumer<CharArray> consumer) {
            this.consumer.sealOrDie(consumer);
            return castAny(this);
        }

        @Override void applyTo(@NotNull String name, @NotNull MutableCharArray array, @NotNull ExtractCallback callback) {
            checkIsInitializedOrDie();
            CharArray captured = captureFrom(array);
            if (captured != null) {
                CharArray sanitized = sanitize(captured);
                Object value = convert(sanitized);
                consumer.get().accept(sanitized);
                callback.onCapture(name, sanitized, value);
            }
        }

        abstract @Nullable CharArray captureFrom(@NotNull MutableCharArray array);
    }

    public static abstract @Immutable class MatchCapture extends Capture<MatchCapture, MoveTo> {
        protected MatchCapture(@NotNull CharSequence name) {
            super(name, MoveTo.EXCLUDE_MATCH);
        }
    }

    public static abstract @Immutable class RegionMatchCapture extends Capture<RegionMatchCapture, RegionMoveTo> {
        protected RegionMatchCapture(@NotNull CharSequence name) {
            super(name, RegionMoveTo.AFTER_END_MATCH);
        }
    }

    public static abstract @Immutable class MultiConvertible<A extends MultiConvertible<?, ?>, M> extends MoveableAction<A, M> {
        protected final Sealed<Map<Integer, Function<CharArray, Object>>> converters = sealedGroup.createAndAdd(BasicMaps.newOrderedMap());

        protected MultiConvertible(@NotNull CharSequence name, @NotNull Fallback fallback, @NotNull M moveTo) {
            super(name, fallback, moveTo);
        }

        protected MultiConvertible(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name, moveTo);
        }

        public @NotNull <R> A convertVia(int index, @NotNull Function<CharArray, R> converter) {
            if (!this.converters.isSealed()) {
                this.converters.get().put(index, castAny(converter));
            } else {
                this.converters.sealOrDie();
            }
            return castAny(this);
        }

        @Override protected void sealIfNotYet() {
            this.converters.finalizeAndSealIfNotYet(ImmutableMap::copyOf);
            super.sealIfNotYet();
        }

        protected @Nullable Object convert(@NotNull CharArray array, int index) {
            return this.converters.get().getOrDefault(index, __ -> null).apply(array);
        }
    }

    public static abstract @Immutable class MultiCapture<A extends MultiCapture<?, ?>, M> extends MultiConvertible<A, M> {
        protected final Sealed<ObjIntConsumer<CharArray>> consumer = sealedGroup.createAndAdd((array, idx) -> {});

        protected MultiCapture(@NotNull CharSequence name, @NotNull M moveTo) {
            super(name, Fallback.LOG_WARN, moveTo);
        }

        public @NotNull A onCapture(@NotNull ObjIntConsumer<CharArray> consumer) {
            this.consumer.sealOrDie(consumer);
            return castAny(this);
        }

        @Override void applyTo(@NotNull String name, @NotNull MutableCharArray array, @NotNull ExtractCallback callback) {
            checkIsInitializedOrDie();
            captureFrom(array, new Consumer<>() {
                private int count = 0;
                @Override public void accept(@NotNull CharArray captured) {
                    CharArray sanitized = sanitize(captured);
                    Object value = convert(sanitized, count);
                    consumer.get().accept(sanitized, count);
                    callback.onCapture("%s:%d".formatted(name, count), sanitized, value);
                    count++;
                }
            });
        }

        abstract void captureFrom(@NotNull MutableCharArray array, @NotNull Consumer<CharArray> callback);
    }

    public static abstract @Immutable class MatchMultiCapture extends MultiCapture<MatchMultiCapture, MoveTo> {
        protected MatchMultiCapture(@NotNull CharSequence name) {
            super(name, MoveTo.EXCLUDE_MATCH);
        }
    }

    public static abstract @Immutable class RegionMatchMultiCapture extends MultiCapture<RegionMatchMultiCapture, RegionMoveTo> {
        protected RegionMatchMultiCapture(@NotNull CharSequence name) {
            super(name, RegionMoveTo.AFTER_END_MATCH);
        }
    }

    public static abstract @Immutable class RepeatAction extends TypedAction<RepeatAction> {
        protected final Sealed<Integer> maxIterations = sealedGroup.createAndAdd(100);

        protected RepeatAction(@NotNull CharSequence name) {
            super(name, Fallback.LOG_WARN);
        }

        public @NotNull RepeatAction maxIterations(int iterations) {
            assert iterations >= -1 : "Invalid iterations number: " + iterations;
            this.maxIterations.sealIfNotYet(iterations);
            return this;
        }
    }

    interface ExtractCallback {
        void onCapture(@NotNull String actionName, @NotNull CharArray captured, @Nullable Object converted);
    }

    public enum Fallback {
        IGNORE,
        LOG_DEBUG,
        LOG_INFO,
        LOG_WARN,
        LOG_ERROR,
        THROW,
    }

    public @FunctionalInterface interface MoveTo {
        void moveTo(@NotNull MutableCharArray array, int atMatch, int afterMatch, boolean forward);

        default void moveForward(@NotNull MutableCharArray array, int atMatch, int afterMatch) {
            if (atMatch >= 0 && afterMatch >= 0) {
                moveTo(array, atMatch, afterMatch, true);
            }
        }

        default void moveBack(@NotNull MutableCharArray array, int atMatch, int afterMatch) {
            if (atMatch >= 0 && afterMatch >= 0) {
                moveTo(array, atMatch, afterMatch, false);
            }
        }

        MoveTo DO_NOT_MOVE = (array, atMatch, afterMatch, forward) -> {};
        MoveTo INCLUDE_MATCH = (array, atMatch, afterMatch, forward) -> sliceEitherWayInPlace(array, atMatch, forward);
        MoveTo EXCLUDE_MATCH = (array, atMatch, afterMatch, forward) -> sliceEitherWayInPlace(array, afterMatch, forward);
    }

    public @FunctionalInterface interface RegionMoveTo {
        void moveTo(@NotNull MutableCharArray array,
                    int atStartMatch, int afterStartMatch, int atEndMatch, int afterEndMatch, boolean forward);

        default void moveForward(@NotNull MutableCharArray array,
                                 int atStartMatch, int afterStartMatch, int atEndMatch, int afterEndMatch) {
            if (atStartMatch >= 0 && afterStartMatch >= 0 && atEndMatch >= 0 && afterEndMatch >= 0) {
                moveTo(array, atStartMatch, afterStartMatch, atEndMatch, afterEndMatch, true);
            }
        }

        default void moveBack(@NotNull MutableCharArray array,
                                 int atStartMatch, int afterStartMatch, int atEndMatch, int afterEndMatch) {
            if (atStartMatch >= 0 && afterStartMatch >= 0 && atEndMatch >= 0 && afterEndMatch >= 0) {
                moveTo(array, atStartMatch, afterStartMatch, atEndMatch, afterEndMatch, false);
            }
        }

        RegionMoveTo DO_NOT_MOVE = (array, at1, after1, at2, after2, forward) -> {};
        RegionMoveTo AT_START_MATCH = (array, at1, after1, _1, _2, forward) -> sliceEitherWayInPlace(array, at1, forward);
        RegionMoveTo AFTER_START_MATCH = (array, at1, after1, _1, _2, forward) -> sliceEitherWayInPlace(array, after1, forward);
        RegionMoveTo AT_END_MATCH = (array, _1, _2, at2, after2, forward) -> sliceEitherWayInPlace(array, at2, forward);
        RegionMoveTo AFTER_END_MATCH = (array, _1, _2, at2, after2, forward) -> sliceEitherWayInPlace(array, after2, forward);
    }

    public @FunctionalInterface interface MoveIntoRegion {
        void moveInto(@NotNull MutableCharArray array, int atStartMatch, int afterStartMatch, int atEndMatch, int afterEndMatch);

        MoveIntoRegion DO_NOT_MOVE = (array, at1, after1, at2, after2) -> {};
        MoveIntoRegion MOVE_INSIDE_EXCLUDE_MATCH = (array, at1, after1, at2, after2) -> {
            if (at1 >= 0 && after1 >= 0 && at2 >= 0 && after2 >= 0) {
                array.sliceInPlace(after1, at2);
            }
        };
        MoveIntoRegion MOVE_INSIDE_INCLUDE_MATCH = (array, at1, after1, at2, after2) -> {
            if (at1 >= 0 && after1 >= 0 && at2 >= 0 && after2 >= 0) {
                array.sliceInPlace(at1, after2);
            }
        };
    }

    private static void sliceEitherWayInPlace(@NotNull MutableCharArray array, int i, boolean forward) {
        if (forward) {
            array.sliceFromInPlace(i);
        } else {
            array.sliceUntilInPlace(i);
        }
    }

    public @FunctionalInterface interface Sanitizer {
        @NotNull CharArray sanitize(@NotNull CharArray value);

        Sanitizer DO_NOTHING = value -> value;
        Sanitizer TRIM_SPACES = CharArray::trim;
    }

    public @Immutable record Extracted(@NotNull CharArray captured, @Nullable Object convertedValue) {
        static @NotNull Extracted of(@NotNull String captured, @NotNull Object convertedValue) {
            return new Extracted(CharArray.of(captured), convertedValue);
        }
        static @NotNull Extracted of(@NotNull String captured) {
            return new Extracted(CharArray.of(captured), null);
        }
    }

    public static class ExtractedMap {
        private final LinkedHashMap<String, Extracted> map = BasicMaps.newOrderedMap();

        public @Nullable Object getConvertedValueOrNull(@NotNull String key) {
            Extracted extracted = map.get(key);
            return extracted != null ? extracted.convertedValue() : null;
        }

        public <T> @NotNull T getConvertedValueOrDie(@NotNull String key) {
            assert map.containsKey(key) : "Key not found: " + key;
            return castAny(requireNonNull(getConvertedValueOrNull(key)));
        }

        public @Nullable CharArray getCapturedTextOrNull(@NotNull String key) {
            Extracted extracted = map.get(key);
            return extracted != null ? extracted.captured() : null;
        }

        public @NotNull CharArray getCapturedTextOrDie(@NotNull String key) {
            assert map.containsKey(key) : "Key not found: " + key;
            return castAny(requireNonNull(getCapturedTextOrNull(key)));
        }

        void put(@NotNull String key, @NotNull CharArray captured, @Nullable Object convertedValue) {
            Extracted existing = map.put(key, new Extracted(captured, convertedValue));
            assert existing == null : "Key duplicate: " + key;
        }

        @NotNull Map<String, Extracted> map() {
            return map;
        }

        @Override public String toString() {
            return map.toString();
        }
    }
}
