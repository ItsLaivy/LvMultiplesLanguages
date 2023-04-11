package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.data.SerializedData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * The message storage stores all the messages and translations
 * @param <C> the component type class
 */
public interface MessageStorage<C> {

    @NotNull Message<C>[] getMessages();

    @NotNull Map<@NotNull String, Map<@NotNull Locale, @NotNull C[]>> getData();

    @NotNull C[] getText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);
    @NotNull C[] getText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces);

    @NotNull Message<C> getMessage(@NotNull String id, @NotNull Object... replaces);

    @Contract(pure = true)
    @NotNull String getName();

    @Contract(pure = true)
    @NotNull Object getPlugin();

    @NotNull Locale getDefaultLocale();

    @NotNull SerializedData serialize();

    /**
     * Merge a message storage into this
     * @param from the message that will be merged with this
     * @return true if the merge has changes, false otherwise
     */
    default boolean merge(@NotNull MessageStorage<C> from) {
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

    @NotNull String replace(@NotNull Locale locale, @NotNull String string, @NotNull Object... replaces);

}
