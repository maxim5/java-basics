package io.spbx.util.collect.pool;

import com.google.common.collect.Sets;
import io.spbx.util.collect.pool.ExpirablePool.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class ExpirablePoolTest {
    @Test
    public void take_two() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertPool(pool).containsAvailable("foo", "bar");

        assertThat(pool.nextAvailable()).isEqualTo("foo");
        assertPool(pool).containsAvailable("bar");

        assertThat(pool.nextAvailable()).isEqualTo("bar");
        assertPool(pool).allTaken();
    }

    @Test
    public void take_one_and_return_back_success() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackSuccess("foo");
        assertPool(pool).containsAvailable("foo", "bar");
    }

    @Test
    public void take_one_and_return_back_expired() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackExpired("foo");
        assertPool(pool).containsAvailable("bar");
    }

    @Test
    public void take_both_and_return_back_both() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        pool.returnBackSuccess("foo");
        pool.returnBackSuccess("bar");
        assertPool(pool).containsAvailable("foo", "bar");
    }

    @Test
    public void take_both_and_return_back_both_different_order() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackSuccess("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        pool.returnBackSuccess("bar");
        assertPool(pool).containsAvailable("foo", "bar");
    }

    @Test
    public void take_one_return_success_and_take_again() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackSuccess("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        assertPool(pool).containsAvailable("foo");
    }

    @Test
    public void take_one_return_expired_and_take_again() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");
        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackExpired("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        assertPool(pool).allTaken();
    }

    @Test
    public void rotation() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar");

        assertThat(pool.nextAvailable()).isEqualTo("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        pool.returnBackSuccess("bar");
        pool.returnBackSuccess("foo");

        assertThat(pool.nextAvailable()).isEqualTo("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        pool.returnBackSuccess("bar");
        pool.returnBackSuccess("foo");

        assertThat(pool.nextAvailable()).isEqualTo("foo");
        pool.returnBackSuccess("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        pool.returnBackSuccess("bar");
    }

    @Test
    public void complex_take_return_expire() {
        ExpirablePool<String> pool = ExpirablePool.of("foo", "bar", "baz");
        assertPool(pool).containsAvailable("foo", "bar", "baz");

        assertThat(pool.nextAvailable()).isEqualTo("foo");
        assertThat(pool.nextAvailable()).isEqualTo("bar");
        assertPool(pool).containsAvailable("baz");

        pool.returnBackExpired("foo");
        assertPool(pool).containsAvailable("baz");

        assertThat(pool.nextAvailable()).isEqualTo("baz");
        assertPool(pool).allTaken();

        pool.returnBackExpired("baz");
        assertPool(pool).allTaken();

        pool.returnBackSuccess("bar");
        assertPool(pool).containsAvailable("bar");

        assertThat(pool.nextAvailable()).isEqualTo("bar");
        assertPool(pool).allTaken();

        pool.returnBackExpired("bar");
        assertPool(pool).allTaken();
    }

    @Test
    public void complex_ultimate() {
        Set<String> all = Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g");
        ExpirablePool<String> pool = ExpirablePool.of(all);
        LinkedList<String> taken = new LinkedList<>();
        Set<String> expired = HashSet.newHashSet(pool.total());
        Random random = new Random(0);
        for (int i = 0; i < 400; i++) {
            switch (random.nextInt(10)) {
                case 0, 1, 2, 3 -> {
                    if (pool.hasAvailable()) {
                        String s = pool.nextAvailable();
                        assertThat(pool.getStats(s).status()).isEqualTo(Status.WORKING);
                        assertThat(pool.getStats(s).taken()).isTrue();
                        assertThat(expired.contains(s)).isFalse();
                        assertThat(taken.contains(s)).isFalse();
                        taken.add(s);
                    }
                }
                case 4, 5, 6, 7, 8 -> {
                    if (!taken.isEmpty()) {
                        String s = taken.remove();
                        assertThat(pool.getStats(s).status()).isEqualTo(Status.WORKING);
                        assertThat(pool.getStats(s).taken()).isTrue();
                        assertThat(expired.contains(s)).isFalse();
                        pool.returnBackSuccess(s);
                        assertThat(pool.getStats(s).taken()).isFalse();
                    }
                }
                case 9 -> {
                    if (!taken.isEmpty()) {
                        String s = taken.remove();
                        assertThat(pool.getStats(s).status()).isEqualTo(Status.WORKING);
                        assertThat(pool.getStats(s).taken()).isTrue();
                        assertThat(expired.contains(s)).isFalse();
                        pool.returnBackExpired(s);
                        assertThat(expired.add(s)).isTrue();
                        assertThat(pool.getStats(s).status()).isEqualTo(Status.EXPIRED);
                    }
                }
            }

            assertThat(pool.taken()).isEqualTo(taken.size());
            assertThat(pool.allWorking()).containsExactlyElementsIn(Sets.difference(all, expired));
            assertThat(pool.allAvailable()).containsExactlyElementsIn(Sets.difference(all, Sets.union(expired, new HashSet<>(taken))));

            if (!pool.hasWorking()) {
                return;
            }
        }
    }

    private static @NotNull ExpirablePoolSubject assertPool(@NotNull ExpirablePool<String> pool) {
        return new ExpirablePoolSubject(pool);
    }

    private record ExpirablePoolSubject(@NotNull ExpirablePool<String> pool) {
        public void containsAvailable(@NotNull String @NotNull ... expected) {
            assertThat(pool.allAvailable()).containsExactlyElementsIn(expected);
            assertThat(pool.hasAvailable()).isEqualTo(expected.length > 0);
            assertThat(pool.available()).isEqualTo(expected.length);
            assertThat(pool.taken()).isEqualTo(pool.working() - expected.length);
            for (String s : expected) {
                assertThat(pool.getStats(s).status()).isEqualTo(Status.WORKING);
            }
        }

        public void allTaken() {
            containsAvailable();
            assertThrows(IllegalStateException.class, () -> pool.nextAvailable());
        }
    }
}
