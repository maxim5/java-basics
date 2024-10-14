package io.spbx.util.extern.asm;

import io.spbx.util.base.Unchecked;
import io.spbx.util.rt.RuntimeRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static io.spbx.util.base.BasicExceptions.IllegalStateExceptions.assureNonNull;
import static io.spbx.util.base.Unchecked.Suppliers.runQuietlyOrNull;
import static io.spbx.util.base.Unchecked.Suppliers.runRethrow;
import static io.spbx.util.collect.BasicIterables.newMutableList;
import static io.spbx.util.func.IntPredicates.equalTo;
import static io.spbx.util.func.Predicates.equalTo;

public class AsmClassScanner {
    static {
        RuntimeRequirement.verify("org.objectweb.asm.ClassReader");
    }

    private final ClassReader classReader;
    private final int apiVersion;

    private AsmClassScanner(@NotNull ClassReader classReader) {
        this(classReader, Api.JDK_UP_TO_23);
    }

    private AsmClassScanner(@NotNull ClassReader classReader, @NotNull Api api) {
        this(classReader, api.version);
    }

    private AsmClassScanner(@NotNull ClassReader classReader, int api) {
        this.classReader = classReader;
        this.apiVersion = api;
    }

    public static @NotNull AsmClassScanner of(@NotNull Class<?> klass) {
        return AsmClassScanner.of(klass.getName());
    }

    public static @NotNull AsmClassScanner of(@NotNull String className) {
        return new AsmClassScanner(runRethrow(() -> new ClassReader(className)));
    }

    public static @NotNull AsmClassScanner of(@NotNull InputStream input) {
        return new AsmClassScanner(runRethrow(() -> new ClassReader(input)));
    }

