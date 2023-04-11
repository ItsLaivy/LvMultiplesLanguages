package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface IBungeeMessage extends Message<BaseComponent> {

    @Override
    @NotNull IBungeeMessageStorage getStorage();

}
