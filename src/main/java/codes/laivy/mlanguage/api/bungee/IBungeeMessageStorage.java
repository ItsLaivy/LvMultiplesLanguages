package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import org.jetbrains.annotations.NotNull;

public interface IBungeeMessageStorage extends MessageStorage {

    @Override
    @NotNull IBungeeMessage[] getMessages();

    @Override
    @NotNull IBungeeMessage get(@NotNull String id, @NotNull Message... replaces);

}
