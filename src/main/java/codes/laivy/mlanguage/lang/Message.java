package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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

    @NotNull Map<@NotNull Locale, @NotNull BaseComponent[]> getData();

    /**
     * The language message ID of this Message
     * @return the id
     */
    @NotNull String getId();

    /**
     * Note: If the replacement is an instance of Message of BaseComponent it will be automatically translated
     * @return the untranslated version of the replaces
     */
    @NotNull Object[] getReplacements();

    default @NotNull BaseComponent[] get(@Nullable Locale locale, @Nullable Object... replaces) {
        Object[] mReplaces = getReplacements();
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

        return getStorage().getText(locale, getId(), finalReplaces);
    }

    default @NotNull BaseComponent[] get(@Nullable Locale locale) {
        return this.get(locale, new Object[0]);
    }

    @NotNull SerializedData serialize();
}
