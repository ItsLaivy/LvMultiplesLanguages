package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public interface IBungeeMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBungeeMessage[] getMessages();

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull String id, @NotNull Object... replaces);

    default @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces) {
        return this.getText(player.getUniqueId(), id, replaces);
    }
}
