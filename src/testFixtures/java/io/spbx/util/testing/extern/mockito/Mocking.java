package io.spbx.util.testing.extern.mockito;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.MustBeClosed;
import org.jetbrains.annotations.NotNull;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.ScopedMock;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.InlineMockMaker;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Mocking {
    /* System Properties */

    public static void withSystemProperty(@NotNull String key, @NotNull String value, @NotNull Runnable runnable) {
        String original = System.getProperty(key);
        try {
            System.setProperty(key, value);
            runnable.run();
        } finally {
            System.setProperty(key, original);
        }
    }

    /* Instant */

    public static void withInstantNowFixed(@NotNull Instant instant, @NotNull Runnable runnable) {
        try (ScopedMock ignore = mockInstantNow(instant)) {
            runnable.run();
        } finally {
            clearMock(Instant.class);
        }
    }

    @CheckReturnValue
    @MustBeClosed
    public static @NotNull ScopedMock mockInstantNow(@NotNull Instant fixedInstant) {
        MockedStatic<Instant> mocked = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS);
        mocked.when(Instant::now).thenReturn(fixedInstant);
        return mocked;
    }

    public static Instant nowTruncatedToMillis() {
        Clock clock = Clock.fixed(Instant.now().truncatedTo(ChronoUnit.MILLIS), ZoneId.systemDefault());
        return Instant.now(clock);
    }

    /* Utils */

    public static void clearMock(@NotNull Object mock) {
        if (Mockito.mockingDetails(mock).isMock()) {
            if (Plugins.getMockMaker() instanceof InlineMockMaker inlineMockMaker) {
                inlineMockMaker.clearMock(mock);
            } else {
                Mockito.clearAllCaches();  // last resort
            }
        }
    }
}
