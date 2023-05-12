package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBukkitMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Plugin, BaseComponent[], BukkitMessage, BukkitMessageStorage> {

    @Nullable IBukkitItemTranslator getItemTranslator();

    @Override
    @Nullable BukkitMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name);

    default @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull BukkitMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), messageStorage, id, replaces);
    }
}
