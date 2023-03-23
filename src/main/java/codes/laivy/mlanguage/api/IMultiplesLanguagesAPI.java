package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface IMultiplesLanguagesAPI {

    /**
     * Loads all languages into the RAM
     */
    void load();

    /**
     * Unloads all loaded languages
     */
    void unload();

    @NotNull Set<Language> getLanguages();

    default @NotNull BaseComponent get(@NotNull Locale locale, @NotNull Language language, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, language, id, (Object[]) replaces);
    }
    @NotNull BaseComponent get(@NotNull Locale locale, @NotNull Language language, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message get(@NotNull Language language, @NotNull String id, @NotNull Object... replaces);

    @Nullable Locale getLocale(@NotNull UUID user);
    void setLocale(@NotNull UUID user, @Nullable Locale locale);

    @NotNull Locale getDefaultLocale();

    @NotNull ItemTranslator<?> getItemTranslator();

}
