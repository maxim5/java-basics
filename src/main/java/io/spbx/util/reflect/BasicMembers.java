package io.spbx.util.reflect;

import io.spbx.util.base.BasicNulls;
import io.spbx.util.classpath.BasicClasspath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.spbx.util.base.EasyCast.castAny;
import static java.util.Objects.requireNonNull;

public class BasicMembers {
    /** Modifiers **/

    public static boolean isPublicStatic(@NotNull Member member) {
        return hasModifiers(member, Modifier.STATIC | Modifier.PUBLIC);
    }

    public static boolean isPublic(@NotNull Member member) {
        return hasModifiers(member, Modifier.PUBLIC);
    }

    public static boolean isPrivate(@NotNull Member member) {
        return hasModifiers(member, Modifier.PRIVATE);
    }

    public static boolean isStatic(@NotNull Member member) {
        return hasModifiers(member, Modifier.STATIC);
    }

    public static boolean isVisibleInPackage(@NotNull Member member, @NotNull String packageName) {
        return isPublic(member) || !isPrivate(member) && packageName.equals(member.getDeclaringClass().getPackageName());
    }

    public static boolean hasModifiers(@NotNull Member member, int modifiers) {
        return (modifiers & member.getModifiers()) == modifiers;
    }

    public static boolean hasName(@NotNull Member member, @NotNull String name) {
        return member.getName().equals(name);
    }

    /** Methods **/

    public static class Methods implements MembersContainer<Method> {
        private final Class<?> klass;

        Methods(@Nullable Class<?> klass) {
            this.klass = klass;
        }

        public static @NotNull Methods of(@Nullable Class<?> klass) {
            return new Methods(klass);
        }

        @Override public @NotNull Stream<Method> list(@NotNull Scope scope) {
            return klass == null ? Stream.empty() : switch (scope) {
                case DECLARED -> Stream.of(klass.getDeclaredMethods());
                case HIERARCHY_PUBLIC -> Stream.of(klass.getMethods());
                case HIERARCHY_ALL -> Stream.concat(Stream.of(klass.getDeclaredMethods()),
                                                    Methods.of(klass.getSuperclass()).list(Scope.HIERARCHY_ALL));
            };
        }

        @Override public @Nullable Method find(@NotNull Scope scope, @NotNull Predicate<Method> predicate) {
            return klass == null ? null : switch (scope) {
                case DECLARED -> Stream.of(klass.getDeclaredMethods()).filter(predicate).findAny().orElse(null);
                case HIERARCHY_PUBLIC -> Stream.of(klass.getMethods()).filter(predicate).findAny().orElse(null);
                case HIERARCHY_ALL ->
                    BasicNulls.firstNonNullIfExist(find(Scope.DECLARED, predicate),
                                                   () -> Methods.of(klass.getSuperclass()).find(Scope.HIERARCHY_ALL, predicate));
            };
        }
    }

    public static boolean hasReturnType(@NotNull Method method, @NotNull Class<?> type) {
        return type.isAssignableFrom(method.getReturnType());
    }

    /** Fields **/

    public static class Fields implements MembersContainer<Field> {
        private final Class<?> klass;

        Fields(@Nullable Class<?> klass) {
            this.klass = klass;
        }

        public static @NotNull Fields of(@Nullable Class<?> klass) {
            return new Fields(klass);
        }

        @Override public @NotNull Stream<Field> list(@NotNull Scope scope) {
            return klass == null ? Stream.empty() : switch (scope) {
                case DECLARED -> Stream.of(klass.getDeclaredFields());
                case HIERARCHY_PUBLIC -> Stream.of(klass.getFields());
                case HIERARCHY_ALL -> Stream.concat(Stream.of(klass.getDeclaredFields()),
                                                    Fields.of(klass.getSuperclass()).list(Scope.HIERARCHY_ALL));
            };
        }

        @Override public @Nullable Field find(@NotNull Scope scope, @NotNull Predicate<Field> predicate) {
            return klass == null ? null : switch (scope) {
                case DECLARED -> Stream.of(klass.getDeclaredFields()).filter(predicate).findAny().orElse(null);
                case HIERARCHY_PUBLIC -> Stream.of(klass.getFields()).filter(predicate).findAny().orElse(null);
                case HIERARCHY_ALL ->
                    BasicNulls.firstNonNullIfExist(find(Scope.DECLARED, predicate),
                                                   () -> Fields.of(klass.getSuperclass()).find(Scope.HIERARCHY_ALL, predicate));
            };
        }

