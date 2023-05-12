package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslatableItem<I> {

    @Contract(pure = true)
    @NotNull I getItem();

    @Nullable StoredMessage getName();

    @Nullable StoredMessage getLore();

}
