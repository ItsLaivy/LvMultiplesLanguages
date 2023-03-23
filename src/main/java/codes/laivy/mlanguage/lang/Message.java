package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

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
    @NotNull Language getLanguage();

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
    @NotNull BaseComponent[] getReplaces(@NotNull Locale locale);

    @NotNull BaseComponent get(@NotNull Locale locale);

    @NotNull SerializedData serialize();

}
