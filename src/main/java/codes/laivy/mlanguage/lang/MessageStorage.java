package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MessageStorage {

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, id, (Object[]) replaces);
    }
    @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);

    @NotNull String getName();
    @NotNull Object getPlugin();

    @NotNull Locale getDefaultLocale();

    @NotNull SerializedData serialize();

    /**
     * Unload the language
     */
    void unload();

}
