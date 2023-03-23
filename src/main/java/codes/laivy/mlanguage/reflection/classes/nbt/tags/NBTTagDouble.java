package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagDouble extends NBTBase {
    public NBTTagDouble(double value) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.DOUBLE, value).getValue());
    }

    /**
     * Construct a LaivyNPC NBTTagDouble from an NMS NBTTagDouble
     * @param value a NMS NBTTagDouble
     */
    protected NBTTagDouble(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagDoubleClass getClassExecutor() {
        return (NBTTagDoubleClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagDouble");
    }

    public static class NBTTagDoubleClass extends NBTBaseClass {
        public NBTTagDoubleClass(@NotNull String className) {
            super(className);
        }
    }
}
