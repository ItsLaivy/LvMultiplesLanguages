package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagLong extends NBTBase {
    public NBTTagLong(long value) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.LONG, value).getValue());
    }

    /**
     * Construct a LaivyNPC NBTTagLong from an NMS NBTTagLong
     * @param value a NMS NBTTagLong
     */
    protected NBTTagLong(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagLongClass getClassExecutor() {
        return (NBTTagLongClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagLong");
    }

    public static class NBTTagLongClass extends NBTBaseClass {
        public NBTTagLongClass(@NotNull String className) {
            super(className);
        }
    }
}
