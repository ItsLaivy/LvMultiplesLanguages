package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.data.SerializedData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The Message class represents a message in a specific language, with a unique ID that allows
 * quick access to the message using a method. The class comes with default replaces to facilitate
 * message customization.
 *
 * @author Laivy
 * @since 1.0
 */
public interface Message {

    /**
     * The language of this Message
     * @return the language
     */
    @NotNull MessageStorage getStorage();

    /**
     * The language message ID of this Message
     * @return the id
     */
    @NotNull String getId();

    /**
     * The default replaces of this message
     * @return the default replaces
     * @param locale the locale
     */
    default @NotNull BaseComponent[] getReplaces(@Nullable Locale locale) {
        Set<BaseComponent> componentSet = new LinkedHashSet<>();
        for (Message message : getReplaces()) {
            componentSet.addAll(Arrays.asList(message.get(locale)));
        }
        return componentSet.toArray(new BaseComponent[0]);
    }

    /**
     * @return the untranslated version of the replaces
     */
    @NotNull Message[] getReplaces();

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @Nullable Object... replaces) {
        BaseComponent[] mReplaces = getReplaces(locale);
        final Object[] finalReplaces = new Object[mReplaces.length + replaces.length];

        int row = 0;
        for (Object replace : mReplaces) {
            finalReplaces[row] = replace;
            row++;
        }
        for (Object replace : replaces) {
            finalReplaces[row] = replace;
            row++;
        }

        return getStorage().get(locale, getId(), finalReplaces);
    }

    default @NotNull BaseComponent[] get(@Nullable Locale locale) {
        return this.get(locale, new Object[0]);
    }

    @NotNull SerializedData serialize();

}
