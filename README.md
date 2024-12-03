# Basic Java Utils

Takes care of the boilerplate so that you don't have to.

## API

### Concise [streams](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/collect/Streamer.java)

```java
Streamer.of(1, 2, 3, null).toArrayList();                 // [1, 2, 3, null]
Streamer.repeat("abc", 3).join(" ");                      // "abc abc abc"
Streamer.of(iterable).skipNulls().toNativeArray();        // native array without nulls
```

```java
Streamer.of(mapOf(1, 2)).mapKeys(String::valueOf).toMap() // {"1" -> 2}
Streamer.zip(keys, values).toLinkedHashMap();             // LinkedHashMap of keys -> values
```

```java
Streamer.of(list).toAtMostTwo();                          // get 0, 1 or 2 elements or throw
Streamer.of(array).toExactlyTwo();                        // get exactly 2 elements or throw
```

### Non-verbose [exceptions](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/BasicExceptions.java)

```java
// Concise assertions
IllegalArgumentExceptions.assure(value == null, "Key already present: %s", key);
IllegalStateExceptions.failIf(state.done(), "Invalid `%s` state: %s", this, state);
assert arg > 0 : newInternalError("`arg=%s` must be positive", arg);
```

```java
// One-liners without checked exceptions
List<String> lines = runRethrow(() -> Files.readAllLines(path));
new Thread(rethrow(() -> runServer(port)));
```

```java
// Unchecked throw of any throwable
public void fail() {
    Unchecked.throwAny(new IOException("End of file"));
}
```

```java
// Concise NotImplemented
public Shell getAdminShell() {
    throw notImplemented("getAdminShell() unsupported for engine: %s", table.engine());
}
```

### [`Pair`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/Pair.java), [`Triple`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/Triple.java) and other [`Tuple`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/Tuple.java)s

```java
Pair.of(1, 2).mapFirst(i -> i * 3);                         // (3, 2)
Pair.from("1:2".split(":"));                                // ("1", "2")
Pair.of(1, 2).toTripleWith(3);                              // (1, 2, 3)
Triple.of(1, "2", '3').map(i -> -i, s -> "0", ch -> '4');   // (-1, "0", '4')
OneOf.ofFirst(1).hasSecond();                               // false
Pair.of(1, null).toOneOf();                                 // (1, null)
```

### Convenient [builder](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/collect/ListBuilder.java)s

```java
ListBuilder.of(1, 2, 3).add(4).addAll(5, 6).toArrayList();      // [1, 2, 3, 4, 5, 6]
ListBuilder.of().addAll(iterable).skipNulls().toBasicsTuple();  // Tuple without nulls
```

```java
MapBuilder.builder().put(1, 2).overwrite(1, 3).toTreeMap();     // {1: 3}
MapBuilder.copyOf(map).overwrite(1, 2).bimaps().toGuavaBiMap(); // Guava BiMap
```

### [`DataSize`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/DataSize.java) for size units (Kb, Mb, Gb, ...)

```java
DataSize.parse("123.0KB").toBytes();            // 125952
DataSize.ofBytes(65536).toString();             // 64Kb
DataSize.ofKilobytes(50000).toString();         // 48.83Mb
DataSize.ofGigabytes(1).toString(Unit.Byte);    // 1073741824b
```

### [`Int128`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/base/Int128.java) for near native efficient 128-bit arithmetic

```java
Int128.from("9619122375485128076391017781203171")
    .toBigInteger();                            // 9619122375485128076391017781203171
Int128.from(1L << 62).multiply(10);             // 46116860184273879040
Int128.MAX_VALUE.toString();                    // 170141183460469231731687303715884105727
Int128.MIN_VALUE.toHexString();                 // 80000000000000000000000000000000
```

### Simple [text processing](https://github.com/maxim5/java-basics/tree/master/src/main/java/io/spbx/util/text)

```java
BasicJoin.of(1, 2, null).join(',');                 // "1,2,"
BasicJoin.of(1, 2, "").onlyNonEmpty().join(", ");   // "1, 2"
```

```java
BasicSplit.of("foo.bar.").exactly(3).on('.');       // ["foo", "bar", ""]
BasicSplit.of("foo.bar.").skipEmpty().on(".");      // ["foo", "bar"]
```

```java
BasicParsing.parseIntSafe(str, -1);                 // never throws, falls back to -1
BasicParsing.parseLongSafe(str, 0);                 // never throws, falls back to 0
```

### [`Tabular`](https://github.com/maxim5/java-basics/blob/master/src/main/java/io/spbx/util/collect/Tabular.java) data formatting

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

And more.

## Gradle Setup

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.maxim5:java-basics:0.4.0")
}
```

[![JitPack workflow](https://jitpack.io/v/maxim5/java-basics.svg)](https://jitpack.io/#maxim5/java-basics)
![Gradle workflow](https://github.com/maxim5/java-basics/actions/workflows/gradle.yml/badge.svg)
