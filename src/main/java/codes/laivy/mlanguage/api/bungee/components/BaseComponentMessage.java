package codes.laivy.mlanguage.api.bungee.components;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public interface BaseComponentMessage extends Message<BaseComponent[]> {
    @NotNull BaseComponent[] getText(@NotNull UUID uuid, @NotNull Object... replaces);

    @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull Object... replaces);
    @NotNull String getLegacyText(@NotNull Locale locale, @NotNull Object... replaces);

    boolean isLegacyText(@NotNull Locale locale);

    @NotNull Set<@NotNull Locale> getLegacyTexts();

    default @NotNull BaseComponent[] replace(@NotNull Locale locale, @NotNull BaseComponent[] components, @NotNull Object... replaces) {
        List<BaseComponent> componentList = new LinkedList<>();

        int row = 0;
        for (final BaseComponent component : ComponentUtils.cloneComponent(components)) {
            for (BaseComponent recursive : ComponentUtils.getComponents(component)) {
                if (recursive instanceof TextComponent) {
                    TextComponent text = (TextComponent) recursive;

                    if (text.getText().contains("%s")) {
                        do {
                            if (replaces.length > row) {
                                Object replace = replaces[row];
                                BaseComponent[] index = ComponentUtils.convert(locale, replace);

                                String[] parts = text.getText().split(Pattern.quote("%s"), 2);

                                TextComponent extra = (TextComponent) text.duplicate();
                                TextComponent indexComponent = (TextComponent) text.duplicate();
                                extra.setText(parts[1]);

                                Bukkit.broadcastMessage("Index: '" + indexComponent.getText() + "'");

                                indexComponent.setText("");
                                indexComponent.setExtra(Arrays.asList(index));

                                text.setText(parts[0]);
                                text.addExtra(indexComponent);
                                text.addExtra(extra);

                                Bukkit.broadcastMessage(parts[0].replace("§", "&"));

                                row++;
                            } else {
                                break;
                            }
                        } while (text.getText().contains("%s"));
                    }
                }
            }
        }

        return componentList.toArray(new BaseComponent[0]);
    }

    // Arrays
    default @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getArray(LvMultiplesLanguages.getApi().getLocale(uuid), replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    default @NotNull List<@NotNull String> getLegacyArray(@NotNull UUID uuid, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getLegacyArray(LvMultiplesLanguages.getApi().getLocale(uuid), replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    default @NotNull List<String> getLegacyArray(@NotNull Locale locale, @NotNull Object... replaces) {
        List<BaseComponent[]> components = getArray(locale, replaces);
        List<String> legacy = new LinkedList<>();

        for (BaseComponent[] component : components) {
            legacy.add(ComponentUtils.getText(component));
        }

        return legacy;
    }

    @Override
    default @NotNull List<BaseComponent[]> getArray(@NotNull Locale locale, @NotNull Object... replaces) {
        Locale original = locale;

        if (!getData().containsKey(locale)) {
            locale = getData().keySet().stream().findFirst().orElseThrow(() -> new NullPointerException("Message without data '" + getId() + "'"));
        }

        if (!isArrayText(locale)) {
            throw new UnsupportedOperationException("This text with id '" + getId() + "' and locale '" + locale.name() + "' isn't an array text, use #getText instead (Original: '" + original.name() + "').");
        }

        List<BaseComponent[]> components = new LinkedList<>();

        for (BaseComponent component : getText(locale, replaces)) {
            for (BaseComponent recurring : ComponentUtils.getComponents(component)) {
                if (recurring instanceof TextComponent) {
                    TextComponent text = (TextComponent) recurring;

                    if (text.getText().contains("\n")) {
                        while (text.getText().contains("\n")) {
                            String[] split = text.getText().split("\n", 2);

                            TextComponent t1 = (TextComponent) text.duplicate();
                            TextComponent t2 = (TextComponent) text.duplicate();

                            t1.setText(split[0]);
                            t2.setText(split[1]);

                            components.add(new BaseComponent[] { t1 });

                            text = t2;
                        }

                        recurring = text;
                    }
                }

                components.add(new BaseComponent[] {
                        recurring
                });
            }
        }

        return components;
    }
    // Arrays

    @Override
    @NotNull BaseComponentMessage clone();
}
