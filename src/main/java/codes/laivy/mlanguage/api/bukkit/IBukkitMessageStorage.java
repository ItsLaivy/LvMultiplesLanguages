package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IBukkitMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBukkitMessage[] getMessages();

    @Override
    @NotNull IBukkitMessage getMessage(@NotNull String id, @NotNull Object... replaces);

    default @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return this.getText(player.getUniqueId(), id, replaces);
    }
}
