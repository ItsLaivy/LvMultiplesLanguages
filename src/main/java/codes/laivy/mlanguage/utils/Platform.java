package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The platform system
 * @param <P> the plugin class
 */
public interface Platform<P> {

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

    @NotNull IMultiplesLanguagesAPI<P> getApi();

    void log(@NotNull BaseComponent component);

    enum Type {
        BUKKIT,
        BUNGEE
    }

}
