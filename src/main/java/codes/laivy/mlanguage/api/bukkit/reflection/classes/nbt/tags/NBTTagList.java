package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagList extends NBTBase {
    public NBTTagList(List<NBTBase> value) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.LIST, value).getValue());
    }
    public NBTTagList(NBTTagList value) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.LIST).getValue());
        concatenate(value);
    }

    /**
     * Construct a Multiples languages NBTTagList from an NMS NBTTagList
     * @param value a NMS NBTTagList
     */
    public NBTTagList(@Nullable Object value) {
        super(value);
    }

    public @Nullable List<?> getList() {
        return (List<?>) getDefApi().getVersion().getFieldExec("NBTTagList:list").invokeInstance(this);
    }

    public void concatenate(@NotNull NBTTagList compound) {
        getDefApi().getVersion().nbtbaseConcatenate(this, compound.getValue());
    }

    @Override
    public @NotNull Object getValue() {
        if (super.getValue() == null) {
            setValue(getDefApi().getVersion().nbtTag(Version.NBTTag.LIST).getValue());
        }

        return super.getValue();
    }

    @Override
    public @NotNull NBTTagListClass getClassExecutor() {
        return (NBTTagListClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagList");
    }

    public static class NBTTagListClass extends NBTBaseClass {
        public NBTTagListClass(@NotNull String className) {
            super(className);
        }
    }
}
