package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface IBukkitMessage extends Message<BaseComponent> {
    @Override
    @NotNull IBukkitMessageStorage getStorage();

    @NotNull
    default BaseComponent[] get(@NotNull OfflinePlayer proxiedPlayer) {
        return this.get(proxiedPlayer, new Object[0]);
    }
    @NotNull
    default BaseComponent[] get(@NotNull OfflinePlayer proxiedPlayer, @NotNull Object... replaces) {
        return Message.super.get(proxiedPlayer.getUniqueId(), replaces);
    }
}
