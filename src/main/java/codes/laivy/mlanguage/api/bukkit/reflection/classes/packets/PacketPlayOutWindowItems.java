package codes.laivy.mlanguage.api.bukkit.reflection.classes.packets;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_17_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class PacketPlayOutWindowItems extends ObjectExecutor {
    public PacketPlayOutWindowItems(@Nullable Object value) {
        super(value);
    }

    public int getWindowId() {
        //noinspection DataFlowIssue
        return (int) getDefApi().getVersion().getFieldExec("PacketPlayOutWindowItems:windowId").invokeInstance(this);
    }
    public @Nullable ItemStack[] getItems() {
        return getDefApi().getVersion().getWindowItemsPacketItems(this);
    }

    public int getStateId() {
        if (!ReflectionUtils.isCompatible(V1_17_R1.class)) {
            V1_17_R1 v = (V1_17_R1) getDefApi().getVersion();

            if (!v.isStateEnabled()) {
                throw new UnsupportedOperationException("This method is only available since 1.17.1");
            }
        }

        //noinspection DataFlowIssue
        return (int) getDefApi().getVersion().getFieldExec("PacketPlayOutWindowItems:state").invokeInstance(this);
    }
    public @NotNull ItemStack getHeldItem() {
        if (!ReflectionUtils.isCompatible(V1_17_R1.class)) {
            V1_17_R1 v = (V1_17_R1) getDefApi().getVersion();

            if (!v.isStateEnabled()) {
                throw new UnsupportedOperationException("This method is only available since 1.17.1");
            }
        }

        return new ItemStack(getDefApi().getVersion().getFieldExec("PacketPlayOutWindowItems:held").invokeInstance(this));
    }

    @Override
    public @NotNull PacketPlayOutWindowItemsClass getClassExecutor() {
        return (PacketPlayOutWindowItemsClass) getDefApi().getVersion().getClassExec("PacketPlayOutWindowItems");
    }

    public static class PacketPlayOutWindowItemsClass extends ClassExecutor {
        public PacketPlayOutWindowItemsClass(@NotNull String className) {
            super(className);
        }
    }
}
