package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.MessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IBungeeMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Plugin, BaseComponent[]> {

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Override
    @Nullable IBungeeMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name);

    @Override
    @NotNull IBungeeMessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components);

}
