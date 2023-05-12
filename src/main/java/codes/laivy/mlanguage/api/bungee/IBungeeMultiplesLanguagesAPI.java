package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBungeeMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Plugin, BaseComponent[], BungeeMessage, BungeeMessageStorage> {

    @Override
    @Nullable BungeeMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name);

    default @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull BungeeMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), messageStorage, id, replaces);
    }
}
