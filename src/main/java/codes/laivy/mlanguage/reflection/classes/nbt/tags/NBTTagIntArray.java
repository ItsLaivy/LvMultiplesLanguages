package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagIntArray extends NBTBase {
    public NBTTagIntArray(int[] value) {
        //noinspection PrimitiveArrayArgumentToVarargsMethod
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.INT_ARRAY, value).getValue());
    }

    /**
     * Construct a LaivyNPC NBTTagIntArray from an NMS NBTTagIntArray
     * @param value a NMS NBTTagIntArray
     */
    protected NBTTagIntArray(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagIntArrayClass getClassExecutor() {
        return (NBTTagIntArrayClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagIntArray");
    }

    public static class NBTTagIntArrayClass extends NBTBaseClass {
        public NBTTagIntArrayClass(@NotNull String className) {
            super(className, true);
        }
    }
}
