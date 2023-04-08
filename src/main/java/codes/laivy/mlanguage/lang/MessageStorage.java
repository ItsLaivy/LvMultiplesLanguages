package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.api.bukkit.natives.BukkitMessageStorage;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface MessageStorage {

    @NotNull Message[] getMessages();

    @NotNull Map<@NotNull String, Map<@NotNull Locale, @NotNull BaseComponent[]>> getData();

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull BaseComponent... replaces) {
        return get(locale, id, (Object[]) replaces);
    }
    @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message get(@NotNull String id, @NotNull Message... replaces);

    @NotNull String getName();
    @NotNull Object getPlugin();

    @NotNull Locale getDefaultLocale();

    @NotNull SerializedData serialize();

    /**
     * Merge a message storage into this
     * @param from the message that will be merged with this
     * @return true if the merge has changes, false otherwise
     */
    default boolean merge(@NotNull MessageStorage from) {
        throw new UnsupportedOperationException("This message storage doesn't supports merges");
    }

    /**
     * Loads the language
     */
    void load();

    /**
     * Unloads the language
     */
    void unload();

    default @NotNull String replace(@NotNull Locale locale, @NotNull String string, @NotNull Object... replaces) {
        for (Object replace : replaces) {
            if (!string.contains("%s")) {
                break;
            }

            String index;

            if (replace instanceof Message) {
                index = ComponentUtils.getText(ComponentUtils.merge(((Message) replace).get(locale)));
            } else if (replace instanceof BaseComponent) {
                index = ComponentUtils.getText((BaseComponent) replace);
            } else {
                index = String.valueOf(replace);
            }

            string = string.replaceFirst("%s", index);
        }
        return string;
    }

}
