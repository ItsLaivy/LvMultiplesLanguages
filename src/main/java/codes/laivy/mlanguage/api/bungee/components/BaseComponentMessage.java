package codes.laivy.mlanguage.api.bungee.components;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
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

                    while (text.getText().contains("%s")) {
                        if (replaces.length > row) {
                            Object replace = replaces[row];
                            BaseComponent[] index;

                            // TODO: 23/05/2023 Enhance this
                            if (replace instanceof BaseComponentMessage) {
                                index = ((BaseComponentMessage) replace).getText(locale);
                            } else if (replace instanceof BaseComponent) {
                                index = new BaseComponent[] { (BaseComponent) replace };
                            } else if (replace instanceof BaseComponent[]) {
                                index = (BaseComponent[]) replace;
                            } else if (replace instanceof Collection) {
                                Collection<?> collection = (Collection<?>) replace;
                                List<BaseComponent> componentList2 = new LinkedList<>();

                                int r = 0;
                                for (Object object : collection) {
                                    if (r > 0) componentList2.add(new TextComponent("\n"));

                                    if (object instanceof BaseComponent) {
                                        componentList2.add((BaseComponent) object);
                                    } else if (object instanceof BaseComponent[]) {
                                        componentList2.add(new TextComponent((BaseComponent[]) object));
                                    } else if (object instanceof BaseComponentMessage) {
                                        componentList2.add(new TextComponent(((BaseComponentMessage) object).getText(locale)));
                                    } else {
                                        componentList2.add(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(object))));
                                    }

                                    r++;
                                }

                                index = componentList2.toArray(new BaseComponent[0]);
                            } else {
                                index = new BaseComponent[] { new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(replace))) };
                            }

                            // TODO: 11/05/2023 Component-based replace
                            text.setText(text.getText().replaceFirst(Pattern.quote("%s"), Matcher.quoteReplacement(ComponentUtils.getText(index))));
                            row++;
                        } else {
                            break;
                        }
                    }
                }
            }

            componentList.add(component);
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
            if (component instanceof TextComponent) {
                TextComponent text = (TextComponent) component;

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
                    component = text;
                }
            }

            components.add(new BaseComponent[] {
                    component
            });
        }

        return components;
    }
    // Arrays
}
