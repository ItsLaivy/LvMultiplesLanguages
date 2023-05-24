package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The LvMultiplesLanguages API
 * @param <P> the plugin class
 * @param <C> The component class
 * @param <M> the message class
 * @param <S> the message storage class
 */
public interface IMultiplesLanguagesAPI<P, C, M extends Message<C>, S extends MessageStorage<C, M>> {

    /**
     * Loads all languages into the RAM
     */
    void load();

    /**
     * Unloads all loaded languages
     */
    void unload();

    /**
     * Checks if the API is loaded
     * @return true if the api is loaded, false otherwise
     */
    boolean isLoaded();

    /**
     * The platform running this API
     * @return the platform
     */
    @NotNull P getPlugin();

    @NotNull Set<S> getStorages();

    @Nullable S getStorage(@NotNull P plugin, @NotNull String name);

    @NotNull C getText(@Nullable Locale locale, @NotNull S messageStorage, @NotNull String id, @NotNull Object... replaces);

    default @NotNull C getText(@NotNull UUID uuid, @NotNull S messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(getLocale(uuid), messageStorage, id, replaces);
    }

    @NotNull Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @NotNull Locale locale);

    /**
     * Creates a new storage with the values
     * @param plugin the plugin
     * @param name the name
     * @param locale the default locale
     * @param messages the messages
     * @return a new message storage or an existent storage one if exists
     */
    @NotNull S createStorage(@NotNull P plugin, @NotNull String name, @NotNull Locale locale, @NotNull Set<M> messages);

    @NotNull M createMessage(@NotNull String id, @NotNull Map<@NotNull Locale, @NotNull C> data);

    // TODO: 23/05/2023 #createStorage(InputStream)

    @NotNull MessageSerializer<C, M, S> getSerializer();
}
