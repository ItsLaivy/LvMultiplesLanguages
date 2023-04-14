package codes.laivy.mlanguage.api.bukkit.natives;

import codes.laivy.mlanguage.api.bukkit.IBukkitArrayMessage;
import codes.laivy.mlanguage.api.bukkit.IBukkitMessageStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BukkitArrayMessage extends BukkitMessage implements IBukkitArrayMessage {
    public BukkitArrayMessage(@NotNull IBukkitMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        super(messageStorage, id, replaces);
    }
    public BukkitArrayMessage(@NotNull IBukkitMessageStorage messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces) {
        super(messageStorage, id, prefixes, suffixes, replaces);
    }
}
