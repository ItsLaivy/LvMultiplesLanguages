package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;
import static org.bukkit.Bukkit.getServer;

public class ReflectionUtils {
    
    static {
        // TODO: 06/04/2023 A better reflection utils system 
        getDefApi();
    }

    public static boolean isCompatible(@NotNull Class<? extends Version> version) {
        return version.isAssignableFrom(getDefApi().getVersion().getClass());
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
        return getDefApi().getVersion().isCompatible(getVersionName());
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
