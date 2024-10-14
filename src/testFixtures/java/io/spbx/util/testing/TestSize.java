package io.spbx.util.testing;

import io.spbx.util.props.PropertyMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static io.spbx.util.base.BasicNulls.firstNonNull;
import static io.spbx.util.base.Unchecked.Suppliers.runQuietlyOrNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public enum TestSize {
    FAST(0),
    MEDIUM(10),
    SLOW(20),
    STRESS(50),
    STRESS_THOROUGH(100);

    private final int value;

    TestSize(int value) {
        this.value = value;
    }

    public static void assumeTestSize(@NotNull TestSize actualSize, @Nullable Object @NotNull... params) {
        assumeTrue(actualSize.value <= getTestSizeFromPropsOrDefaultForCurrentLaunch().value,
                   () -> "The test is skipped because it's too big for this run: %s".formatted(Arrays.toString(params)));
    }

    public static @Nullable TestSize getTestSizeFromProps() {
        String property = PropertyMap.system().getOrNull("test.size");
        return property != null ? runQuietlyOrNull(() -> TestSize.valueOf(property.trim().toUpperCase())) : null;
    }

    public static @NotNull TestSize getTestSizeFromPropsOrDefault(@NotNull TestSize testSize) {
        return firstNonNull(getTestSizeFromProps(), testSize);
    }

    public static @NotNull TestSize getTestSizeFromPropsOrDefaultForCurrentLaunch() {
        // By default, when a single class/method is launched, run all tests.
        return firstNonNull(getTestSizeFromProps(), () -> switch (JUnits.getJUnitLaunch()) {
            case SINGLE_METHOD, SINGLE_CLASS -> STRESS;     // run stress tests for a targeted test launch
            case SINGLE_PACKAGE, OTHER -> FAST;             // ignore stress tests for a wide test launch
            case UNKNOWN -> STRESS;                         // include stress tests for all other launches
        });
    }
}
