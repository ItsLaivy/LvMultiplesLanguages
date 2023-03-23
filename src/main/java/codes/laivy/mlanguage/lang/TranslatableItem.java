package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslatableItem<T> {

    @NotNull T getItem();

    @Nullable Message getName();
    @Nullable Message getLore();

}
