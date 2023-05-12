package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagByteArray extends NBTBase {
    public NBTTagByteArray(byte[] value) {
        //noinspection PrimitiveArrayArgumentToVarargsMethod
        super(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.BYTE_ARRAY, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagByteArray from an NMS NBTTagByteArray
     * @param value a NMS NBTTagByteArray
     */
    protected NBTTagByteArray(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagByteArrayClass getClassExecutor() {
        return (NBTTagByteArrayClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagByteArray");
    }

    public static class NBTTagByteArrayClass extends NBTBaseClass {
        public NBTTagByteArrayClass(@NotNull String className) {
            super(className);
        }
    }
}
