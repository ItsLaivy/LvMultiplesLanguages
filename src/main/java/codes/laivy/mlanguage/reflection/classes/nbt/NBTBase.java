package codes.laivy.mlanguage.reflection.classes.nbt;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public abstract class NBTBase extends ObjectExecutor {
    protected NBTBase(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTBaseClass getClassExecutor() {
        return (NBTBaseClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase");
    }

    /**
     * This returns the NBTTag class of the object
     * @param base the nbt object
     * @return the object class of this nbt object
     */
    @NotNull
    public static Version.NBTTag getTagType(@NotNull Object base) {
        if (!multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase").isReflectiveInstance(base)) {
            throw new IllegalArgumentException("This object isn't a NBTBase type! you passed a '" + base.getClass().getName() + "'");
        }

        Version.NBTTag tag;
        if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagByte").isReflectiveInstance(base)) {
            tag = Version.NBTTag.BYTE;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagByteArray").isReflectiveInstance(base)) {
            tag = Version.NBTTag.BYTE_ARRAY;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagCompound").isReflectiveInstance(base)) {
            tag = Version.NBTTag.COMPOUND;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagDouble").isReflectiveInstance(base)) {
            tag = Version.NBTTag.DOUBLE;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagFloat").isReflectiveInstance(base)) {
            tag = Version.NBTTag.FLOAT;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagInt").isReflectiveInstance(base)) {
            tag = Version.NBTTag.INT;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagIntArray").isReflectiveInstance(base)) {
            tag = Version.NBTTag.INT_ARRAY;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagList").isReflectiveInstance(base)) {
            tag = Version.NBTTag.LIST;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagLong").isReflectiveInstance(base)) {
            tag = Version.NBTTag.LONG;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagShort").isReflectiveInstance(base)) {
            tag = Version.NBTTag.SHORT;
        } else if (multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagString").isReflectiveInstance(base)) {
            tag = Version.NBTTag.STRING;
        } else {
            throw new IllegalArgumentException("Cannot identify that NBTTag '" + base.getClass().getName() + "'");
        }
        return tag;
    }

    public static class NBTBaseClass extends ClassExecutor {
        public NBTBaseClass(@NotNull String className, boolean array) {
            super(className, array);
        }
        public NBTBaseClass(@NotNull String className) {
            super(className);
        }
    }
}
