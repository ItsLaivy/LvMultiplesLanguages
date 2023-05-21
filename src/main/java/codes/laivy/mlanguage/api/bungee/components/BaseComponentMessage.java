package codes.laivy.mlanguage.api.bungee.components;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface BaseComponentMessage extends Message<BaseComponent[]> {
    @NotNull BaseComponent[] getText(@NotNull UUID uuid, @NotNull Object... replaces);
    @NotNull List<@NotNull BaseComponent[]> getArray(@NotNull UUID uuid, @NotNull Object... replaces);

    @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull Object... replaces);
    @NotNull String getLegacyText(@NotNull Locale locale, @NotNull Object... replaces);

    @NotNull List<@NotNull String> getLegacyArray(@NotNull UUID uuid, @NotNull Object... replaces);
    @NotNull List<String> getLegacyArray(@NotNull Locale locale, @NotNull Object... replaces);

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

                            if (replace instanceof BukkitMessage) {
                                index = ((BukkitMessage) replace).getText(locale);
                            } else if (replace instanceof BaseComponent) {
                                index = new BaseComponent[] { (BaseComponent) replace };
                            } else if (replace instanceof BaseComponent[]) {
                                index = (BaseComponent[]) replace;
                            } else {
                                index = new BaseComponent[] { new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(replace))) };
                            }

                            // TODO: 11/05/2023 Component-based replace
                            text.setText(text.getText().replaceFirst("%s", ComponentUtils.getText(index)));
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
}
