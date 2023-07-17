package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StoredMessage {

    @NotNull MessageStorage<?, ?> getStorage();

    @NotNull Message<?> getMessage();

    @NotNull List<Object> getReplacements();

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

}
