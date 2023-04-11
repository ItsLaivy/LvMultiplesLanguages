package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import org.jetbrains.annotations.NotNull;

public interface IBukkitMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBukkitMessage[] getMessages();

    @Override
    @NotNull IBukkitMessage getMessage(@NotNull String id, @NotNull Object... replaces);

}
