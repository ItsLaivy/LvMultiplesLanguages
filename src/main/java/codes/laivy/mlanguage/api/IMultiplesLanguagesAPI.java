package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The LvMultiplesLanguages API
 * @param <P> the plugin class
 */
public interface IMultiplesLanguagesAPI<P> {

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

    default @Nullable MessageStorage getStorage(@NotNull P plugin, @NotNull String name) {
        for (MessageStorage messageStorage : getStorages()) {
            if (messageStorage.getName().equals(name) && messageStorage.getPlugin().equals(plugin)) {
                return messageStorage;
            }
        }
        return null;
    }

    /**
     * Get the language with the values or creates a new one storage with the parameter details if not exists
     * @param plugin the plugin
     * @param name the storage name
     * @param defaultLocale the default storage locale
     * @param components the components
     * @return if the message storage with that details doesn't exist it will create a new one. return the existent otherwise
     */
    @NotNull MessageStorage create(@NotNull P plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components);

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, messageStorage, id, (Object[]) replaces);
    }
    @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Message... replaces);

    @Nullable Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @Nullable Locale locale);

    @Nullable ItemTranslator<?, ?> getItemTranslator();

}
