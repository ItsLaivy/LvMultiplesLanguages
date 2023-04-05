package codes.laivy.mlanguage.reflection.versions;

import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.classes.player.CraftPlayer;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.reflection.classes.player.NetworkManager;
import codes.laivy.mlanguage.reflection.classes.player.PlayerConnection;
import codes.laivy.mlanguage.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.Executor;
import codes.laivy.mlanguage.reflection.executors.FieldExecutor;
import codes.laivy.mlanguage.reflection.executors.MethodExecutor;
import codes.laivy.mlanguage.reflection.objects.IntegerObjExec;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import io.netty.channel.Channel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class V1_18_R1 extends V1_17_R1 {

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_17_R1.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        } else if (version == V1_14_R1.class) {
            if (executor instanceof MethodExecutor) {
                if (key.equals("NBTTagCompound:set")) {
                    return false;
                }
            }
        } else if (version == V1_13_R1.class) {
            if (executor instanceof MethodExecutor) {
                if (key.equals("NBTTagCompound:keySet")) {
                    return false;
                }
            }
        } else if (version == V1_8_R3.class) {
            if (key.equals("NetworkManager:channel")) {
                return false;
            }
        } else if (version == V1_8_R1.class) {
            if (key.equals("PacketPlayOutSetSlot:windowId") || key.equals("PacketPlayOutSetSlot:slot") || key.equals("Container:windowId") || key.equals("PlayerConnection:networkManager") || key.equals("EntityPlayer:activeContainer") || key.equals("PacketPlayOutSetSlot:item") || key.equals("ItemStack:tag") || key.equals("NBTTagString:getData") || key.equals("ChatSerializer:convertToBase") || key.equals("NBTTagCompound:get") || key.equals("NBTTagCompound:remove") || key.equals("NBTTagCompound:contains") || key.equals("NBTTagCompound:isEmpty") || key.equals("PlayerConnection:sendPacket")) {
                return false;
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public @NotNull PacketPlayOutSetSlot createSetSlotPacket(int windowId, int slot, int state, @NotNull ItemStack itemStack) {
        return new PacketPlayOutSetSlot(getClassExec("PacketPlayOutSetSlot").getConstructor(ClassExecutor.INT, ClassExecutor.INT, ClassExecutor.INT, multiplesLanguagesBukkit().getVersion().getClassExec("ItemStack")).newInstance(new IntegerObjExec(windowId), new IntegerObjExec(state), new IntegerObjExec(slot), itemStack));
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_18_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.nbt.NBTBase"));

        load(V1_18_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.nbt.NBTTagByte"));
        load(V1_18_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.nbt.NBTTagByteArray"));
        load(V1_18_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.nbt.NBTTagCompound"));
        load(V1_18_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.nbt.NBTTagDouble"));
        load(V1_18_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.nbt.NBTTagFloat"));
        load(V1_18_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.nbt.NBTTagInt"));
        load(V1_18_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.nbt.NBTTagIntArray"));
        load(V1_18_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.nbt.NBTTagList"));
        load(V1_18_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.nbt.NBTTagLong"));
        load(V1_18_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.nbt.NBTTagShort"));
        load(V1_18_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.nbt.NBTTagString"));
        // Items
        load(V1_18_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.world.item.ItemStack"));
        load(V1_18_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack"));
        load(V1_18_R1.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_18_R1.inventory.CraftMetaItem"));
        // Packets
        load(V1_18_R1.class, "Packet", new Packet.PacketClass("net.minecraft.network.protocol.Packet"));
        load(V1_18_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.network.protocol.game.PacketPlayOutSetSlot"));
        // Player
        load(V1_18_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.level.EntityPlayer"));
        load(V1_18_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer"));
        load(V1_18_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.network.PlayerConnection"));
        load(V1_18_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.network.NetworkManager"));
        // Inventory
        load(V1_18_R1.class, "Container", new Container.ContainerClass("net.minecraft.world.inventory.Container"));
        // Chat
        load(V1_18_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.network.chat.IChatBaseComponent"));
        load(V1_18_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        load(V1_18_R1.class, "ChatSerializer:convertToBase", new MethodExecutor(getClassExec("IChatBaseComponent"), ClassExecutor.STRING, "a", "Converts a IChatBaseComponent to a string"));

        load(V1_18_R1.class, "NBTTagCompound:set", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "a", "Sets a value inside a NBTTagCompound", ClassExecutor.STRING, getClassExec("NBTBase")));
        load(V1_18_R1.class, "NBTTagCompound:get", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "c", "Gets a value inside a NBTTagCompound", ClassExecutor.STRING));
        load(V1_18_R1.class, "NBTTagCompound:remove", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.VOID, "r", "Removes a value from a NBTTagCompound", ClassExecutor.STRING));
        load(V1_18_R1.class, "NBTTagCompound:contains", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "e", "Check if a NBTTagCompound contains a key", ClassExecutor.STRING));
        load(V1_18_R1.class, "NBTTagCompound:isEmpty", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "f", "Check if a NBTTagCompound is empty"));

        load(V1_18_R1.class, "PlayerConnection:sendPacket", new MethodExecutor(getClassExec("PlayerConnection"), ClassExecutor.VOID, "a", "Sends a packet to a PlayerConnection", getClassExec("Packet")));

        load(V1_18_R1.class, "NBTTagCompound:keySet", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), new ClassExecutor(Set.class) {}, "d", "Gets a NBTTagCompound's keys"));
    }

    @Override
    public void loadFields() {
        super.loadFields();

        load(V1_18_R1.class, "PacketPlayOutSetSlot:windowId", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "c", "Gets the windowId of the PacketPlayOutSetSlot packet"));
        load(V1_18_R1.class, "PacketPlayOutSetSlot:state", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "d", "Gets the state of the PacketPlayOutSetSlot packet"));
        load(V1_18_R1.class, "PacketPlayOutSetSlot:slot", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "e", "Gets the slot of the PacketPlayOutSetSlot packet"));
        load(V1_18_R1.class, "PacketPlayOutSetSlot:item", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), getClassExec("ItemStack"), "f", "Gets the item of the PacketPlayOutSetSlot packet"));

        load(V1_18_R1.class, "NBTTagString:getData", new FieldExecutor(getClassExec("NBTBase:NBTTagString"), ClassExecutor.STRING, "A", "Gets a NBTTagString's data"));
        load(V1_18_R1.class, "ItemStack:tag", new FieldExecutor(getClassExec("ItemStack"), getClassExec("NBTBase:NBTTagCompound"), "u", "Gets the item NBT tag"));
        load(V1_18_R1.class, "EntityPlayer:activeContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bV", "Gets the active container inventory of an EntityPlayer"));
        load(V1_18_R1.class, "EntityPlayer:defaultContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bW", "Gets the default container inventory of an EntityPlayer"));
        load(V1_18_R1.class, "PlayerConnection:networkManager", new FieldExecutor(getClassExec("PlayerConnection"), getClassExec("NetworkManager"), "a", "Gets the NetworkManager of a PlayerConnection"));
        load(V1_18_R1.class, "Container:windowId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "j", "Gets the id of a Container"));
        load(V1_18_R1.class, "Container:stateId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "q", "Gets the state id of a Container"));
        load(V1_18_R1.class, "NetworkManager:channel", new FieldExecutor(getClassExec("NetworkManager"), new ClassExecutor(Channel.class), "k", "Gets the Channel of a NetworkManager"));
    }

    @Override
    public void translateInventory(@NotNull Player player) {
        BukkitItemTranslator translator = multiplesLanguagesBukkit().getApi().getItemTranslator();

        try {
            EntityPlayer entityPlayer = EntityPlayer.getEntityPlayer(player);
            Set<PacketPlayOutSetSlot> packets = new LinkedHashSet<>();

            PlayerInventory playerInventory = player.getInventory();
            InventoryView view = player.getOpenInventory();
            // State id
            int activeState = 0;
            int defaultState = 0;

            if (ReflectionUtils.isCompatible(V1_18_R1.class)) {
                activeState = entityPlayer.getActiveContainer().getStateId();
                entityPlayer.getActiveContainer().setStateId(activeState + 1);
                defaultState = entityPlayer.getDefaultContainer().getStateId();
                entityPlayer.getDefaultContainer().setStateId(defaultState + 1);
            }
            // Armor translation
            int slot = 5;
            for (org.bukkit.inventory.ItemStack armor : playerInventory.getArmorContents()) {
                if (armor != null && armor.getType() != Material.AIR) {
                    if (translator.isTranslatable(armor)) {
                        packets.add(translator.translate(armor, player, entityPlayer.getActiveContainer().getId(), slot, activeState));
                    }
                }
                slot++;
            }
            // Inventory translation
            for (org.bukkit.inventory.ItemStack item : playerInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    if (translator.isTranslatable(item)) {
                        final int dSlot;

                        if (slot == 49) {
                            dSlot = 45;
                        } else if ((slot - 9) < 9) {
                            dSlot = slot + 27;
                        } else {
                            dSlot = slot - 9;
                        }

                        packets.add(translator.translate(item.clone(), player, 0, dSlot, activeState));
                    }
                }
                slot++;
            }
            // Opened inventory translation
            for (int row = 0; row < view.getTopInventory().getSize(); row++) {
                org.bukkit.inventory.ItemStack item = view.getTopInventory().getItem(row);

                if (item != null && item.getType() != Material.AIR) {
                    if (translator.isTranslatable(item)) {
                        packets.add(translator.translate(item.clone(), player, entityPlayer.getDefaultContainer().getId(), row, defaultState));
                        translator.reset(item);
                    }
                }
            }
            // Sending packets
            for (Packet packet : packets) {
                entityPlayer.getConnection().sendPacket(packet);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
