package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageArray;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The LvMultiplesLanguages API
 * @param <PLUGIN> the plugin class
 * @param <C> The component class
 */
public interface IMultiplesLanguagesAPI<I, PLUGIN, PLAYER, C> {

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
    @NotNull PLUGIN getPlugin();

    @NotNull Set<MessageStorage<C>> getStorages();

    @Nullable MessageStorage<C> getStorage(@NotNull PLUGIN plugin, @NotNull String name);

    @NotNull C[] getText(@Nullable Locale locale, @NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull Object... replaces);

    default @NotNull C[] getText(@NotNull UUID uuid, @NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(getLocale(uuid), messageStorage, id, replaces);
    }

    @NotNull Message<C> getMessage(@NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull Object... replaces);
    @NotNull Message<C> getMessage(@NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces);

    @NotNull MessageArray<C> getMessageArray(@NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull Object... replaces);
    @NotNull MessageArray<C> getMessageArray(@NotNull MessageStorage<C> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces);

    @Nullable Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @Nullable Locale locale);

    @Nullable ItemTranslator<I, PLAYER, C> getItemTranslator();

}
