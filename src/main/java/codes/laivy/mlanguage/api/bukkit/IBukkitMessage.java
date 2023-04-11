package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface IBukkitMessage extends Message<BaseComponent> {

    @Override
    @NotNull IBukkitMessageStorage getStorage();

}
