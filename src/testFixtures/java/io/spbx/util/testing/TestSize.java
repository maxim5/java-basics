package io.spbx.util.testing;

import io.spbx.util.props.StandardProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

import static io.spbx.util.base.lang.BasicNulls.firstNonNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Immutable
public enum TestSize {
    FAST(0),            // <= 100ms
    MEDIUM(10),         // <= 500ms
    SLOW(20),           // <= 5sec
    EXTENDED(50),       // <= 30sec
    ENDLESS(100);       // no limit

    private final int value;

    TestSize(int value) {
        this.value = value;
    }

    public static void assumeTestSize(@NotNull TestSize actualSize, @Nullable Object @NotNull... params) {
        assumeTrue(actualSize.value <= getTestSizeFromPropsOrDefaultForCurrentLaunch().value,
                   () -> "The test is skipped because it's too big for this run: %s".formatted(Arrays.toString(params)));
    }

    public static @Nullable TestSize getTestSizeFromProps() {
        return StandardProperties.system().getEnumOrNull("test.size", property -> TestSize.valueOf(property.toUpperCase()));
    }

    public static @NotNull TestSize getTestSizeFromPropsOrDefault(@NotNull TestSize testSize) {
        return firstNonNull(getTestSizeFromProps(), testSize);
    }

    public static @NotNull TestSize getTestSizeFromPropsOrDefaultForCurrentLaunch() {
        // By default, when a single class/method is launched, run all tests.
        return firstNonNull(getTestSizeFromProps(), () -> switch (JUnits.getJUnitLaunch()) {
            case SINGLE_METHOD, SINGLE_CLASS -> EXTENDED;   // run EXTENDED tests for a targeted test launch
            case SINGLE_PACKAGE, OTHER -> SLOW;             // ignore EXTENDED tests for a wide test launch
            case UNKNOWN -> EXTENDED;                       // include EXTENDED tests for all other launches
        });
    }
}
