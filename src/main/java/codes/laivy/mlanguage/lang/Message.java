package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull MessageStorage getLanguage();

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
            componentSet.add(message.get(locale));
        }
        return componentSet.toArray(new BaseComponent[0]);
    }

    /**
     * @return the untranslated version of the replaces
     */
    @NotNull Message[] getReplaces();

    @NotNull BaseComponent get(@Nullable Locale locale);

    @NotNull SerializedData serialize();

}
