package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.BukkitStoredMessage;
import org.jetbrains.annotations.NotNull;

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
}
