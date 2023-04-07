package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.StringObjExec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagCompound extends NBTBase {

    public NBTTagCompound(@NotNull NBTTagCompound compound) {
        this(compound.getValue());
        concatenate(compound);
    }

    /**
     * Construct a Multiples Languages NBTTagCompound from an NMS NBTTagCompound
     * @param value a NMS NBTTagCompound
     */
    public NBTTagCompound(@Nullable Object value) {
        super(value);
    }

    public void concatenate(@NotNull NBTTagCompound compound) {
        getDefApi().getVersion().nbtbaseConcatenate(this, compound);
    }

    @Override
    public @NotNull Object getValue() {
        if (super.getValue() == null) {
            setValue(getDefApi().getVersion().nbtTag(Version.NBTTag.COMPOUND).getValue());
        }

        return super.getValue();
    }

    public void set(@NotNull String key, @NotNull Object base) {
        this.set(key, getDefApi().getVersion().nbtTag(NBTBase.getTagType(base), base));
    }
    public void set(@NotNull String key, @NotNull NBTBase base) {
        getDefApi().getVersion().nbtCompound(Version.NBTCompoundAction.SET, this, new StringObjExec(key), base);
    }
    public NBTBase get(@NotNull String key) {
        return new NBTBase(getDefApi().getVersion().nbtCompound(Version.NBTCompoundAction.GET, this, new StringObjExec(key), null));
    }
    public void remove(@NotNull String key) {
        getDefApi().getVersion().nbtCompound(Version.NBTCompoundAction.REMOVE, this, new StringObjExec(key), null);
    }
    public boolean contains(@NotNull String key) {
        //noinspection ConstantConditions
        return (boolean) getDefApi().getVersion().nbtCompound(Version.NBTCompoundAction.CONTAINS, this, new StringObjExec(key), null);
    }
    public Set<String> keySet() {
        //noinspection unchecked
        return (Set<String>) getDefApi().getVersion().nbtCompound(Version.NBTCompoundAction.KEY_SET, this, null, null);
    }

    @Override
    public @NotNull NBTTagCompoundClass getClassExecutor() {
        return (NBTTagCompoundClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagCompound");
    }

    public static class NBTTagCompoundClass extends NBTBaseClass {
        public NBTTagCompoundClass(@NotNull String className) {
            super(className);
        }
    }
}
