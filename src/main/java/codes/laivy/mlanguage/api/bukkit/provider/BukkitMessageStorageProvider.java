package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.plugin.BukkitPluginProperty;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BukkitMessageStorageProvider implements BukkitMessageStorage {

    private final @NotNull BukkitPluginProperty plugin;
    private final @NotNull String name;
    private final @NotNull Locale defaultLocale;
    private final @NotNull Set<BukkitMessage> messages;

    public BukkitMessageStorageProvider(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Set<BukkitMessage> messages) {
        this.plugin = new BukkitPluginProperty(plugin);
        this.name = name;
        this.defaultLocale = defaultLocale;
        this.messages = messages;
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getArray(uuid, replaces);
    }

    @Override
    public @NotNull BaseComponent[] getText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getText(uuid, replaces);
    }

    @Override
    public @NotNull List<@NotNull String> getLegacyTextArray(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getLegacyArray(uuid, replaces);
    }

    @Override
    public @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getLegacyText(uuid, replaces);
    }

    @Override
    public @NotNull List<String> getLegacyTextArray(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        if (locale == null) locale = getDefaultLocale();
        return getMessage(id).getLegacyArray(locale, replaces);
    }

    @Override
    public @NotNull String getLegacyText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        if (locale == null) locale = getDefaultLocale();
        return getMessage(id).getLegacyText(locale, replaces);
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getArray(player, replaces);
    }

    @Override
    public @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getText(player, replaces);
    }

    @Override
    public @NotNull List<BaseComponent @NotNull []> getTextArray(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        if (locale == null) locale = getDefaultLocale();
        return getMessage(id).getArray(locale, replaces);
    }

    @Override
    public BaseComponent @NotNull [] getText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        if (locale == null) locale = getDefaultLocale();
        return getMessage(id).getText(locale, replaces);
    }

    @Override
    public @NotNull Set<BukkitMessage> getMessages() {
        return messages;
    }

    @Override
    public @NotNull BukkitMessage getMessage(@NotNull String id) {
        Optional<BukkitMessage> optional = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new NullPointerException("This storage named '" + getName() + "' at plugin '" + getPluginProperty().getName() + "' doesn't contains a message with id '" + id + "'");
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull BukkitPluginProperty getPluginProperty() {
        return plugin;
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public boolean isArray(@NotNull String id, @NotNull Locale locale) {
        return getMessage(id).isArrayText(locale);
    }
    @Override
    public boolean isLegacy(@NotNull String id, @NotNull Locale locale) {
        return getMessage(id).isLegacyText(locale);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
