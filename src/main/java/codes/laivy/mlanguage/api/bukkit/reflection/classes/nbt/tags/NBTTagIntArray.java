package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagIntArray extends NBTBase {
    public NBTTagIntArray(int[] value) {
        //noinspection PrimitiveArrayArgumentToVarargsMethod
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.INT_ARRAY, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagIntArray from an NMS NBTTagIntArray
     * @param value a NMS NBTTagIntArray
     */
    protected NBTTagIntArray(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagIntArrayClass getClassExecutor() {
        return (NBTTagIntArrayClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagIntArray");
    }

    public static class NBTTagIntArrayClass extends NBTBaseClass {
        public NBTTagIntArrayClass(@NotNull String className) {
            super(className, true);
        }
    }
}
