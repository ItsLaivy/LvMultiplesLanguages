package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *   the IBukkitArrayMessage class extends IBukkitMessage and overrides the #get()
 *   method to return an array of messages as well. However, unlike the BukkitMessage, each value
 *   in the array represents a separate line. When using BukkitArrayMessage, all the lines in the
 *   array will be concatenated to form a single text string.
 * </p>
 * <p>
 *   This allows for more flexibility in formatting messages, where BukkitMessage is suitable for
 *   messages with separate texts, and BukkitArrayMessage is designed for messages with multiple
 *   lines that need to be combined into a single line.
 * </p>
 */
public interface MessageArray<C> extends Message<C> {
    default @NotNull List<@NotNull C[]> getArray(@NotNull Locale locale, @NotNull Object... replaces) {
        return getStorage().getTextArray(locale, getId(), replaces);
    }
    default @NotNull List<@NotNull C[]> getArray(@NotNull Locale locale) {
        return this.getArray(locale, new Object[0]);
    }
    default @NotNull List<@NotNull C[]> getArray(@NotNull UUID uuid, @NotNull Object... replaces) {
        return getStorage().getTextArray(uuid, getId(), replaces);
    }
    default @NotNull List<@NotNull C[]> getArray(@NotNull UUID uuid) {
        return this.getArray(uuid, new Object[0]);
    }
}
