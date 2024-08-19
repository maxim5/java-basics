# Basics Java Utils

## Features

- Boilerplate-less streams

```java
Streamer.of(1, 2, 3).toArrayList();                             // 1, 2, 3
Streamer.of(1, 2, 3).join(" ");                                 // "1 2 3"
Streamer.of(Map.of(1, 2)).mapKeys(String::valueOf).toMap();     // "1" -> 2
Streamer.of(iterable).skipNulls().toNativeArray();              // array without nulls
Streamer.of(list).toAtMostTwo();                                // returns 0, 1 or 2 elements or throws
```

- `DataSize` for size units (Kb, Mb, Gb, ...)

```java
DataSize.parse("123KB").toBytes();              // 125952
DataSize.ofBytes(65536).toString();             // 64Kb
DataSize.ofKilobytes(50000).toString();         // 48.83Mb
DataSize.ofGigabytes(1).toString(Unit.Byte);    // 1073741824b
```

- `Int128` for efficient 128-bit arithmetic

```java
Int128 value = Int128.from("9619122375485128076391017781203171");
BigInteger big = value.toBigInteger();  // 9619122375485128076391017781203171
Int128.from(1L << 62).multiply(10);     // 46116860184273879040
Int128.MAX_VALUE.toString();            // 170141183460469231731687303715884105727
Int128.MIN_VALUE.toHexString();         // 80000000000000000000000000000000
```

- Boilerplate-less exceptions

```java
IllegalArgumentExceptions.assure(value == null, "Key already present: %s", key);
IllegalStateExceptions.failIf(value.isTerminal(), "%s: Expected non-terminal, but got %s", this, value);
assert value >= 0 : newInternalError("`value=%s` is negative", value);

public Shell getAdminShell() {
    throw notImplemented("getAdminShell() unsupported for engine: %s", table.engine());
}
```

## Setting up Gradle Dependency

```
repositories {
    mavenCentral()
    ...
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    classpath("com.github.maxim5:java-basics:0.1.1")
}
```