        public @Nullable Field findPublicStaticInstance() {
            return klass != null ? find(field -> isPublicStatic(field) && hasType(field, klass)) : null;
        }
    }

    public static boolean hasType(@NotNull Field field, @NotNull Class<?> type) {
        return type.isAssignableFrom(field.getType());
    }

    /** Constructors **/

    public static class Constructors implements MembersContainer<Constructor<?>> {
        private final Class<?> klass;

        Constructors(@Nullable Class<?> klass) {
            this.klass = klass;
        }

        public static @NotNull Constructors of(@Nullable Class<?> klass) {
            return new Constructors(klass);
        }

        @Override public @NotNull Stream<Constructor<?>> list(@NotNull Scope scope) {
            return klass == null ? Stream.empty() : switch (scope) {
                case DECLARED -> Stream.of(klass.getDeclaredConstructors());
                case HIERARCHY_PUBLIC, HIERARCHY_ALL -> Stream.of(klass.getConstructors());
            };
        }

        @Override public @Nullable Constructor<?> find(@NotNull Scope scope, @NotNull Predicate<Constructor<?>> predicate) {
            return klass == null ? null : list(scope).filter(predicate).findAny().orElse(null);
        }
    }

    /** MembersContainer **/

    public interface MembersContainer<T extends Member> {
        @NotNull Stream<T> list(@NotNull Scope scope);

        @Nullable T find(@NotNull Scope scope, @NotNull Predicate<T> predicate);

        default @Nullable T find(@NotNull String name) {
            return find(Scope.DECLARED, name);
        }

        default @Nullable T find(@NotNull Predicate<T> predicate) {
            return find(Scope.DECLARED, predicate);
        }

        default @Nullable T find(@NotNull Scope scope, @NotNull String name) {
            return find(scope, method -> hasName(method, name));
        }

        default @NotNull T getOrDie(@NotNull String name) {
            return getOrDie(Scope.DECLARED, name);
        }

        default @NotNull T getOrDie(@NotNull Predicate<T> predicate) {
            return getOrDie(Scope.DECLARED, predicate);
        }

        default @NotNull T getOrDie(@NotNull Scope scope, @NotNull String name) {
            return getOrDie(scope, method -> hasName(method, name));
        }

        default @NotNull T getOrDie(@NotNull Scope scope, @NotNull Predicate<T> predicate) {
            return requireNonNull(find(scope, predicate));
        }

        default boolean has(@NotNull String name) {
            return has(Scope.DECLARED, name);
        }

        default boolean has(@NotNull Predicate<T> predicate) {
            return has(Scope.DECLARED, predicate);
        }

        default boolean has(@NotNull Scope scope, @NotNull String name) {
            return has(scope, method -> hasName(method, name));
        }

        default boolean has(@NotNull Scope scope, @NotNull Predicate<T> predicate) {
            return find(scope, predicate) != null;
        }
    }

    public enum Scope {
        DECLARED,
        HIERARCHY_PUBLIC,
        HIERARCHY_ALL,
    }

    /** Instances **/

    public static <T> @Nullable T newInstanceOrNull(@Nullable Class<T> klass) {
        if (klass == null) {
            return null;
        }
        try {
            Field instance = Fields.of(klass).findPublicStaticInstance();
            if (instance != null) {
                return castAny(instance.get(klass));
            }

            Constructor<T> constructor = klass.getConstructor();
            if (!isPrivate(constructor)) {
                return constructor.newInstance();
            }

            Method staticConstructor =
                Methods.of(klass).find(Scope.DECLARED, method -> isPublicStatic(method) && hasReturnType(method, klass));
            if (staticConstructor != null) {
                return castAny(staticConstructor.invoke(klass));
            }
        } catch (Throwable ignore) {
        }
        return null;
    }

    public static <T> @Nullable T newInstanceOrNull(@NotNull String className) {
        Class<T> klass = castAny(BasicClasspath.classForNameOrNull(className));
        return BasicMembers.newInstanceOrNull(klass);
    }

    public static <T> T @NotNull[] newArray(int length, @NotNull T t) {
        return castAny(Array.newInstance(t.getClass(), length));
    }
}
