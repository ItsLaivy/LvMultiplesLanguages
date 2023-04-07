package codes.laivy.mlanguage.api.bukkit.reflection.versions;

import codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.others.NonNullList;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutWindowItems;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Slot;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.Executor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.MethodExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.CraftPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.NetworkManager;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.PlayerConnection;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.FieldExecutor;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class V1_18_R2 extends V1_18_R1 {

    public V1_18_R2(@NotNull BukkitMultiplesLanguagesAPI api) {
        super(api);
    }

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_18_R1.class) {
            return false;
        } else if (version == V1_17_R1.class) {
            if (key.equals("Container:stateId")) {
                return false;
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_18_R2.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.nbt.NBTBase"));

        load(V1_18_R2.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.nbt.NBTTagByte"));
        load(V1_18_R2.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.nbt.NBTTagByteArray"));
        load(V1_18_R2.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.nbt.NBTTagCompound"));
        load(V1_18_R2.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.nbt.NBTTagDouble"));
        load(V1_18_R2.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.nbt.NBTTagFloat"));
        load(V1_18_R2.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.nbt.NBTTagInt"));
        load(V1_18_R2.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.nbt.NBTTagIntArray"));
        load(V1_18_R2.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.nbt.NBTTagList"));
        load(V1_18_R2.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.nbt.NBTTagLong"));
        load(V1_18_R2.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.nbt.NBTTagShort"));
        load(V1_18_R2.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.nbt.NBTTagString"));
        // Items
        load(V1_18_R2.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.world.item.ItemStack"));
        load(V1_18_R2.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack"));
        load(V1_18_R2.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_18_R2.inventory.CraftMetaItem"));
        // Packets
        load(V1_18_R2.class, "Packet", new Packet.PacketClass("net.minecraft.network.protocol.Packet"));
        load(V1_18_R2.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.network.protocol.game.PacketPlayOutSetSlot"));
        load(V1_18_R2.class, "PacketPlayOutWindowItems", new PacketPlayOutWindowItems.PacketPlayOutWindowItemsClass("net.minecraft.network.protocol.game.PacketPlayOutWindowItems"));
        // Player
        load(V1_18_R2.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.level.EntityPlayer"));
        load(V1_18_R2.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer"));
        load(V1_18_R2.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.network.PlayerConnection"));
        load(V1_18_R2.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.network.NetworkManager"));
        // Inventory
        load(V1_18_R2.class, "Container", new Container.ContainerClass("net.minecraft.world.inventory.Container"));
        load(V1_18_R2.class, "Slot", new Slot.SlotClass("net.minecraft.world.inventory.Slot"));
        load(V1_18_R2.class, "Slot:getItem", new MethodExecutor(getClassExec("Slot"), getClassExec("ItemStack"), "e", "Gets the item of a Slot"));
        // Chat
        load(V1_18_R2.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.network.chat.IChatBaseComponent"));
        load(V1_18_R2.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer"));
        // Others
        load(V1_18_R2.class, "NonNullList", new NonNullList.NonNullListClass("net.minecraft.core.NonNullList"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        load(V1_18_R2.class, "ChatSerializer:convertToBase", new MethodExecutor(getClassExec("IChatBaseComponent"), ClassExecutor.STRING, "a", "Converts a IChatBaseComponent to a string"));

        load(V1_18_R2.class, "NBTTagCompound:set", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "a", "Sets a value inside a NBTTagCompound", ClassExecutor.STRING, getClassExec("NBTBase")));
        load(V1_18_R2.class, "NBTTagCompound:get", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "c", "Gets a value inside a NBTTagCompound", ClassExecutor.STRING));
        load(V1_18_R2.class, "NBTTagCompound:remove", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.VOID, "r", "Removes a value from a NBTTagCompound", ClassExecutor.STRING));
        load(V1_18_R2.class, "NBTTagCompound:contains", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "e", "Check if a NBTTagCompound contains a key", ClassExecutor.STRING));
        load(V1_18_R2.class, "NBTTagCompound:isEmpty", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "f", "Check if a NBTTagCompound is empty"));

        load(V1_18_R2.class, "PlayerConnection:sendPacket", new MethodExecutor(getClassExec("PlayerConnection"), ClassExecutor.VOID, "a", "Sends a packet to a PlayerConnection", getClassExec("Packet")));

        load(V1_18_R2.class, "NBTTagCompound:keySet", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), new ClassExecutor(Set.class) {}, "d", "Gets a NBTTagCompound's keys"));
    }

    @Override
    public void loadFields() {
        super.loadFields();


        load(V1_18_R2.class, "PacketPlayOutSetSlot:state", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "d", "Gets the state of the PacketPlayOutSetSlot packet"));
        load(V1_18_R2.class, "PacketPlayOutSetSlot:slot", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "e", "Gets the slot of the PacketPlayOutSetSlot packet"));
        load(V1_18_R2.class, "PacketPlayOutSetSlot:item", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), getClassExec("ItemStack"), "f", "Gets the item of the PacketPlayOutSetSlot packet"));

        load(V1_18_R2.class, "EntityPlayer:activeContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bV", "Gets the active container inventory of an EntityPlayer"));
        load(V1_18_R2.class, "EntityPlayer:defaultContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bU", "Gets the default container inventory of an EntityPlayer"));
        load(V1_18_R2.class, "Container:windowId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "j", "Gets the id of a Container"));
        load(V1_18_R2.class, "Container:stateId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "r", "Gets the state id of a Container"));
        load(V1_18_R2.class, "NetworkManager:channel", new FieldExecutor(getClassExec("NetworkManager"), new ClassExecutor(Channel.class), "m", "Gets the Channel of a NetworkManager"));
    }
}
