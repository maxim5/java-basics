package io.spbx.util.lazy;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.collect.iter.BasicIterables;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

@Tag("fast")
public class SealedListGroupTest {
    @Test
    public void add_to_group_simple() {
        SealedGroup sealedGroup = SealedListGroup.empty();
        assertSealedGroup(sealedGroup).isNotSealed().containsExactly();

        Sealed<Integer> one = sealedGroup.createAndAdd(1);
        assertThat(one.isSealed()).isFalse();
        assertSealedGroup(sealedGroup).isNotSealed().containsExactly(1);

        Sealed<String> another = AtomicSealed.create("foo");
        assertThat(another.isSealed()).isFalse();
        sealedGroup.addToGroup(another);
        assertSealedGroup(sealedGroup).isNotSealed().containsExactly(1, "foo");

        one.sealOrDie(2);
        assertSealedGroup(sealedGroup).isNotSealed().containsExactly(2, "foo");
    }

    @Test
    public void seal_all_or_die() {
        SealedGroup sealedGroup = SealedListGroup.empty();
        sealedGroup.createAndAdd(1);
        sealedGroup.createAndAdd("foo").sealOrDie("bar");
        sealedGroup.sealAllOrDie();
        assertSealedGroup(sealedGroup).isSealed().containsExactly(1, "bar");
    }

    @Test
    public void seal_all_if_not_already() {
        SealedGroup sealedGroup = SealedListGroup.empty();
        sealedGroup.createAndAdd(1);
        sealedGroup.createAndAdd("foo").sealIfNotYet("bar");
        sealedGroup.sealAllIfNotYet();
        assertSealedGroup(sealedGroup).isSealed().containsExactly(1, "bar");
    }

    @CheckReturnValue
    private static @NotNull SealedGroupSubject assertSealedGroup(@NotNull SealedGroup group) {
        return new SealedGroupSubject(group);
    }

    @CanIgnoreReturnValue
    private record SealedGroupSubject(@NotNull SealedGroup group) {
        public @NotNull SealedGroupSubject containsExactly(@NotNull Object @NotNull ... expected) {
            assertThat(currentContents()).containsExactlyElementsIn(expected).inOrder();
            return this;
        }

        public @NotNull SealedGroupSubject containsExactly(@NotNull List<?> expected) {
            assertThat(currentContents()).containsExactlyElementsIn(expected).inOrder();
            return this;
        }

        public @NotNull SealedGroupSubject isSealed() {
            assertThat(group.isSealed()).isTrue();
            for (Sealed<?> sealed : group) {
                assertThat(sealed.isSealed()).isTrue();
            }
            trySealOrDie();
            trySealIfNotYet();
            tryToAdd();
            return this;
        }

        public @NotNull SealedGroupSubject isNotSealed() {
            assertThat(group.isSealed()).isFalse();
            return this;
        }

        private void trySealOrDie() {
            assertThrows(AssertionError.class, () -> group.sealAllOrDie());
            for (Sealed<?> sealed : group) {
                assertThrows(AssertionError.class, () -> sealed.sealOrDie());
                assertThrows(AssertionError.class, () -> sealed.sealOrDie(null));
                assertThrows(AssertionError.class, () -> sealed.finalizeAndSealOrDie(t -> t));
            }
        }

        private void trySealIfNotYet() {
            List<?> current = currentContents();
            group.sealAllIfNotYet();
            containsExactly(current);
            group.sealAllIfNotYet();
            containsExactly(current);
        }

        private void tryToAdd() {
            assertThrows(AssertionError.class, () -> group.createAndAdd(null));
            assertThrows(AssertionError.class, () -> group.addToGroup(AtomicSealed.create(null)));
        }

        private @NotNull List<?> currentContents() {
            return BasicIterables.asList(group).stream().map(Supplier::get).toList();
        }
    }
}