    public static @NotNull AsmClassScanner of(@NotNull ClassLoader loader, @NotNull String className) {
        try (InputStream input = loader.getResourceAsStream(toResourceName(className))) {
            return new AsmClassScanner(new ClassReader(assureNonNull(input, "Class not found:", className)));
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static class IfSupportedOrNull {
        public static @Nullable AsmClassScanner of(@NotNull Class<?> klass) {
            return IfSupportedOrNull.of(klass.getName());
        }

        public static @Nullable AsmClassScanner of(@NotNull String className) {
            return runQuietlyOrNull(() -> new AsmClassScanner(new ClassReader(className)));
        }

        public static @Nullable AsmClassScanner of(@NotNull InputStream input) {
            return runQuietlyOrNull(() -> new AsmClassScanner(new ClassReader(input)));
        }

        public static @Nullable AsmClassScanner of(@NotNull ClassLoader loader, @NotNull String className) {
            try (InputStream input = loader.getResourceAsStream(toResourceName(className))) {
                return input != null ? new AsmClassScanner(new ClassReader(input)) : null;
            } catch (Throwable e) {
                return null;
            }
        }
    }

    private static @NotNull String toResourceName(@NotNull String className) {
        return className.replace('.', '/') + ".class";
    }

    public @NotNull AsmClassScanner with(@NotNull Api api) {
        return new AsmClassScanner(classReader, api);
    }

    public @NotNull AsmClassScanner with(int api) {
        return new AsmClassScanner(classReader, api);
    }

    public @NotNull HeaderConsumer header(@NotNull HeaderConsumer consumer) {
        classReader.accept(new ClassVisitor(apiVersion) {
            @Override public void visit(int version, int access, String name, String sign, String sup, String[] inter) {
                consumer.accept(version, access, name, sign, sup, inter);
            }
        }, ClassReader.SKIP_CODE);
        return consumer;
    }

    public @Nullable String superClass() {
        AtomicReference<String> result = new AtomicReference<>();
        header((version, access, name, signature, superName, interfaces) -> result.set(superName));
        return result.get();
    }

    public @Nullable String[] superInterfaces() {
        AtomicReference<String[]> result = new AtomicReference<>();
        header((version, access, name, signature, superName, interfaces) -> result.set(interfaces));
        return result.get();
    }

    public @NotNull FieldConsumer fields(@NotNull FieldPredicate predicate, @NotNull FieldConsumer consumer) {
        classReader.accept(new ClassVisitor(apiVersion) {
            @Override public FieldVisitor visitField(int access, String name, String desc, String sign, Object val) {
                if (predicate.test(access, name, desc, sign, val)) {
                    consumer.accept(access, name, desc, sign, val);
                }
                return null;
            }
        }, ClassReader.SKIP_CODE);
        return consumer;
    }

    public @NotNull List<FieldData> fields(@NotNull FieldPredicate predicate) {
        List<FieldData> list = newMutableList();
        fields(predicate, (acc, name, desc, sign, val) -> list.add(new FieldData(acc, name, desc, sign, val)));
        return list;
    }

    public @NotNull MethodConsumer methods(@NotNull MethodPredicate predicate, @NotNull MethodConsumer consumer) {
        classReader.accept(new ClassVisitor(apiVersion) {
            @Override public MethodVisitor visitMethod(int access, String name, String desc, String sign, String[] exc) {
                if (predicate.test(access, name, desc, sign, exc)) {
                    consumer.accept(access, name, desc, sign, exc);
                }
                return null;
            }
        }, ClassReader.SKIP_CODE);
        return consumer;
    }

    public @NotNull List<MethodData> methods(@NotNull MethodPredicate predicate) {
        List<MethodData> list = newMutableList();
        methods(predicate, (acc, name, desc, sign, exc) -> list.add(new MethodData(acc, name, desc, sign, exc)));
        return list;
    }

    // https://asm.ow2.io/versions.html
    public enum Api {
        JDK_UP_TO_23(Opcodes.ASM9),     // JDK 16, ..., 23
        JDK_UP_TO_15(Opcodes.ASM8),     // JDK 14, 15
        JDK_UP_TO_13(Opcodes.ASM7);     // JDK 11, 12, 13

        private final int version;
        Api(int api) {
            this.version = api;
        }
    }

    // See {@link Type#getInternalName()}.
    public static @NotNull String packageName(@NotNull String internalName) {
        int idx;
        return (idx = internalName.lastIndexOf('/')) >= 0 ? internalName.substring(0, idx).replace('/', '.') :
            (idx = internalName.lastIndexOf('.')) >= 0 ? internalName.substring(0, idx) : "";
    }

    public interface HeaderConsumer {
        void accept(int version, int access, String name, String signature, String superName, String[] interfaces);
    }

    public interface FieldConsumer {
        void accept(int access, String name, String descriptor, String signature, Object value);
    }

    public interface MethodConsumer {
        void accept(int access, String name, String descriptor, String signature, String[] exceptions);
    }

    public interface FieldPredicate {
        boolean test(int access, String name, String descriptor, String signature, Object value);

        default FieldPredicate and(FieldPredicate predicate) {
            return (_0, _1, _2, _3, _4) -> this.test(_0, _1, _2, _3, _4) && predicate.test(_0, _1, _2, _3, _4);
        }
        default FieldPredicate or(FieldPredicate predicate) {
            return (_0, _1, _2, _3, _4) -> this.test(_0, _1, _2, _3, _4) || predicate.test(_0, _1, _2, _3, _4);
        }

        static FieldPredicate access(IntPredicate pred) { return (access, _1, _2, _3, _4) -> pred.test(access); }
        static FieldPredicate access(int access) { return access(equalTo(access)); }

        static FieldPredicate name(Predicate<String> pred) { return (_0, name, _2, _3, _4) -> pred.test(name); }
        static FieldPredicate name(String name) { return name(equalTo(name)); }

        static FieldPredicate descriptor(Predicate<String> pred) { return (_0, _1, desc, _3, _4) -> pred.test(desc); }
        static FieldPredicate descriptor(String desc) { return descriptor(equalTo(desc)); }

        static FieldPredicate signature(Predicate<String> pred) { return (_0, _1, _2, sign, _4) -> pred.test(sign); }
        static FieldPredicate signature(String sign) { return signature(equalTo(sign)); }

        static FieldPredicate value(Predicate<Object> pred) { return (_0, _1, _2, _3, val) -> pred.test(val); }
        static FieldPredicate value(Object val) { return value(equalTo(val)); }
    }

    public interface MethodPredicate {
        boolean test(int access, String name, String descriptor, String signature, String[] exceptions);

        default MethodPredicate and(MethodPredicate predicate) {
            return (_0, _1, _2, _3, _4) -> this.test(_0, _1, _2, _3, _4) && predicate.test(_0, _1, _2, _3, _4);
        }
        default MethodPredicate or(MethodPredicate predicate) {
            return (_0, _1, _2, _3, _4) -> this.test(_0, _1, _2, _3, _4) || predicate.test(_0, _1, _2, _3, _4);
        }

        static MethodPredicate access(IntPredicate pred) { return (access, _1, _2, _3, _4) -> pred.test(access); }
        static MethodPredicate access(int access) { return access(equalTo(access)); }

        static MethodPredicate name(Predicate<String> pred) { return (_0, name, _2, _3, _4) -> pred.test(name); }
        static MethodPredicate name(String name) { return name(equalTo(name)); }

        static MethodPredicate descriptor(Predicate<String> pred) { return (_0, _1, desc, _3, _4) -> pred.test(desc); }
        static MethodPredicate descriptor(String desc) { return descriptor(equalTo(desc)); }

        static MethodPredicate signature(Predicate<String> pred) { return (_0, _1, _2, sign, _4) -> pred.test(sign); }
        static MethodPredicate signature(String sign) { return signature(equalTo(sign)); }

        static MethodPredicate exceptions(Predicate<String[]> exc) { return (_0, _1, _2, _3, val) -> exc.test(val); }
        static MethodPredicate exceptions(String[] val) { return exceptions(equalTo(val)); }
    }

    public interface Access {
        static boolean has(int access, int bits) { return (access & bits) != 0; }
        static boolean hasNot(int access, int bits) { return (access & bits) == 0; }

        static boolean isStatic(int access) { return has(access, Opcodes.ACC_STATIC); }
        static boolean isPublic(int access) { return has(access, Opcodes.ACC_PUBLIC); }
        static boolean isPrivate(int access) { return has(access, Opcodes.ACC_PRIVATE); }
        static boolean isProtected(int access) { return has(access, Opcodes.ACC_PROTECTED); }
        static boolean isPackageLocal(int access) { return hasNot(access, ANY_VISIBILITY); }

        int ANY_VISIBILITY = Opcodes.ACC_PUBLIC + Opcodes.ACC_PRIVATE + Opcodes.ACC_PROTECTED;
    }

    public record FieldData(int access,
                            @NotNull String name,
                            @Nullable String descriptor,
                            @Nullable String signature,
                            @Nullable Object value) {}

    public record MethodData(int access,
                             @NotNull String name,
                             @Nullable String descriptor,
                             @Nullable String signature,
                             @Nullable String[] exceptions) {}
}
