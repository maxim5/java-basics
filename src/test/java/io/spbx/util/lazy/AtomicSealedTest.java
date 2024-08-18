package io.spbx.util.lazy;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.spbx.util.func.Chains;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

@Tag("fast")
public class AtomicSealedTest {
    public static final Integer NULL = null;

    @Test
    public void initial_value() {
        assertSealed(AtomicSealed.create(NULL)).isNotSealed().contains(null);
        assertSealed(AtomicSealed.create(123)).isNotSealed().contains(123);
    }

    @Test
    public void seal_or_die() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.sealOrDie();
        assertSealed(seal).isSealed().contains(123);
    }

    @Test
    public void finalize_and_seal_or_die() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.finalizeAndSealOrDie(n -> n + 1);
        assertSealed(seal).isSealed().contains(124);
    }

    @Test
    public void seal_if_not_yet() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.sealIfNotYet(125);
        assertSealed(seal).isSealed().contains(125);
    }

    @Test
    public void seal_current_if_not_yet() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.sealIfNotYet();
        assertSealed(seal).isSealed().contains(123);
    }

    @Test
    public void finalize_and_seal_if_not_yet() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.finalizeAndSealIfNotYet(n -> n + 1);
        assertSealed(seal).isSealed().contains(124);
    }

    @Test
    public void compare_and_seal() {
        AtomicSealed<Integer> seal = new AtomicSealed<>(123);
        assertSealed(seal).isNotSealed();
        seal.compareAndSeal(124);
        assertSealed(seal).isSealed().contains(124);
    }

    @CheckReturnValue
    private static <T> @NotNull SealedSubject<T> assertSealed(@NotNull Sealed<T> seal) {
        return new SealedSubject<>(seal);
    }

    @CanIgnoreReturnValue
    private record SealedSubject<T>(@NotNull Sealed<T> seal) {
        public @NotNull SealedSubject<T> contains(T expected) {
            assertThat(seal.get()).isEqualTo(expected);
            return this;
        }

        public @NotNull SealedSubject<T> isSealed() {
            assertThat(seal.isSealed()).isTrue();
            trySealOrDie();
            trySealIfNotYet();
            tryCompareAndSeal();
            return this;
        }

        public @NotNull SealedSubject<T> isNotSealed() {
            assertThat(seal.isSealed()).isFalse();
            return this;
        }

        private void trySealOrDie() {
            assertThrows(AssertionError.class, () -> seal.sealOrDie());
            assertThrows(AssertionError.class, () -> seal.sealOrDie(null));
            assertThrows(AssertionError.class, () -> seal.finalizeAndSealOrDie(t -> t));
        }

        private void trySealIfNotYet() {
            T current = seal.get();
            seal.sealIfNotYet();
            contains(current);
            seal.sealIfNotYet(null);
            contains(current);
            seal.finalizeAndSealIfNotYet(Chains.constant(null));
            contains(current);
        }

        private void tryCompareAndSeal() {
            T current = seal.get();
            seal.compareAndSeal(current);
            contains(current);
            assertThrows(AssertionError.class, () -> seal.compareAndSeal(null));
        }
    }
}
