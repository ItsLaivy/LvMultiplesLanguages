package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IBungeeMessage extends Message<BaseComponent> {

    @Override
    @NotNull IBungeeMessageStorage getStorage();

    @NotNull
    default BaseComponent[] get(@NotNull ProxiedPlayer proxiedPlayer) {
        return this.get(proxiedPlayer, new Object[0]);
    }
    @NotNull
    default BaseComponent[] get(@NotNull ProxiedPlayer proxiedPlayer, @Nullable Object... replaces) {
        return Message.super.get(proxiedPlayer.getUniqueId(), replaces);
    }
}
