package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagFloat extends NBTBase {
    public NBTTagFloat(float value) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.FLOAT, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagFloat from an NMS NBTTagFloat
     * @param value a NMS NBTTagFloat
     */
    protected NBTTagFloat(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagFloatClass getClassExecutor() {
        return (NBTTagFloatClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagFloat");
    }

    public static class NBTTagFloatClass extends NBTBaseClass {
        public NBTTagFloatClass(@NotNull String className) {
            super(className);
        }
    }
}
