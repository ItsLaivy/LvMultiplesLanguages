package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The platform system
 * @param <P> the plugin class
 * @param <C> The component class
 * @param <M> the message class
 * @param <S> the message storage class
 */
public interface Platform<P, C, M extends Message<C>, S extends MessageStorage<C, M>> {

    /**
     * Returns the plugin data folder (e.g.: "/plugins")
     * @return the plugin folder
     */
    @Contract(pure = true)
    @Nullable File getDataFolder();

    /**
     * The platform {@link Type} of this plugin
     * @return the platform type
     */
    @Contract(pure = true)
    @NotNull Type getType();

    @NotNull IMultiplesLanguagesAPI<P, C, M, S> getApi();

    /**
     * Log a message into the console
     * @param message the message
     */
    void log(@NotNull C message);

    /**
     * Sends a message to every user at the server
     * @param message the message
     * @param replaces the replaces
     */
    void broadcast(@NotNull M message, @NotNull Object[] replaces);

    enum Type {
        BUKKIT,
        BUNGEE
    }

}
