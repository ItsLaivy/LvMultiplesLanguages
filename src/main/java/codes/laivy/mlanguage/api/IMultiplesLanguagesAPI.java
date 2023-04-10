package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The LvMultiplesLanguages API
 * @param <P> the plugin class
 * @param <C> The component class
 */
public interface IMultiplesLanguagesAPI<P, C> {

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

    @NotNull Set<MessageStorage> getStorages();

    @Nullable MessageStorage getStorage(@NotNull P plugin, @NotNull String name);

    /**
     * Get the language with the values or creates a new one storage with the parameter details if not exists
     * @param plugin the plugin
     * @param name the storage name
     * @param defaultLocale the default storage locale
     * @param components the components
     * @return if the message storage with that details doesn't exist it will create a new one. return the existent otherwise
     */
    @NotNull MessageStorage create(@NotNull P plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull C>> components);

    default @NotNull C getText(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull C replaces) {
        return getText(locale, messageStorage, id, (Object[]) replaces);
    }
    @NotNull C getText(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message getMessage(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Nullable Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @Nullable Locale locale);

    @Nullable ItemTranslator<?, ?> getItemTranslator();

}
