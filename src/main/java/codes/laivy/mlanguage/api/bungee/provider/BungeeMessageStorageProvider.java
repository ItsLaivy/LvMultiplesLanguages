package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.plugin.BungeePluginProperty;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BungeeMessageStorageProvider implements BungeeMessageStorage {

    private final @NotNull BungeePluginProperty plugin;
    private final @NotNull String name;
    private final @NotNull Locale defaultLocale;
    private final @NotNull Set<BungeeMessage> messages;

    public BungeeMessageStorageProvider(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Set<BungeeMessage> messages) {
        this.plugin = new BungeePluginProperty(plugin);
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
    public @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces) {
        return getMessage(id).getArray(player, replaces);
    }

    @Override
    public @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull String id, @NotNull Object... replaces) {
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
    public @NotNull Set<BungeeMessage> getMessages() {
        return messages;
    }

    @Override
    public @NotNull BungeeMessage getMessage(@NotNull String id) {
        Optional<BungeeMessage> optional = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        return optional.orElseThrow(() -> new NullPointerException("This storage named '" + getName() + "' at plugin '" + getPluginProperty().getName() + "' doesn't contains a message with id '" + id + "'"));
    }

    @Override
    public @NotNull BungeeMessage getMessage(@NotNull String id, @NotNull Object... replaces) {
        Optional<BungeeMessage> optional = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        BungeeMessage message = optional.orElseThrow(() -> new NullPointerException("This storage named '" + getName() + "' at plugin '" + getPluginProperty().getName() + "' doesn't contains a message with id '" + id + "'"));

        message = message.clone();
        message.getReplacements().addAll(Arrays.asList(replaces));

        return message;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull BungeePluginProperty getPluginProperty() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BungeeMessageStorageProvider)) return false;
        BungeeMessageStorageProvider that = (BungeeMessageStorageProvider) o;
        return getPluginProperty().equals(that.getPluginProperty()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPluginProperty(), getName());
    }
}
