package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public abstract class MultiplesLanguagesAPI implements IMultiplesLanguagesAPI {

    private final @NotNull Platform platform;

    private @Nullable ItemTranslator<?, ?> itemTranslator;

    private @Nullable Set<MessageStorage> messageStorages;
    private @Nullable Locale defaultLocale;

    public MultiplesLanguagesAPI(@NotNull Platform platform, @Nullable ItemTranslator<?, ?> itemTranslator) {
        this.platform = platform;
        this.itemTranslator = itemTranslator;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return platform;
    }

    @Override
    public void load() {
        defaultLocale = Locale.EN_US;

        @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> componentMap = new LinkedHashMap<>();
        componentMap.put("Teste1", new LinkedHashMap<Locale, BaseComponent[]>() {{
            put(Locale.EN_US, new TextComponent[] {new TextComponent("Test cool name, just for test")});
            put(Locale.PT_BR, new TextComponent[] {new TextComponent("Nome top só pra teste mesmo")});
        }});
        componentMap.put("Teste2", new LinkedHashMap<Locale, BaseComponent[]>() {{
            put(Locale.EN_US, new TextComponent[] {new TextComponent("Cool test lore, just for testing :)")});
            put(Locale.PT_BR, new TextComponent[] {new TextComponent("Lore top só pra testar cara, que legal...")});
        }});

        messageStorages = new LinkedHashSet<MessageStorage>() {{
            add(new BukkitMessageStorage(Locale.EN_US, componentMap, "Nome teste", multiplesLanguagesBukkit()));
        }};

        platform.log("§aLoaded " + messageStorages.size() + " language" + (messageStorages.size() == 1 ? "" : "s") + ".");
    }

    @Override
    public void unload() {
        messageStorages = null;
        defaultLocale = null;
    }

    @Override
    public @NotNull Set<MessageStorage> getLanguages() {
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
