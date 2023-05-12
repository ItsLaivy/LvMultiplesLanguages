package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.plugin.PluginProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * The message storage stores all the messages and translations
 * @param <C> the component type class
 */
public interface MessageStorage<C, M extends Message<C>> {

    @NotNull Set<M> getMessages();
    @NotNull M getMessage(@NotNull String id);

    @NotNull List<@NotNull C> getTextArray(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);
    @NotNull C getText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);

    @Contract(pure = true)
    @NotNull String getName();

    @Contract(pure = true)
    @NotNull PluginProperty getPluginProperty();

    @NotNull Locale getDefaultLocale();

    boolean isArray(@NotNull String id, @NotNull Locale locale);

    default boolean isArray(@NotNull Message<C> message, @NotNull Locale locale) {
        return isArray(message.getId(), locale);
    }

    /**
     * Loads the language
     */
    void load();

    /**
     * Unloads the language
     */
    void unload();

}
