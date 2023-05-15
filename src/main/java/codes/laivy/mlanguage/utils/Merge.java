package codes.laivy.mlanguage.utils;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@ApiStatus.Internal
public final class Merge {
    private final @NotNull Set<String> merged;
    private final @NotNull Set<String> unused;

    public Merge(@NotNull Set<String> merged, @NotNull Set<String> unused) {
        this.merged = merged;
        this.unused = unused;
    }

    public @NotNull Set<String> getMerged() {
        return merged;
    }

    public @NotNull Set<String> getUnused() {
        return unused;
    }
}
