package codes.laivy.mlanguage.utils;

import org.jetbrains.annotations.NotNull;

public class ClassUtils {

    public static boolean isPresent(@NotNull String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignore) {
            return false;
        }
    }

    public static boolean isInstanceOf(@NotNull Class<?> c1, @NotNull Class<?> c2) {
        if (c1.equals(c2)) {
            return true;
        } else if (c1.isAssignableFrom(c2)) {
            return true;
        } else if (c2.isAssignableFrom(c1)) {
            return true;
        }
        return same(c1, c2);
    }
    public static boolean same(@NotNull Class<?> c1, @NotNull Class<?> c2) {
        if (c1.isPrimitive() && c2.isPrimitive()) {
            return c1.equals(c2);
        } else {
            try {
                if (c1.isPrimitive()) {
                    try {
                        Class<?> primitive = (Class<?>) c2.getField("TYPE").get(null);
                        return c1.equals(primitive);
                    } catch (NoSuchFieldException ignore) {
                    }
                } else {
                    try {
                        Class<?> primitive = (Class<?>) c1.getField("TYPE").get(null);
                        return c2.equals(primitive);
                    } catch (NoSuchFieldException ignore) {
                    }
                }
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
