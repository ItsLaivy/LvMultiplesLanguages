package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BukkitMessage extends BaseComponentMessage {
    @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull Object... replaces);

    @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull OfflinePlayer player, @NotNull Object... replaces);
    @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull OfflinePlayer player);

    @Override
    @NotNull BukkitMessage clone();
}
