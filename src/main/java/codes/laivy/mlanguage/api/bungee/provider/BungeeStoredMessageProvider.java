package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.BungeeStoredMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BungeeStoredMessageProvider implements BungeeStoredMessage {

    private final @NotNull BungeeMessageStorage storage;
    private final @NotNull BungeeMessage message;

    public BungeeStoredMessageProvider(@NotNull BungeeMessageStorage storage, @NotNull BungeeMessage message) {
        this.storage = storage;
        this.message = message;
    }

    @Override
    public @NotNull BungeeMessageStorage getStorage() {
        return storage;
    }

    @Override
    public @NotNull BungeeMessage getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BungeeStoredMessageProvider)) return false;
        BungeeStoredMessageProvider that = (BungeeStoredMessageProvider) o;
        return getStorage().equals(that.getStorage()) && getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStorage(), getMessage());
    }
}
