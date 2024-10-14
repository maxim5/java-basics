package io.spbx.util.testing;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.spbx.util.base.BasicExceptions.InternalErrors.assureNonNull;
import static io.spbx.util.testing.TestingBasics.streamOf;
import static io.spbx.util.testing.TestingBytes.CHARSET;

public class TestingParams {
    public static byte @Nullable[] paramToNullBytes(@NotNull String encoded) {
        return encoded.equals("null") ? null :
            encoded.isEmpty() || encoded.equals("[]") ?
                new byte[0] :
                Bytes.toArray(streamOf(encoded.split(",")).map(String::trim).map(Byte::parseByte).toList());
    }

    public static byte @NotNull[] paramToBytes(@NotNull String encoded) {
        return assureNonNull(paramToNullBytes(encoded), "Invalid encoded params:", encoded);
    }

    public static int @Nullable[] paramToNullInts(@NotNull String encoded) {
        return encoded.equals("null") ? null :
            encoded.isEmpty() || encoded.equals("[]") ?
                new int[0] :
                Ints.toArray(streamOf(encoded.split(",")).map(String::trim).map(Integer::parseInt).toList());
    }

    public static int @NotNull[] paramToInts(@NotNull String encoded) {
        return assureNonNull(paramToNullInts(encoded), "Invalid encoded params:", encoded);
    }

    public static long @Nullable[] paramToNullLongs(@NotNull String encoded) {
        return encoded.equals("null") ? null :
            encoded.isEmpty() || encoded.equals("[]") ?
                new long[0] :
                Longs.toArray(streamOf(encoded.split(",")).map(String::trim).map(Long::parseLong).toList());
    }

    public static long @NotNull[] paramToLongs(@NotNull String encoded) {
        return assureNonNull(paramToNullLongs(encoded), "Invalid encoded params:", encoded);
    }

    public static @Nullable String paramToNullString(@NotNull String encoded) {
        return encoded.equals("null") ? null :
            encoded.matches("(\\d+,?\\s*)+") ?
                new String(paramToBytes(encoded), CHARSET) :
                encoded;
    }

    public static @NotNull String paramToString(@NotNull String encoded) {
        return assureNonNull(paramToNullString(encoded), "Invalid encoded params:", encoded);
    }
}
