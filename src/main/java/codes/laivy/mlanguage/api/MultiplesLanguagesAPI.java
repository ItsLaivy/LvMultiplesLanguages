package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MultiplesLanguagesAPI implements IMultiplesLanguagesAPI {

    private final @NotNull Platform platform;

    private @Nullable ItemTranslator<?, ?> itemTranslator;

    protected @Nullable Set<MessageStorage> messageStorages;
    protected @Nullable Locale defaultLocale;

    protected boolean loaded = false;

    public MultiplesLanguagesAPI(@NotNull Platform platform, @Nullable ItemTranslator<?, ?> itemTranslator) {
        this.platform = platform;
        this.itemTranslator = itemTranslator;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return platform;
    }

    @Override
    public @NotNull Set<MessageStorage> getStorages() {
        if (messageStorages != null) {
            return messageStorages;
        }
        throw new NullPointerException("The API isn't loaded yet.");
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.get(locale, id, replaces);
    }

    @Override
    public @NotNull Message get(final @NotNull MessageStorage messageStorage, final @NotNull String id, final @NotNull Object... replaces) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        if (defaultLocale != null) {
            return defaultLocale;
        } else {
            throw new NullPointerException("The API isn't loaded yet.");
        }
    }

    @Override
    public @Nullable ItemTranslator<?, ?> getItemTranslator() {
        return itemTranslator;
    }

    public void setItemTranslator(@Nullable ItemTranslator<?, ?> itemTranslator) {
        this.itemTranslator = itemTranslator;
    }
}
