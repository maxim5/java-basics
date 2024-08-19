# Basic Java Utils

## Features

- Concise streams

```java
Streamer.of(1, 2, 3, null).toArrayList();                 // [1, 2, 3, null]
Streamer.repeat("abc", 3).join(" ");                      // "abc abc abc"
Streamer.of(iterable).skipNulls().toNativeArray();        // native array without nulls

Streamer.of(mapOf(1, 2)).mapKeys(String::valueOf).toMap() // {"1" -> 2}
Streamer.zip(keys, values).toLinkedHashMap();             // LinkedHashMap of keys -> values

Streamer.of(list).toAtMostTwo();                          // get 0, 1 or 2 elements or throw
Streamer.of(array).toExactlyTow();                        // get exactly 2 elements or throw
```

- Non-verbose exceptions

```java
// Concise assertions
IllegalArgumentExceptions.assure(value == null, "Key already present: %s", key);
IllegalStateExceptions.failIf(state.done(), "Invalid `%s` state: %s", this, state);
assert arg > 0 : newInternalError("`arg=%s` must be positive", arg);

// One-liners without checked exceptions
List<String> lines = runRethrow(() -> Files.readAllLines(path));
new Thread(rethrow(() -> runServer(port)));

// Unchecked throw of any throwable
public void fail() {
    Unchecked.throwAny(new IOException("End of file"));
}

// Concise NotImplemented
public Shell getAdminShell() {
    throw notImplemented("getAdminShell() unsupported for engine: %s", table.engine());
}
```

- `Pair`, `Triple` and other convenient `Tuple`s

```java
Pair.of(1, 2).mapFirst(i -> i * 3);                         // (3, 2)
Pair.from("1:2".split(":"));                                // ("1", "2")
Pair.of(1, 2).toTripleWith(3);                              // (1, 2, 3)
Triple.of(1, "2", '3').map(i -> -i, s -> "0", ch -> '4');   // (-1, "0", '4')
OneOf.ofFirst(1).hasSecond();                               // false
Pair.of(1, null).toOneOf();                                 // (1, null)
```

- Convenient builders

```java
ListBuilder.of(1, 2, 3).add(4).addAll(5, 6).toArrayList();      // [1, 2, 3, 4, 5, 6]
ListBuilder.of().addAll(iterable).skipNulls().toBasicsTuple();  // Tuple without nulls
MapBuilder.builder().put(1, 2).overwrite(1, 3).toTreeMap();     // {1: 3}
MapBuilder.copyOf(map).overwrite(1, 2).bimaps().toGuavaBiMap(); // Guava BiMap
```

- `DataSize` for size units (Kb, Mb, Gb, ...)

```java
DataSize.parse("123.0KB").toBytes();            // 125952
DataSize.ofBytes(65536).toString();             // 64Kb
DataSize.ofKilobytes(50000).toString();         // 48.83Mb
DataSize.ofGigabytes(1).toString(Unit.Byte);    // 1073741824b
```

- `Int128` for near native efficient 128-bit arithmetic

```java
Int128 value = Int128.from("9619122375485128076391017781203171");
BigInteger big = value.toBigInteger();      // 9619122375485128076391017781203171
Int128.from(1L << 62).multiply(10);         // 46116860184273879040
Int128.MAX_VALUE.toString();                // 170141183460469231731687303715884105727
Int128.MIN_VALUE.toHexString();             // 80000000000000000000000000000000
```

- Simple text processing
```java
BasicJoin.of(1, 2, null).join(',');                 // "1,2,"
BasicJoin.of(1, 2, "").onlyNonEmpty().join(", ");   // "1, 2"
BasicSplit.of("foo.bar.").exactly(3).on('.');       // ["foo", "bar", ""]
BasicSplit.of("foo.bar.").skipEmpty().on(".");      // ["foo", "bar"]
BasicParsing.parseIntSafe(str, -1);                 // never throws, falls back to -1
```

- Tabular data formatting
```java
Tabular<String> tab = ArrayTabular.of(
    arrayOf("foo", "bar"),
    arrayOf("foo", ""),
    arrayOf("1", "123456")
);
ASCII_FORMATTER.formatIntoTableString(tab);
/*
 ----------------
 | foo | bar    |
 ----------------
 | foo |        |
 ----------------
 | 1   | 123456 |
 ----------------   
 */
```

## Gradle Setup

```kotlin
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
