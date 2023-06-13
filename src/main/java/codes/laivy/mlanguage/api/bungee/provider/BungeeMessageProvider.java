package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BungeeMessageProvider implements BungeeMessage {

    private final @NotNull String id;

    private final @NotNull Map<Locale, BaseComponent[]> data;

    private final @NotNull Set<Locale> array;
    private final @NotNull Set<Locale> legacies;

    private final @NotNull List<Object> replacements;
    private final @NotNull List<Object> prefixes;
    private final @NotNull List<Object> suffixes;

    public BungeeMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data, @NotNull Set<Locale> array, @NotNull Set<Locale> legacies) {
        this(id, data, array, legacies, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
    }
    public BungeeMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data) {
        this(id, data, new LinkedHashSet<>(), new LinkedHashSet<>());
    }
    public BungeeMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data, @NotNull Set<Locale> array, @NotNull Set<Locale> legacies, @NotNull List<Object> replacements, @NotNull List<Object> prefixes, @NotNull List<Object> suffixes) {
        this.id = id;
        this.data = data;

        this.array = array;
        this.legacies = legacies;

        this.replacements = replacements;
        this.prefixes = prefixes;
        this.suffixes = suffixes;
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull ProxiedPlayer player, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), replaces);
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getText(LvMultiplesLanguages.getApi().getLocale(uuid), replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull Locale locale, @NotNull Object... replaces) {
        if (!getData().containsKey(locale)) {
            locale = getData().keySet().stream().findFirst().orElseThrow(() -> new NullPointerException("Message without data '" + getId() + "'"));
        }

        List<Object> replacesList = new LinkedList<>();
        replacesList.addAll(getReplacements());
        replacesList.addAll(Arrays.asList(replaces));

        BaseComponent[] components = replace(
                locale,
                getData().get(locale),
                replacesList.toArray(new Object[0])
        ).clone();

        if (components.length == 0) {
            throw new IllegalArgumentException("This message '" + getId() + "' has an empty array of components");
        }

        return components;
    }

    @Override
    public @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull Object... replaces) {
        return ComponentUtils.getText(getText(uuid, replaces));
    }

    @Override
    public @NotNull String getLegacyText(@NotNull Locale locale, @NotNull Object... replaces) {
        return ComponentUtils.getText(getText(locale, replaces));
    }

    // Array
    @Override
    public @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull ProxiedPlayer player, @NotNull Object... replaces) {
        return getArray(player.getUniqueId(), replaces);
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull ProxiedPlayer player) {
        return getArray(player, new Object[0]);
    }
    // Array

    @Override
    public @NotNull Map<@NotNull Locale, BaseComponent @NotNull []> getData() {
        return data;
    }

    @Override
    public @NotNull String getId() {
        return id;
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
    public Locale[] getLocales() {
        return data.keySet().toArray(new Locale[0]);
    }

    @Override
    public boolean isArrayText(@NotNull Locale locale) {
        return getArrayTexts().contains(locale);
    }

    @Override
    public boolean isLegacyText(@NotNull Locale locale) {
        return getLegacyTexts().contains(locale);
    }

    @Override
    public @NotNull Set<@NotNull Locale> getLegacyTexts() {
        return legacies;
    }

    @Override
    public @NotNull Set<@NotNull Locale> getArrayTexts() {
        return array;
    }

    @Override
    public @NotNull BungeeMessageProvider clone() {
        try {
            BungeeMessageProvider clone = (BungeeMessageProvider) super.clone();
            return new BungeeMessageProvider(clone.getId(), new LinkedHashMap<>(clone.getData()), new LinkedHashSet<>(clone.getArrayTexts()), new LinkedHashSet<>(clone.getLegacyTexts()), new LinkedList<>(clone.getReplacements()), new LinkedList<>(clone.getPrefixes()), new LinkedList<>(clone.getSuffixes()));
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BungeeMessageProvider)) return false;
        BungeeMessageProvider that = (BungeeMessageProvider) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
