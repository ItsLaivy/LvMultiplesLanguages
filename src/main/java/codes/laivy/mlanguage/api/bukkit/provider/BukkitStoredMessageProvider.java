package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.BukkitStoredMessage;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BukkitStoredMessageProvider implements BukkitStoredMessage {

    private final @NotNull BukkitMessageStorage storage;
    private final @NotNull BukkitMessage message;

    private final @NotNull List<Object> replacements;
    private final @NotNull List<Object> prefixes;
    private final @NotNull List<Object> suffixes;

    public BukkitStoredMessageProvider(@NotNull BukkitMessageStorage storage, @NotNull BukkitMessage message) {
        this.storage = storage;
        this.message = message;

        this.replacements = new LinkedList<>();
        this.prefixes = new LinkedList<>();
        this.suffixes = new LinkedList<>();
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
    public @NotNull List<Object> getReplacements() {
        return replacements;
    }

    @Override
    public @NotNull List<Object> getPrefixes() {
        return prefixes;
    }

    @Override
    public @NotNull List<Object> getSuffixes() {
        return suffixes;
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
