package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.lang.Message;
import org.jetbrains.annotations.NotNull;

public interface IBungeeMessage extends Message {

    @Override
    @NotNull IBungeeMessageStorage getStorage();

}
