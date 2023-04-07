package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTBase extends ObjectExecutor {
    public NBTBase(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTBaseClass getClassExecutor() {
        return (NBTBaseClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase");
    }

    /**
     * This returns the NBTTag class of the object
     * @param base the nbt object
     * @return the object class of this nbt object
     */
    @NotNull
    public static Version.NBTTag getTagType(@NotNull Object base) {
        if (!multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase").isReflectiveInstance(base)) {
            throw new IllegalArgumentException("This object isn't a NBTBase type! you passed a '" + base.getClass().getName() + "'");
        }

        Version.NBTTag tag;
        if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagByte").isReflectiveInstance(base)) {
            tag = Version.NBTTag.BYTE;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagByteArray").isReflectiveInstance(base)) {
            tag = Version.NBTTag.BYTE_ARRAY;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagCompound").isReflectiveInstance(base)) {
            tag = Version.NBTTag.COMPOUND;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagDouble").isReflectiveInstance(base)) {
            tag = Version.NBTTag.DOUBLE;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagFloat").isReflectiveInstance(base)) {
            tag = Version.NBTTag.FLOAT;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagInt").isReflectiveInstance(base)) {
            tag = Version.NBTTag.INT;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagIntArray").isReflectiveInstance(base)) {
            tag = Version.NBTTag.INT_ARRAY;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagList").isReflectiveInstance(base)) {
            tag = Version.NBTTag.LIST;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagLong").isReflectiveInstance(base)) {
            tag = Version.NBTTag.LONG;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagShort").isReflectiveInstance(base)) {
            tag = Version.NBTTag.SHORT;
        } else if (multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagString").isReflectiveInstance(base)) {
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
