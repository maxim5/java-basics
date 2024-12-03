package io.spbx.util.base.lang;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;

import java.util.Map;

@Stateless
@CheckReturnValue
public class EasyCast {
    public static int castToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static boolean castToBool(int i) {
        return i != 0;
    }

    @SuppressWarnings("unchecked")
    public static <R, T> R castAny(T object) {
        return (R) object;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> castMap(Map<?, ?> map) {
        return (Map<K, V>) map;
    }
}
