package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.lang.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * This is responsible to translate the items
 * @param <T> the item class
 */
public interface ItemTranslator<T> {

    boolean isTranslatable(@NotNull T item);

    void translate(@NotNull T item, @NotNull Locale locale);

    /**
     * Reset the item name and lore to the default locale language
     * This method is called everytime needs to be changed to the default language code
     *
     * @param item the item
     */
    void reset(@NotNull T item);

}
