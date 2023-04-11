package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.craftbukkit.CraftBukkitMessageStorage;
import org.jetbrains.annotations.NotNull;

public interface IBungeeMessageStorage extends CraftBukkitMessageStorage {

    @Override
    @NotNull IBungeeMessage[] getMessages();

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull String id, @NotNull Object... replaces);

}
