package io.spbx.util.props;

import io.spbx.util.base.lang.DataSize;
import io.spbx.util.base.math.Int128;
import io.spbx.util.base.str.BasicJoin;
import io.spbx.util.base.tuple.Pair;
import io.spbx.util.func.Reversibles;
import io.spbx.util.io.BasicNet;
import io.spbx.util.security.Secret;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.spbx.util.base.error.Unchecked.Suppliers.runRethrow;
import static io.spbx.util.base.lang.BasicNulls.firstNonNullIfExists;
import static io.spbx.util.base.lang.EasyCast.castAny;

public interface StandardProperties extends PropertyMap {
    /* Standard `TypeExtractor`s */

    TypeExtractor<Path> PATH = Path::of;
    TypeExtractor<List<Path>> PATHS = ListExtractor.separatedBy(File.pathSeparator).map(Reversibles.extendToList(PATH));
    TypeExtractor<Secret> SECRET = TypeExtractor.of(Secret::ofAscii, Secret::toHumanReadableAsciiString);
    TypeExtractor<DataSize> DATA_SIZE = DataSize::parse;
    TypeExtractor<Int128> INT128 = Int128::from;
    TypeExtractor<BigInteger> BIG_INTEGER = BigInteger::new;
    TypeExtractor<BigDecimal> BIG_DECIMAL = BigDecimal::new;
    TypeExtractor<InetAddress> INET_ADDRESS = BasicNet::parseInetAddress;
    TypeExtractor<Inet4Address> IP4_ADDRESS = BasicNet::parseIp4Address;
    TypeExtractor<Inet6Address> IP6_ADDRESS = BasicNet::parseIp6Address;
    TypeExtractor<URI> URI = java.net.URI::create;
    TypeExtractor<URL> URL = StandardProperties::parseUrl;
    TypeExtractor<Duration> DURATION = Duration::parse;
    TypeExtractor<Instant> INSTANT = Instant::parse;
    TypeExtractor<LocalTime> LOCAL_TIME = LocalTime::parse;
    TypeExtractor<LocalDate> LOCAL_DATE = LocalDate::parse;
    TypeExtractor<LocalDateTime> LOCAL_DATE_TIME = LocalDateTime::parse;

    record ListExtractor(@NotNull String separator) implements TypeExtractor<List<String>> {
        public static @NotNull ListExtractor separatedBy(@NotNull String separator) {
            return new ListExtractor(separator);
        }
        @Override public @NotNull List<String> forward(@NotNull String str) {
            return Stream.of(str.split(separator)).toList();
        }
        @Override public @NotNull String backward(@NotNull List<String> list) {
            return BasicJoin.of(list).join(separator);
        }
    }

    record PairExtractor(@NotNull String separator) implements TypeExtractor<Pair<String, String>> {
        public static @NotNull PairExtractor separatedBy(@NotNull String separator) {
            return new PairExtractor(separator);
        }
        @Override public @NotNull Pair<String, String> forward(@NotNull String str) {
            return Pair.from(str.split(separator));
        }
        @Override public @NotNull String backward(@NotNull Pair<String, String> pair) {
            return BasicJoin.of(pair).join(separator);
        }
    }

    /* Specialized shortcuts for getters */

    default <T extends Enum<T>> @NotNull T getEnum(@NotNull String key, @NotNull T def) {
        return getOptional(key).map(property -> toEnum(property, def)).orElse(def);
    }
    default <T extends Enum<T>> @NotNull T getEnum(@NotNull EnumProperty<T> property) {
        return getEnum(property.key(), property.def());
    }
    default <T extends Enum<T>> @Nullable T getEnumOrNull(@NotNull String key, @NotNull Function<String, T> valueOf) {
        return getOptional(key).map(property -> toEnum(property, valueOf)).orElse(null);
    }

    default @NotNull Path getPath(@NotNull PropertyKeyDefault<Path> property) {
        return via(PATH).get(property);
    }
    default @NotNull List<Path> getPaths(@NotNull PropertyKeyDefault<List<Path>> property) {
        return via(PATHS).get(property);
    }
    default @NotNull Secret getSecret(@NotNull PropertyKeyDefault<Secret> property) {
        return via(SECRET).get(property);
    }
    default @NotNull Duration getDuration(@NotNull PropertyKeyDefault<Duration> property) {
        return via(DURATION).get(property);
    }

    /* Standard `Property` classes */

    record EnumProperty<T extends Enum<T>>(@NotNull String key, @NotNull T def) implements PropertyKeyDefault<T> {
        public static <T extends Enum<T>> @NotNull EnumProperty<T> of(@NotNull String key, @NotNull T def) {
            return new EnumProperty<>(key, def);
        }
    }

    record PathProperty(@NotNull String key, @NotNull Path def) implements PropertyKeyDefault<Path> {
        public static @NotNull PathProperty of(@NotNull String key, @NotNull Path def) {
            return new PathProperty(key, def);
        }
        public @NotNull MultiPathProperty toMulti() {
            return MultiPathProperty.of(key, List.of(def));
        }
    }

    record SecretProperty(@NotNull String key, @NotNull Secret def) implements PropertyKeyDefault<Secret> {
        public static @NotNull SecretProperty of(@NotNull String key, @NotNull Secret def) {
            return new SecretProperty(key, def);
        }
    }

    record MultiPathProperty(@NotNull String key, @NotNull List<Path> def) implements PropertyKeyDefault<List<Path>> {
        public static @NotNull MultiPathProperty of(@NotNull String key, @NotNull List<Path> def) {
            return new MultiPathProperty(key, def);
        }
    }

    record DurationProperty(@NotNull String key, @NotNull Duration def) implements PropertyKeyDefault<Duration> {
        public static @NotNull DurationProperty of(@NotNull String key, @NotNull Duration def) {
            return new DurationProperty(key, def);
        }
    }

    /* Standardization */

    static @NotNull StandardProperties system() {
        return System::getProperty;
    }

    static @NotNull StandardProperties env() {
        return System::getenv;
    }

    @Override default @NotNull StandardProperties chainedWith(@NotNull PropertyMap backup) {
        return key -> firstNonNullIfExists(this.getOrNull(key), () -> backup.getOrNull(key));
    }

    @Override default @NotNull StandardProperties standardize() {
        return this;
    }

    static @NotNull StandardProperties standardize(@NotNull PropertyMap propertyMap) {
        return propertyMap instanceof StandardProperties standard ? standard : propertyMap::getOrNull;
    }

    /* Implementation details */

    private static <T extends Enum<T>> @NotNull T toEnum(@NotNull String property, @NotNull T def) {
        Class<T> enumClass = castAny(def.getClass());
        T[] enumConstants = enumClass.getEnumConstants();
        Optional<T> exactMatch = Stream.of(enumConstants).filter(v -> v.name().equals(property)).findFirst();
        if (exactMatch.isPresent()) {
            return exactMatch.get();
        }
        Optional<T> closeMatch = Stream.of(enumConstants).filter(v -> v.name().equalsIgnoreCase(property)).findFirst();
        return closeMatch.orElse(def);
    }

    private static <T extends Enum<T>> @Nullable T toEnum(@NotNull String property, @NotNull Function<String, T> valueOf) {
        try {
            return valueOf.apply(property.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static @NotNull URL parseUrl(@NotNull String url) {
        return runRethrow(() -> java.net.URI.create(url).toURL());
    }
}
