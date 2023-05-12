package codes.laivy.mlanguage.api.bungee.components;

import codes.laivy.mlanguage.lang.StoredMessage;
import org.jetbrains.annotations.NotNull;

public interface BaseComponentStoredMessage<M extends BaseComponentMessage, S extends BaseComponentMessageStorage<M>> extends StoredMessage {

    @Override
    @NotNull S getStorage();

    @Override
    @NotNull M getMessage();

}
