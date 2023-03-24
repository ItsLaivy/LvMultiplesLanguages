package codes.laivy.mlanguage.reflection.classes.packets;

import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class PacketPlayOutSetSlot extends Packet {
    public PacketPlayOutSetSlot(@Nullable Object value) {
        super(value);
    }

    public int getWindowId() {
        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("PacketPlayOutSetSlot:windowId").invokeInstance(this);
    }
    public int getSlot() {
        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("PacketPlayOutSetSlot:slot").invokeInstance(this);
    }
    public ItemStack getItemStack() {
        return new ItemStack(multiplesLanguagesBukkit().getVersion().getFieldExec("PacketPlayOutSetSlot:item").invokeInstance(this));
    }

    @Override
    public @NotNull PacketPlayOutSetSlotClass getClassExecutor() {
        return (PacketPlayOutSetSlotClass) multiplesLanguagesBukkit().getVersion().getClassExec("PacketPlayOutSetSlot");
    }

    public static final class PacketPlayOutSetSlotClass extends PacketClass {
        public PacketPlayOutSetSlotClass(@NotNull String className) {
            super(className);
        }
    }
}
