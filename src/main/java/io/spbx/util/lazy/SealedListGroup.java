package io.spbx.util.lazy;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SealedListGroup implements SealedGroup {
    private final Sealed<List<Sealed<?>>> list = AtomicSealed.create(new ArrayList<>());

    public static @NotNull SealedGroup empty() {
        return new SealedListGroup();
    }

    @Override
    public boolean isSealed() {
        return list.isSealed();
    }

    @Override
    public void sealAllOrDie() {
        list.finalizeAndSealOrDie(ImmutableList::copyOf);
        for (Sealed<?> sealed : this) {
            sealed.sealIfNotYet();
        }
    }

    @Override
    public void sealAllIfNotYet() {
        list.finalizeAndSealIfNotYet(ImmutableList::copyOf);
        for (Sealed<?> sealed : this) {
            sealed.sealIfNotYet();
        }
    }

    @Override
    public <T> void addToGroup(@NotNull Sealed<T> sealed) {
        assert !isSealed() : "Invalid state. %s is already sealed: %s".formatted(getClass().getSimpleName(), list.get());
        list.get().add(sealed);
    }

    @Override
    public @NotNull <T> Sealed<T> createAndAdd(@Nullable T initialValue) {
        Sealed<T> result = AtomicSealed.create(initialValue);
        addToGroup(result);
        return result;
    }

    @Override
    public @NotNull Iterator<Sealed<?>> iterator() {
        return list.get().iterator();
    }
}
