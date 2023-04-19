package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IBukkitMessage extends Message<BaseComponent> {
    @Override
    @NotNull IBukkitMessageStorage getStorage();

    @NotNull
    default BaseComponent[] getText(@NotNull OfflinePlayer proxiedPlayer) {
        return this.getText(proxiedPlayer, new Object[0]);
    }
    @NotNull
    default BaseComponent[] getText(@NotNull OfflinePlayer proxiedPlayer, @NotNull Object... replaces) {
        return Message.super.getText(proxiedPlayer.getUniqueId(), replaces);
    }

    default @NotNull String getLegacyText(@Nullable Locale locale, @NotNull Object... replaces) {
        return ComponentUtils.getText(Message.super.getText(locale, replaces));
    }
    default @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull Object... replaces) {
        return ComponentUtils.getText(Message.super.getText(uuid, replaces));
    }
    default @NotNull String getLegacyText(@NotNull OfflinePlayer player, @NotNull Object... replaces) {
        return ComponentUtils.getText(getText(player, replaces));
    }
}
