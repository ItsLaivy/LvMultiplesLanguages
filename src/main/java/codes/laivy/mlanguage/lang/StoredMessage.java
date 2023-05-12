package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.NotNull;

public interface StoredMessage {

    @NotNull MessageStorage<?, ?> getStorage();

    @NotNull Message<?> getMessage();

}
