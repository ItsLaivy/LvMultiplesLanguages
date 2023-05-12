package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Message class represents a message in a specific language, with a unique ID that allows
 * quick access to the message using a method. The class comes with default replaces to facilitate
 * message customization.
 *
 * @param <C> The message component type
 *
 * @author Laivy
 * @since 1.0
 */
public interface Message<C> {

    @NotNull Map<@NotNull Locale, @NotNull C> getData();

    /**
     * The language message ID of this Message
     * @return the id
     */
    @Contract(pure = true)
    @NotNull String getId();

    /**
     * Note: If the replacement is an instance of Message of BaseComponent it will be automatically translated
     * @return the untranslated version of the replaces
     */
    @NotNull Set<Object> getReplacements();

    @NotNull C getText(@NotNull Locale locale, @NotNull Object... replaces);
    default @NotNull C getText(@NotNull Locale locale) {
        return this.getText(locale, new Object[0]);
    }

    @NotNull List<@NotNull C> getArray(@NotNull Locale locale, @NotNull Object... replaces);
    default @NotNull List<@NotNull C> getArray(@NotNull Locale locale) {
        return this.getArray(locale, new Object[0]);
    }

    /**
     * The prefixes of this message
     * @return the prefixes list
     */
    @NotNull Set<Object> getPrefixes();

    /**
     * The suffixes of this message
     * @return the suffixes list
     */
    @NotNull Set<Object> getSuffixes();

    boolean isArray(@NotNull Locale locale);

    @NotNull Set<@NotNull Locale> getArrayTexts();

    Locale[] getLocales();

}
