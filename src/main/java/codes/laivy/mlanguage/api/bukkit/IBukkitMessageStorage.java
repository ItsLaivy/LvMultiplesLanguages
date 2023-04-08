package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import org.jetbrains.annotations.NotNull;

public interface IBukkitMessageStorage extends MessageStorage {

    @Override
    @NotNull IBukkitMessage[] getMessages();

    @Override
    @NotNull IBukkitMessage get(@NotNull String id, @NotNull Message... replaces);

}
