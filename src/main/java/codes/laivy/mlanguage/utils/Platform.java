package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The platform system
 * @param <I> the item class
 * @param <PLUGIN> the plugin class
 * @param <PLAYER>> the player class
 * @param <C> The component class
 */
public interface Platform<I, PLUGIN, PLAYER, C> {

    /**
     * Returns the plugin data folder (e.g.: "/plugins")
     * @return the plugin folder
     */
    @Contract(pure = true)
    @Nullable File getDataFolder();

    /**
     * The plataform {@link Type} of this plugin
     * @return the platform type
     */
    @Contract(pure = true)
    @NotNull Type getType();

    @NotNull IMultiplesLanguagesAPI<I, PLUGIN, PLAYER, C> getApi();

    void log(@NotNull BaseComponent component);

    enum Type {
        BUKKIT,
        BUNGEE
    }

}
