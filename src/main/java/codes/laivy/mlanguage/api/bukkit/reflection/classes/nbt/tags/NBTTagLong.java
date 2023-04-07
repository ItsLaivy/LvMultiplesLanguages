package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagLong extends NBTBase {
    public NBTTagLong(long value) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.LONG, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagLong from an NMS NBTTagLong
     * @param value a NMS NBTTagLong
     */
    protected NBTTagLong(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagLongClass getClassExecutor() {
        return (NBTTagLongClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagLong");
    }

    public static class NBTTagLongClass extends NBTBaseClass {
        public NBTTagLongClass(@NotNull String className) {
            super(className);
        }
    }
}
