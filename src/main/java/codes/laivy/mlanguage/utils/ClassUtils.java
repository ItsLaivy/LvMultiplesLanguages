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

}
