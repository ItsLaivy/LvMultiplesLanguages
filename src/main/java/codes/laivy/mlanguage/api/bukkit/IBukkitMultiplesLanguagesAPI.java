package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.MessageStorage;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBukkitMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Plugin> {

    @Override
    @Nullable IBukkitItemTranslator getItemTranslator();

    @Override
    @NotNull IBukkitMessage get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces);

}
