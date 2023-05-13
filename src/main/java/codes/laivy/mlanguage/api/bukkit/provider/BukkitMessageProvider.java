package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BukkitMessageProvider implements BukkitMessage {
    
    private final @NotNull String id;

    private final @NotNull Map<Locale, BaseComponent[]> data;

    private final @NotNull Set<Locale> array;
    private final @NotNull Set<Locale> legacies;

    private final @NotNull Set<Object> replacements;
    private final @NotNull Set<Object> prefixes;
    private final @NotNull Set<Object> suffixes;

    public BukkitMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data, @NotNull Set<Locale> array, @NotNull Set<Locale> legacies) {
        this(id, data, array, legacies, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
    }
    public BukkitMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data) {
        this(id, data, new LinkedHashSet<>(), new LinkedHashSet<>());
    }
    public BukkitMessageProvider(@NotNull String id, @NotNull Map<Locale, BaseComponent[]> data, @NotNull Set<Locale> array, @NotNull Set<Locale> legacies, @NotNull Set<Object> replacements, @NotNull Set<Object> prefixes, @NotNull Set<Object> suffixes) {
        this.id = id;
        this.data = data;
        this.array = array;
        this.legacies = legacies;
        this.replacements = replacements;
        this.prefixes = prefixes;
        this.suffixes = suffixes;
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull OfflinePlayer player, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), replaces);
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getText(LvMultiplesLanguages.getApi().getLocale(uuid), id, replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@NotNull Locale locale, @NotNull Object... replaces) {
        if (!getData().containsKey(locale)) {
            locale = getData().keySet().stream().findFirst().orElseThrow(() -> new NullPointerException("Message without data '" + getId() + "'"));
        }

        return replace(
                locale,
                getData().get(locale),
                replaces
        ).clone();
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
    public @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull OfflinePlayer player, @NotNull Object... replaces) {
        return getArray(player.getUniqueId(), replaces);
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull OfflinePlayer player) {
        return getArray(player, new Object[0]);
    }

    public @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getArray(LvMultiplesLanguages.getApi().getLocale(uuid), replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    public @NotNull List<@NotNull String> getLegacyArray(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getLegacyArray(LvMultiplesLanguages.getApi().getLocale(uuid), replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    public @NotNull List<String> getLegacyArray(@NotNull Locale locale, @NotNull Object... replaces) {
        List<BaseComponent[]> components = getArray(locale, replaces);
        List<String> legacy = new LinkedList<>();

        for (BaseComponent[] component : components) {
            legacy.add(ComponentUtils.getText(component));
        }

        return legacy;
    }

    @Override
    public @NotNull List<BaseComponent[]> getArray(@NotNull Locale locale, @NotNull Object... replaces) {
        Locale original = locale;

        if (!getData().containsKey(locale)) {
            locale = getData().keySet().stream().findFirst().orElseThrow(() -> new NullPointerException("Message without data '" + getId() + "'"));
        }

        if (!isArrayText(locale)) {
            throw new UnsupportedOperationException("This text with id '" + getId() + "' and locale '" + locale.name() + "' isn't an array text, use #getText instead (Original: '" + original.name() + "').");
        }

        List<BaseComponent[]> components = new LinkedList<>();

        for (BaseComponent component : getText(locale, replaces)) {
            components.add(new BaseComponent[] {
                    component.duplicate()
            });
        }

        return components;
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
    public @NotNull Set<Object> getReplacements() {
        return replacements;
    }

    @Override
    public @NotNull Set<Object> getPrefixes() {
        return prefixes;
    }

    @Override
    public @NotNull Set<Object> getSuffixes() {
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
    protected BukkitMessageProvider clone() throws CloneNotSupportedException {
        BukkitMessageProvider clone = (BukkitMessageProvider) super.clone();
        return new BukkitMessageProvider(clone.getId(), new LinkedHashMap<>(clone.getData()), new LinkedHashSet<>(clone.getArrayTexts()), new LinkedHashSet<>(clone.getLegacyTexts()), new LinkedHashSet<>(clone.getReplacements()), new LinkedHashSet<>(clone.getPrefixes()), new LinkedHashSet<>(clone.getSuffixes()));
    }
}
