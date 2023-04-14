package codes.laivy.mlanguage.api.bungee.natives;

import codes.laivy.mlanguage.api.bungee.IBungeeArrayMessage;
import codes.laivy.mlanguage.api.bungee.IBungeeMessageStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BungeeArrayMessage extends BungeeMessage implements IBungeeArrayMessage {
    public BungeeArrayMessage(@NotNull IBungeeMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        super(messageStorage, id, replaces);
    }
    public BungeeArrayMessage(@NotNull IBungeeMessageStorage messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces) {
        super(messageStorage, id, prefixes, suffixes, replaces);
    }
}
