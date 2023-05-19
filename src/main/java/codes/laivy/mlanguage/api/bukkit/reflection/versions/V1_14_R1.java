package codes.laivy.mlanguage.api.bukkit.reflection.versions;

import codes.laivy.mlanguage.main.BukkitMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutWindowItems;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.CraftPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.NetworkManager;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.PlayerConnection;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Slot;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.Executor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.FieldExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.MethodExecutor;
import codes.laivy.mlanguage.utils.ClassUtils;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class V1_14_R1 extends V1_13_R2 {

    public V1_14_R1(@NotNull BukkitMultiplesLanguages plugin) {
        super(plugin);
    }

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_13_R2.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        } else if (version == V1_8_R1.class) {
            if (executor instanceof MethodExecutor) {
                if (key.equals("NBTTagCompound:set")) {
                    return false;
                }
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void setCraftItemMetaLore(@NotNull CraftMetaItem item, @Nullable List<BaseComponent[]> lore) {
        if (lore != null) {
            List<Object> objects = new LinkedList<>();
            for (BaseComponent[] component : lore) {
                objects.add(IChatBaseComponent.convert(component).getValue());
            }

            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:lore").set(item, objects);
        } else {
            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:lore").set(item, null);
        }
    }

    @Override 
    public void setItemLore(@NotNull ItemStack itemStack, @Nullable List<BaseComponent[]> lore) {
        NBTTagCompound tag = itemStack.getTag();

        if (tag == null) {
            itemStack.setTag((NBTTagCompound) nbtTag(NBTTag.COMPOUND));
            tag = itemStack.getTag();
        }

        if (tag == null) {
            throw new NullPointerException("Couldn't get the NBT Tag of this itemstack");
        }

        NBTTagCompound display = (NBTTagCompound) nbtTag(NBTTag.COMPOUND);
        if (tag.contains("display")) {
            display = new NBTTagCompound(tag.get("display").getValue());
        }

        if (lore != null) {
            List<NBTBase> loreBase = new LinkedList<>();
            for (BaseComponent[] line : lore) {
                loreBase.add(multiplesLanguagesBukkit().getVersion().nbtTag(NBTTag.STRING, ComponentUtils.serialize(line)));
            }
            display.set("Lore", new NBTTagList(loreBase));
        } else {
            display.remove("Lore");
        }

        tag.set("display", display);
        itemStack.setTag(tag);
    }

    @Override
    public void setItemBukkitLore(org.bukkit.inventory.@NotNull ItemStack itemStack, @Nullable List<BaseComponent[]> lore) {
        if (ClassUtils.isInstanceOf(getClassExec("CraftMetaItem").getReflectionClass(), itemStack.getItemMeta().getClass())) {
            if (itemStack.hasItemMeta()) {
                CraftMetaItem itemMeta = new CraftMetaItem(itemStack.getItemMeta());
                itemMeta.setLore(lore);
                itemStack.setItemMeta((ItemMeta) itemMeta.getValue());
                return;
            }
        }
        super.setItemBukkitLore(itemStack, lore);
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_14_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_14_R1.NBTBase"));

        load(V1_14_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_14_R1.NBTTagByte"));
        load(V1_14_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_14_R1.NBTTagByteArray"));
        load(V1_14_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_14_R1.NBTTagCompound"));
        load(V1_14_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_14_R1.NBTTagDouble"));
        load(V1_14_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_14_R1.NBTTagFloat"));
        load(V1_14_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_14_R1.NBTTagInt"));
        load(V1_14_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_14_R1.NBTTagIntArray"));
        load(V1_14_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_14_R1.NBTTagList"));
        load(V1_14_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_14_R1.NBTTagLong"));
        load(V1_14_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_14_R1.NBTTagShort"));
        load(V1_14_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_14_R1.NBTTagString"));
        // Items
        load(V1_14_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_14_R1.ItemStack"));
        load(V1_14_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack"));
        load(V1_14_R1.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_14_R1.inventory.CraftMetaItem"));
        // Packets
        load(V1_14_R1.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_14_R1.Packet"));
        load(V1_14_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_14_R1.PacketPlayOutSetSlot"));
        load(V1_14_R1.class, "PacketPlayOutWindowItems", new PacketPlayOutWindowItems.PacketPlayOutWindowItemsClass("net.minecraft.server.v1_14_R1.PacketPlayOutWindowItems"));
        // Player
        load(V1_14_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_14_R1.EntityPlayer"));
        load(V1_14_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer"));
        load(V1_14_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_14_R1.PlayerConnection"));
        load(V1_14_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_14_R1.NetworkManager"));
        // Inventory
        load(V1_14_R1.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_14_R1.Container"));
        load(V1_14_R1.class, "Slot", new Slot.SlotClass("net.minecraft.server.v1_14_R1.Slot"));
        // Chat
        load(V1_14_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_14_R1.IChatBaseComponent"));
        load(V1_14_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_14_R1.IChatBaseComponent$ChatSerializer"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        load(V1_14_R1.class, "NBTTagCompound:set", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "set", "Sets a value inside a NBTTagCompound", ClassExecutor.STRING, getClassExec("NBTBase")));
    }

    @Override
    public void loadFields() {
        super.loadFields();

        load(V1_14_R1.class, "CraftMetaItem:lore", new FieldExecutor(getClassExec("CraftMetaItem"), new ClassExecutor(List.class), "lore", "Gets the component lore of a ItemMeta"));
    }
}
