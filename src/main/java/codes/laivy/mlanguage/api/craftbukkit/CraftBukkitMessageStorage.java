package codes.laivy.mlanguage.api.craftbukkit;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

/**
 * The storage with Bukkit/Bungee BaseComponent type
 */
public interface CraftBukkitMessageStorage extends MessageStorage<BaseComponent> {
    default @NotNull String replace(@NotNull Locale locale, @NotNull String string, @NotNull Object... replaces) {
        for (Object replace : replaces) {
            if (!string.contains("%s")) {
                break;
            }

            String index;

            if (replace instanceof Message) {
                //noinspection unchecked
                index = ComponentUtils.getText(ComponentUtils.merge(((Message<BaseComponent>) replace).get(locale)));
            } else if (replace instanceof BaseComponent) {
                index = ComponentUtils.getText((BaseComponent) replace);
            } else if (replace instanceof BaseComponent[]) {
                index = ComponentUtils.getText((BaseComponent[]) replace);
            } else {
                index = String.valueOf(replace);
            }

            string = string.replaceFirst("%s", index);
        }
        return string;
    }
}
