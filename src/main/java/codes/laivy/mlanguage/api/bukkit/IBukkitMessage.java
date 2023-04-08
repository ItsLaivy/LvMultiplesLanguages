package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Message;
import org.jetbrains.annotations.NotNull;

public interface IBukkitMessage extends Message {

    @Override
    @NotNull IBukkitMessageStorage getStorage();

}
