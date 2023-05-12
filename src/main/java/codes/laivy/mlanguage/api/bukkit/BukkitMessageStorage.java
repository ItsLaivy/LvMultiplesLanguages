package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BukkitMessageStorage extends BaseComponentMessageStorage<BukkitMessage> {
    @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces);
    @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces);
}
