package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * The multiples languages main API
 */
public interface IMultiplesLanguagesAPI {

    /**
     * Loads all languages into the RAM
     */
    void load();

    /**
     * Unloads all loaded languages
     */
    void unload();

    /**
     * The platform running this API
     * @return the platform
     */
    @NotNull Platform getPlatform();

    @NotNull Set<MessageStorage> getLanguages();

    default @Nullable MessageStorage getLanguage(@NotNull String name, @NotNull Object plugin) {
        for (MessageStorage messageStorage : getLanguages()) {
            if (messageStorage.getName().equals(name) && messageStorage.getPlugin().equals(plugin)) {
                return messageStorage;
            }
        }
        return null;
    }

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, messageStorage, id, (Object[]) replaces);
    }
    @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Nullable Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @Nullable Locale locale);

    @NotNull Locale getDefaultLocale();

    @Nullable ItemTranslator<?, ?> getItemTranslator();

}
