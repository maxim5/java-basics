package io.spbx.util.collect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.spbx.util.base.BasicExceptions.InternalErrors;
import io.spbx.util.base.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static io.spbx.util.testing.TestingBasics.listOf;

class TestingMapCollectors {
    public static final Pattern DUP_KEY_ERR = Pattern.compile("Duplicate values `.*` and `.*` for the same key `.*`");
    public static final Pattern DUP_KEY_ERR_GUAVA = Pattern.compile("Multiple entries with same key: .*=.* and .*=.*");
    public static final Pattern DUP_VAL_ERR_GUAVA = Pattern.compile("Multiple entries with same value: .*=.* and .*=.*");
    public static final Pattern DUP_KEY_ERR_BIMAP = Pattern.compile("key already present: .*");
    public static final Pattern DUP_VAL_ERR_BIMAP = Pattern.compile("value already present: .*");

    public enum InputMapCase {
        EMPTY,
        ONE_PAIR(Pair.of(1, 2)),
        TWO_PAIRS(Pair.of(1, 2), Pair.of(3, 4)),
        MANY_PAIRS(Pair.of(1, 2), Pair.of(3, 4), Pair.of(5, 6)),
        NULL_KEYS(Pair.of(null, 2)),
        NULL_VALUES(Pair.of(1, null)),
        DUPLICATE_KEYS(Pair.of(1, 2), Pair.of(1, 4)),
        DUPLICATE_VALUES(Pair.of(1, 2), Pair.of(3, 2));

        private final List<Pair<Integer, Integer>> pairs;

        @SafeVarargs InputMapCase(@NotNull Pair<Integer, Integer> ... pairs) {
            this.pairs = listOf(pairs);
        }

        public boolean hasNulls() {
            return this == NULL_KEYS || this == NULL_VALUES;
        }

        public boolean hasDuplicateKeys() {
            return this == DUPLICATE_KEYS;
        }

        public boolean hasDuplicateValues() {
            return this == DUPLICATE_VALUES;
        }

        public @NotNull List<Pair<Integer, Integer>> pairs() {
            return pairs;
        }

        public @NotNull Map<Integer, Integer> map() {
            return map(Merge.FAIL);
        }

        public @NotNull Map<Integer, Integer> map(@NotNull Merge merge) {
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            pairs.forEach(pair -> {
                switch (merge) {
                    case IGNORE -> hashMap.putIfAbsent(pair.first(), pair.second());
                    case OVERWRITE -> hashMap.put(pair.first(), pair.second());
                    case FAIL -> InternalErrors.assure(hashMap.put(pair.first(), pair.second()) == null,
                                                       "Merge collisions are not expected in this test");
                }
            });
            return hashMap;
        }

        public @NotNull Map<Integer, Pair<Integer, Integer>> mapToPairs() {
            HashMap<Integer, Pair<Integer, Integer>> hashMap = new HashMap<>();
            pairs.forEach(pair -> hashMap.put(pair.first(), pair));
            return hashMap;
        }

        public @NotNull Multimap<Integer, Integer> multimap() {
            HashMultimap<Integer, Integer> hashMap = HashMultimap.create();
            pairs.forEach(pair -> hashMap.put(pair.first(), pair.second()));
            return hashMap;
        }
    }

    public enum Merge {
        IGNORE,
        OVERWRITE,
        FAIL,
    }
}
