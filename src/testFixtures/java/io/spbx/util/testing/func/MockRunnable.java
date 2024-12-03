package io.spbx.util.testing.func;

import io.spbx.util.func.ThrowRunnable;

public class MockRunnable implements Runnable, ThrowRunnable<RuntimeException> {
    private int times;

    @Override
    public void run() {
        times++;
    }

    public int timesCalled() {
        return times;
    }
}
