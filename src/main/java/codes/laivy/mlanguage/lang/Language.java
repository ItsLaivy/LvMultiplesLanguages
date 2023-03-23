package codes.laivy.mlanguage.lang;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface Language {

    default @NotNull BaseComponent get(@NotNull Locale locale, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, id, (Object[]) replaces);
    }
    @NotNull BaseComponent get(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces);

    @NotNull String getName();
    @NotNull String getPlugin();

}
