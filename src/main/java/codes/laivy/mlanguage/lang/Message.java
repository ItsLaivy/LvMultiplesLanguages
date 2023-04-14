package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    /**
     * The language of this Message
     * @return the language
     */
    @Contract(pure = true)
    @NotNull MessageStorage<C> getStorage();

    @NotNull Map<@NotNull Locale, @NotNull C[]> getData();

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
    @Contract(pure = true)
    @NotNull Object[] getReplacements();

    default @NotNull C[] get(@NotNull UUID uuid, @NotNull Object... replaces) {
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

        return getStorage().getText(uuid, getId(), finalReplaces);
    }
    default @NotNull C[] get(@NotNull UUID uuid) {
        return this.get(uuid, new Object[0]);
    }

    default @NotNull C[] get(@Nullable Locale locale, @NotNull Object... replaces) {
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

    default @NotNull C[] get(@Nullable Locale locale) {
        return this.get(locale, new Object[0]);
    }

    /**
     * The prefixes of this message
     * Note: The object can be a string, or another message
     * @return the prefixes list
     */
    @Unmodifiable
    @NotNull List<@NotNull Object> getPrefixes();

    /**
     * The suffixes of this message
     * Note: The object can be a string, or another message
     * @return the suffixes list
     */
    @Unmodifiable
    @NotNull List<@NotNull Object> getSuffixes();

    default boolean isArray(@NotNull Locale locale) {
        return getStorage().isArray(this, locale);
    }

    default Locale[] getLocales() {
        return getData().keySet().toArray(new Locale[0]);
    }

    @NotNull SerializedData serialize();
}
