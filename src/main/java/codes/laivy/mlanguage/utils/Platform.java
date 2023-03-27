package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface Platform {

    /**
     * Returns the plugin data folder (e.g.: "/plugins")
     * @return the plugin folder
     */
    @Nullable File getDataFolder();

    /**
     * The plataform {@link Type} of this plugin
     * @return the platform type
     */
    @NotNull Type getType();

    @NotNull IMultiplesLanguagesAPI getApi();
    void setApi(@NotNull IMultiplesLanguagesAPI api);

    void log(@NotNull BaseComponent component);

    enum Type {
        BUKKIT,
        BUNGEE
    }

}
