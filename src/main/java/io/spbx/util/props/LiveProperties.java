package io.spbx.util.props;

import io.spbx.util.io.UncheckedClosable;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.ThreadSafe;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import static io.spbx.util.base.Unchecked.Suppliers.runRethrow;
import static io.spbx.util.base.Unchecked.rethrow;

@ThreadSafe
public class LiveProperties implements StandardProperties, UncheckedClosable {
    private static final Logger log = Logger.forEnclosingClass();
    private static final long EVENTS_TOLERANCE_MILLIS = 100;

    private final Path config;
    private final WatchService watchService;
    private final Thread monitorThread;
    private final AtomicReference<Properties> reference = new AtomicReference<>(new Properties());
    private volatile long lastEventMillis;

    protected LiveProperties(@NotNull Path configPath) {
        assert !configPath.toAbsolutePath().toFile().isDirectory() : "Config is a directory: " + configPath;
        config = configPath.normalize().toAbsolutePath();
        watchService = runRethrow(() -> FileSystems.getDefault().newWatchService());
        monitorThread = new Thread(() -> {
            try {
                registerWatchService(config.getParent());
            } catch (InterruptedException e) {
                log.debug().log("File monitor interrupted. %s", this);
            } catch (IOException e) {
                rethrow(e);
            } catch (Throwable e) {
                rethrow(e);
            }
        });
        monitorThread.setDaemon(true);
    }

    public static @NotNull LiveProperties running(@NotNull Path configPath) {
        LiveProperties liveProperties = new LiveProperties(configPath);
        liveProperties.updatePropertiesMap();
        liveProperties.startMonitorThread();
        return liveProperties;
    }

    public static @NotNull LiveProperties idle(@NotNull Path configPath) {
        LiveProperties liveProperties = new LiveProperties(configPath);
        liveProperties.updatePropertiesMap();
        return liveProperties;
    }

    public void startMonitorThread() {
        assert !monitorThread.isAlive() && !monitorThread.isInterrupted() : "Monitor thread already started";
        monitorThread.start();
    }

    @Override
    public void close() {
        if (!monitorThread.isAlive()) {
            return;
        }
        log.debug().log("Closing %s ...", this);
        try {
            monitorThread.interrupt();
            watchService.close();
        } catch (IOException e) {
            rethrow(e);
        }
    }

    @Override
    public @Nullable String getOrNull(@NotNull String key) {
        String value = reference.get().getProperty(key);
        return value != null ? value : System.getProperty(key);
    }

    private void registerWatchService(@NotNull Path directory) throws IOException, InterruptedException {
        log.info().log("Starting file monitor %s ...", this);

        directory.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                onEvent(event.kind(), event.context());
            }
            if (!key.reset()) {
                log.info().log("Key is invalid: %s", key);
                return;
            }
        }
    }

    private void onEvent(@NotNull WatchEvent.Kind<?> kind, @NotNull Object context) {
        log.debug().log("WatchEvent.Kind=%s. Context=%s", kind, context);
        if (context instanceof Path path && isConfigPath(path) && checkIn()) {
            updatePropertiesMap();
        }
    }

    private boolean checkIn() {
        long now = System.currentTimeMillis();
        if (now - lastEventMillis >= EVENTS_TOLERANCE_MILLIS) {
            lastEventMillis = now;
            return true;
        }
        return false;
    }

    private boolean isConfigPath(@NotNull Path path) {
        return path.getFileName().equals(config.getFileName()) &&
               path.normalize().toAbsolutePath().equals(config);
    }

    protected void updatePropertiesMap() {
        try {
            if (config.toFile().exists()) {
                Properties properties = readPropertiesMap();
                reference.set(properties);
                log.debug().log("Properties updated: size=%d", properties.size());
            } else {
                log.debug().log("Properties file not found: %s", config);
            }
        } catch (IOException e) {
            log.warn().withCause(e).log("Failed to update properties: %s", e.getMessage());
        }
    }

    private @NotNull Properties readPropertiesMap() throws IOException {
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(config)) {
            properties.load(reader);
        }
        return properties;
    }

    @Override
    public String toString() {
        return "LiveConfig: `%s`".formatted(config);
    }
}
