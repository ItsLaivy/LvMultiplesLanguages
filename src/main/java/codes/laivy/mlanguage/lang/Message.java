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
public interface Message<C> extends Cloneable {

    @NotNull Map<@NotNull Locale, @NotNull C> getData();

    /**
     * The language message ID of this Message
     * @return the id
     */
    @Contract(pure = true)
    @NotNull String getId();

    /**
     * Note: If the replacement is an instance of Message,
     * BaseComponent or BaseComponent[] (for Bukkit/Bungee); Works for Collections and arrays too.
     * It will be automatically translated
     * @return the untranslated version of the replacing
     */
    @NotNull List<Object> getReplacements();

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
     * @return the prefix list
     */
    @NotNull List<Object> getPrefixes();

    /**
     * The suffixes of this message
     * @return the suffix list
     */
    @NotNull List<Object> getSuffixes();

    boolean isArrayText(@NotNull Locale locale);

    @NotNull Set<@NotNull Locale> getArrayTexts();

    Locale[] getLocales();

    @NotNull Message<C> clone();

}
