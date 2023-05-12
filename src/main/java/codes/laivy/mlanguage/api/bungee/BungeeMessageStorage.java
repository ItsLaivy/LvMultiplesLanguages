package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BungeeMessageStorage extends BaseComponentMessageStorage<BungeeMessage> {
    @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces);
    @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces);
}
