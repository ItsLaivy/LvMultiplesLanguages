package codes.laivy.mlanguage.api.craftbukkit;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The storage with Bukkit/Bungee BaseComponent type
 */
public interface CraftBukkitMessageStorage extends MessageStorage<BaseComponent> {

    /**
     * Clones the components and replaces the %s strings to the replaces parameter
     * @param locale the locale
     * @param components the components array
     * @param replaces the replaces
     * @return an array with copies of components replaced
     */
    default @NotNull BaseComponent[] replace(@NotNull Locale locale, @NotNull BaseComponent[] components, @NotNull Object... replaces) {
        Set<BaseComponent> componentSet = new LinkedHashSet<>();

        int row = 0;
        for (final BaseComponent component : ComponentUtils.cloneComponent(components)) {
            for (BaseComponent recursive : ComponentUtils.getComponents(component)) {
                if (replaces.length > row) {
                    Object replace = replaces[row];
                    BaseComponent index;

                    if (replace instanceof Message) {
                        //noinspection unchecked
                        index = ComponentUtils.merge(((Message<BaseComponent>) replace).getText(locale));
                    } else if (replace instanceof BaseComponent) {
                        index = (BaseComponent) replace;
                    } else if (replace instanceof BaseComponent[]) {
                        index = new TextComponent((BaseComponent[]) replace);
                    } else {
                        index = new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(replace)));
                    }

                    if (recursive instanceof TextComponent) {
                        TextComponent text = (TextComponent) recursive;

                        if (text.getText().contains("%s")) {
                            String[] split = text.getText().split("%s");

                            if (split.length != 0) {
                                int splitRow = 0;
                                text.setText("");

                                for (String splitText : split) {
                                    text.setText(splitText);
                                    if (splitRow % 2 == 0) { // Before %s
                                        componentSet.add(index); // Add the %s replacement
                                    }
                                    splitRow++;
                                }
                            }

                            row++;
                        }
                    }
                }
            }

            componentSet.add(component);
        }

        return componentSet.toArray(new BaseComponent[0]);
    }

    /**
     * Checks if the text is considered as legacy text.
     * A legacy text will be serialized as text, not as a base component json
     *
     * @param id the message id
     * @param locale the locale
     * @return {@code true} if the message will be serialized as text, {@code false} otherwise will be serialized as base component
     **/
    boolean isLegacyText(@NotNull String id, @NotNull Locale locale);

}
