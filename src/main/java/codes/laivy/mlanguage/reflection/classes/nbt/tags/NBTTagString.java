package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagString extends NBTBase {
    public NBTTagString(@NotNull String string) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.STRING, string).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagString from an NMS NBTTagString
     * @param value a NMS NBTTagString
     */
    public NBTTagString(@Nullable Object value) {
        super(value);
    }

    public @Nullable String getData() {
        return (String) multiplesLanguagesBukkit().getVersion().getFieldExec("NBTTagString:getData").invokeInstance(this);
    }

    @Override
    public @NotNull NBTTagStringClass getClassExecutor() {
        return (NBTTagStringClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagString");
    }

    public static class NBTTagStringClass extends NBTBaseClass {
        public NBTTagStringClass(@NotNull String className) {
            super(className);
        }
    }
}
