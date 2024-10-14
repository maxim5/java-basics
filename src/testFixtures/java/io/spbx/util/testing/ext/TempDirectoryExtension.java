package io.spbx.util.testing.ext;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import io.spbx.util.logging.Logger;
import io.spbx.util.text.BasicJoin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TempDirectoryExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
    private static final Logger log = Logger.forEnclosingClass();

    private final boolean cleanUp;
    private final boolean onlySuccessful;
    private final List<Path> pathsToClean;
    private Path currentTempDir;

    public TempDirectoryExtension(boolean cleanUp, boolean onlySuccessful) {
        this.cleanUp = cleanUp;
        this.onlySuccessful = onlySuccessful;
        pathsToClean = cleanUp ? new ArrayList<>(1024) : List.of();
    }

    public static @NotNull TempDirectoryExtension withCleanup() {
        return new TempDirectoryExtension(true, false);
    }

    public static @NotNull TempDirectoryExtension withoutCleanup() {
        return new TempDirectoryExtension(false, false);
    }

    public static @NotNull TempDirectoryExtension withCleanupOnlySuccessful() {
        return new TempDirectoryExtension(true, true);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (cleanUp) {
            cleanUpAtExit();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        String className = context.getRequiredTestClass().getSimpleName();
        String testName = context.getRequiredTestMethod().getName();

        Object[] arguments = JUnitExtensions.extractInvocationArguments(context);
        String format = arguments == null || arguments.length == 0 ?
            "%s.%s.".formatted(className, testName) :
            "%s.%s_%s.".formatted(className, testName, BasicJoin.of(arguments).join("_"));

        currentTempDir = Files.createTempDirectory(format);
        log.info().log("Temp storage path for this test: %s", currentTempDir);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        cleanUp(currentTempDir, context.getExecutionException().isEmpty());
    }

    public @NotNull Path currentTempDir() {
        return currentTempDir;
    }

    private void cleanUp(@NotNull Path path, boolean isSuccess) {
        if (cleanUp && (!onlySuccessful || isSuccess)) {
            pathsToClean.add(path);
            log.debug().log("Adding path to clean-up: %s", path);
        }
    }

    private static void deleteAll(@NotNull Path path) {
        log.debug().log("Cleaning-up temp path %s", path);
        try {
            MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE);
        } catch (Exception e) {
            log.info().withCause(e).log("Clean-up did not complete successfully");
        }
    }

    private void cleanUpAtExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pathsToClean.forEach(TempDirectoryExtension::deleteAll)));
    }
}
