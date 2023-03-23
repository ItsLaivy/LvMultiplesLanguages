package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagByte extends NBTBase {
    public NBTTagByte(byte value) {
        super(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.BYTE, value).getValue());
    }

    /**
     * Construct a LaivyNPC NBTTagByte from an NMS NBTTagByte
     * @param value a NMS NBTTagByte
     */
    protected NBTTagByte(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagByteClass getClassExecutor() {
        return (NBTTagByteClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagByte");
    }

    public static class NBTTagByteClass extends NBTBaseClass {
        public NBTTagByteClass(@NotNull String className) {
            super(className);
        }
    }
}
