package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.BukkitStoredMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BukkitStoredMessageProvider implements BukkitStoredMessage {

    private final @NotNull BukkitMessageStorage storage;
    private final @NotNull BukkitMessage message;

    public BukkitStoredMessageProvider(@NotNull BukkitMessageStorage storage, @NotNull BukkitMessage message) {
        this.storage = storage;
        this.message = message;
    }

    @Override
    public @NotNull BukkitMessageStorage getStorage() {
        return storage;
    }

    @Override
    public @NotNull BukkitMessage getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BukkitStoredMessageProvider)) return false;
        BukkitStoredMessageProvider that = (BukkitStoredMessageProvider) o;
        return getStorage().equals(that.getStorage()) && getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStorage(), getMessage());
    }
}
