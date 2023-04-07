package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagDouble extends NBTBase {
    public NBTTagDouble(double value) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.DOUBLE, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagDouble from an NMS NBTTagDouble
     * @param value a NMS NBTTagDouble
     */
    protected NBTTagDouble(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagDoubleClass getClassExecutor() {
        return (NBTTagDoubleClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagDouble");
    }

    public static class NBTTagDoubleClass extends NBTBaseClass {
        public NBTTagDoubleClass(@NotNull String className) {
            super(className);
        }
    }
}
