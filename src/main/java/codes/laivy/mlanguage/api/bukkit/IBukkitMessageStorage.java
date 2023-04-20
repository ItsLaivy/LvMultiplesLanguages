package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface IBukkitMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBukkitMessage[] getMessages();

    @Override
    @NotNull IBukkitMessage getMessage(@NotNull String id, @NotNull Object... replaces);

    @Override
    @NotNull IBukkitArrayMessage getMessageArray(@NotNull String id, @NotNull Object... replaces);

    default @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return this.getTextArray(player.getUniqueId(), id, replaces);
    }

    default @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return this.getText(player.getUniqueId(), id, replaces);
    }

    default @NotNull String getLegacyText(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(player.getUniqueId(), id, replaces));
    }
    default @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(uuid, id, replaces));
    }
    default @NotNull String getLegacyText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(locale, id, replaces));
    }
}
