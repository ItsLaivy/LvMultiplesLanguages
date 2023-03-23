package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.reflection.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;
import static org.bukkit.Bukkit.getServer;

public class ReflectionUtils {

    public static boolean isCompatible(@NotNull Class<? extends Version> version) {
        return version.isAssignableFrom(multiplesLanguagesBukkit().getVersion().getClass());
    }

    @NotNull
    public static String getVersionName() {
        final String[] packageName = getServer().getClass().getPackage().getName().split("\\.");
        for (String p : packageName) if (p.contains("v1_")) {
            return p;
        }
        throw new NullPointerException("Cannot identify the version's name");
    }

    public static boolean isCompatible() {
        return multiplesLanguagesBukkit().getVersion().isCompatible(getVersionName());
    }

    @Nullable
    public static Class<?> getNullableClass(@NotNull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException | NullPointerException ignore) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @NotNull
    public static Class<?> getClass(@NotNull String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't find class named '" + name + "'", e);
        }
    }

}
