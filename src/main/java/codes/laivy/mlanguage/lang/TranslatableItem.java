package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslatableItem<T, C> {

    @Contract(pure = true)
    @NotNull T getItem();

    @Nullable Message<C> getName();

    @Nullable Message<C> getLore();

}
