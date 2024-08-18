package io.spbx.util.extern.guice;

import io.spbx.util.collect.Streamer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// Thanks to https://stackoverflow.com/questions/4238919/inject-generic-implementation-using-guice
public class CompositeType implements ParameterizedType {
    private final String typeName;
    private final Class<?> baseClass;
    private final Type[] generics;

    public CompositeType(@NotNull Class<?> baseClass, @NotNull Class<?> @NotNull ... generics) {
        this.baseClass = baseClass;
        this.generics = generics;
        this.typeName = "%s<%s>".formatted(baseClass.getName(), Streamer.of(generics).map(Class::getName).join(","));
    }

    @Override
    public Type getRawType() {
        return baseClass;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return generics;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
