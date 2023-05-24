package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessage;
import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BungeeMessage extends BaseComponentMessage, Cloneable {
    @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull Object... replaces);

    @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull ProxiedPlayer player, @NotNull Object... replaces);
    @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull ProxiedPlayer player);

    @Override
    @NotNull BungeeMessage clone();
}
