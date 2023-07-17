package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.BungeeStoredMessage;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BungeeStoredMessageProvider implements BungeeStoredMessage {

    private final @NotNull BungeeMessageStorage storage;
    private final @NotNull BungeeMessage message;

    private final @NotNull List<Object> replacements;
    private final @NotNull List<Object> prefixes;
    private final @NotNull List<Object> suffixes;

    public BungeeStoredMessageProvider(@NotNull BungeeMessageStorage storage, @NotNull BungeeMessage message) {
        this.storage = storage;
        this.message = message;

        this.replacements = new LinkedList<>();
        this.prefixes = new LinkedList<>();
        this.suffixes = new LinkedList<>();
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
        if (!(o instanceof BungeeStoredMessageProvider)) return false;
        BungeeStoredMessageProvider that = (BungeeStoredMessageProvider) o;
        return getStorage().equals(that.getStorage()) && getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStorage(), getMessage());
    }
}
