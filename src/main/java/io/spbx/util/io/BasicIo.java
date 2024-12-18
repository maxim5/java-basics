package io.spbx.util.io;

import com.google.common.io.Closeables;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static io.spbx.util.base.lang.EasyCast.castAny;

@Stateless
@Pure
@CheckReturnValue
public class BasicIo {
    public static <T> byte @NotNull[] serialize(@NotNull T instance) {
        return serialize(instance, 8192);
    }

    public static <T> byte @NotNull[] serialize(@NotNull T instance, int bufferSize) {
        assert instance instanceof Serializable : "Object is not Serializable: " + instance;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(bufferSize);
             ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeObject(instance);
            return byteStream.toByteArray();
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static <T> @NotNull T deserialize(byte @NotNull[] bytes) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(byteStream)) {
            return castAny(inputStream.readObject());
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        } catch (ClassNotFoundException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static int base64Length(int len) {
        return ((len + 2) / 3) * 4;
    }

    public static int base64LengthNoPadding(int len) {
        return (len * 4 + 2) / 3;
    }

    public static class Close {
        public static final Closeable EMPTY = () -> {};

        public static @NotNull Closeable asCloseable(@Nullable Object obj) {
            return obj instanceof Closeable closeable ? closeable : EMPTY;
        }

        public static void closeQuietly(@Nullable Closeable closeable) {
            try {
                Closeables.close(closeable, true);
            } catch (IOException impossible) {
                Unchecked.rethrow(impossible);
            }
        }

        public static void closeRethrow(@Nullable Closeable closeable) {
            try {
                Closeables.close(closeable, false);
            } catch (IOException e) {
                Unchecked.rethrow(e);
            }
        }

        public static void closeQuietly(@Nullable AutoCloseable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (Exception ignore) {
            }
        }

        public static void closeRethrow(@Nullable AutoCloseable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (Exception e) {
                Unchecked.rethrow(e);
            }
        }
    }
}
