package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IBungeeMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBungeeMessage[] getMessages();

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull String id, @NotNull Object... replaces);

    @Override
    @NotNull IBungeeArrayMessage getMessageArray(@NotNull String id, @NotNull Object... replaces);

    default @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces) {
        return this.getText(player.getUniqueId(), id, replaces);
    }

    default @NotNull String getLegacyText(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(player.getUniqueId(), id, replaces));
    }
    default @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(uuid, id, replaces));
    }
    default @NotNull String getLegacyText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        return ComponentUtils.getText(this.getText(locale, id, replaces));
    }
}
