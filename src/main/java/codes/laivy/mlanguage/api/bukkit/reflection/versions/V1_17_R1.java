package codes.laivy.mlanguage.api.bukkit.reflection.versions;

import codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.others.NonNullList;
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
import codes.laivy.mlanguage.api.bukkit.reflection.objects.IntegerObjExec;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import io.netty.channel.Channel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class V1_17_R1 extends V1_16_R3 {

    private boolean state = false;

    public V1_17_R1(@NotNull BukkitMultiplesLanguagesAPI api) {
        super(api);
    }

    /**
     * After 1.17.1, the states has been added to the minecraft inventories packets.
     * @return true if is 1.17.1 state checks
     */
    public boolean isStateEnabled() {
        return state || ReflectionUtils.isCompatible(V1_18_R1.class);
    }

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_16_R3.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        } else if (version == V1_12_R1.class) {
            if (key.equals("Container:slots")) {
                return false;
            }
        } else if (version == V1_11_R1.class) {
            if (key.equals("PacketPlayOutWindowItems:items")) {
                return false;
            }
        } else if (version == V1_8_R3.class) {
            if (key.equals("NetworkManager:channel")) {
                return false;
            }
        } else if (version == V1_8_R1.class) {
            if (key.equals("PacketPlayOutWindowItems:windowId") || key.equals("PacketPlayOutSetSlot:slot") || key.equals("PacketPlayOutSetSlot:windowId") || key.equals("Slot:index") || key.equals("PlayerConnection:networkManager") || key.equals("EntityPlayer:playerConnection") || key.equals("NBTTagList:list") || key.equals("NBTTagString:getData") || key.equals("ItemStack:tag") || key.equals("PacketPlayOutSetSlot:item") || key.equals("EntityPlayer:activeContainer") || key.equals("EntityPlayer:defaultContainer") || key.equals("Container:windowId")) {
                return false;
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_17_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.nbt.NBTBase"));

        load(V1_17_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.nbt.NBTTagByte"));
        load(V1_17_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.nbt.NBTTagByteArray"));
        load(V1_17_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.nbt.NBTTagCompound"));
        load(V1_17_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.nbt.NBTTagDouble"));
        load(V1_17_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.nbt.NBTTagFloat"));
        load(V1_17_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.nbt.NBTTagInt"));
        load(V1_17_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.nbt.NBTTagIntArray"));
        load(V1_17_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.nbt.NBTTagList"));
        load(V1_17_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.nbt.NBTTagLong"));
        load(V1_17_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.nbt.NBTTagShort"));
        load(V1_17_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.nbt.NBTTagString"));
        // Items
        load(V1_17_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.world.item.ItemStack"));
        load(V1_17_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack"));
        load(V1_17_R1.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_17_R1.inventory.CraftMetaItem"));
        // Packets
        load(V1_17_R1.class, "Packet", new Packet.PacketClass("net.minecraft.network.protocol.Packet"));
        load(V1_17_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.network.protocol.game.PacketPlayOutSetSlot"));
        load(V1_17_R1.class, "PacketPlayOutWindowItems", new PacketPlayOutWindowItems.PacketPlayOutWindowItemsClass("net.minecraft.network.protocol.game.PacketPlayOutWindowItems"));
        // Player
        load(V1_17_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.level.EntityPlayer"));
        load(V1_17_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer"));
        load(V1_17_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.network.PlayerConnection"));
        load(V1_17_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.network.NetworkManager"));
        // Inventory
        load(V1_17_R1.class, "Container", new Container.ContainerClass("net.minecraft.world.inventory.Container"));
        load(V1_17_R1.class, "Slot", new Slot.SlotClass("net.minecraft.world.inventory.Slot"));
        // Chat
        load(V1_17_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.network.chat.IChatBaseComponent"));
        load(V1_17_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer"));
        // Others
        load(V1_17_R1.class, "NonNullList", new NonNullList.NonNullListClass("net.minecraft.core.NonNullList"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        load(V1_17_R1.class, "NonNullList:create", new MethodExecutor(getClassExec("NonNullList"), getClassExec("NonNullList"), "a", "Creates a new NonNullList", ClassExecutor.OBJECT, ClassExecutor.OBJECT_ARRAY));
    }

    @Override
    public void loadFields() {
        super.loadFields();

        try {
            load(V1_17_R1.class, "PacketPlayOutSetSlot:windowId", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "c", "Gets the windowId of the PacketPlayOutSetSlot packet"));
            load(V1_17_R1.class, "PacketPlayOutSetSlot:item", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), getClassExec("ItemStack"), "e", "Gets the item of the PacketPlayOutSetSlot packet"));
            load(V1_17_R1.class, "PacketPlayOutSetSlot:slot", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "d", "Gets the slot of the PacketPlayOutSetSlot packet"));

            load(V1_17_R1.class, "PacketPlayOutWindowItems:items", new FieldExecutor(getClassExec("PacketPlayOutWindowItems"), new ClassExecutor(List.class), "b", "Gets the items of the PacketPlayOutWindowItems packet"));

            load(V1_17_R1.class, "ItemStack:tag", new FieldExecutor(getClassExec("ItemStack"), getClassExec("NBTBase:NBTTagCompound"), "w", "Gets the item NBT tag"));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof NoSuchFieldException) {
                state = true;

                load(V1_17_R1.class, "Container:stateId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "q", "Gets the state id of a Container"));

                load(V1_17_R1.class, "PacketPlayOutSetSlot:windowId", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "c", "Gets the windowId of the PacketPlayOutSetSlot packet"));
                load(V1_17_R1.class, "PacketPlayOutSetSlot:state", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "d", "Gets the state of the PacketPlayOutSetSlot packet"));
                load(V1_17_R1.class, "PacketPlayOutSetSlot:slot", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "e", "Gets the slot of the PacketPlayOutSetSlot packet"));
                load(V1_17_R1.class, "PacketPlayOutSetSlot:item", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), getClassExec("ItemStack"), "f", "Gets the item of the PacketPlayOutSetSlot packet"));

                load(V1_17_R1.class, "PacketPlayOutWindowItems:state", new FieldExecutor(getClassExec("PacketPlayOutWindowItems"), ClassExecutor.INT, "b", "Gets the state of the PacketPlayOutWindowItems packet"));
                load(V1_17_R1.class, "PacketPlayOutWindowItems:items", new FieldExecutor(getClassExec("PacketPlayOutWindowItems"), new ClassExecutor(List.class), "c", "Gets the items of the PacketPlayOutWindowItems packet"));
                load(V1_17_R1.class, "PacketPlayOutWindowItems:held", new FieldExecutor(getClassExec("PacketPlayOutWindowItems"), getClassExec("ItemStack"), "d", "Gets the held item of the PacketPlayOutWindowItems packet"));

                load(V1_17_R1.class, "ItemStack:tag", new FieldExecutor(getClassExec("ItemStack"), getClassExec("NBTBase:NBTTagCompound"), "u", "Gets the item NBT tag"));
            } else {
                throw e;
            }
        }

        load(V1_17_R1.class, "PacketPlayOutWindowItems:windowId", new FieldExecutor(getClassExec("PacketPlayOutWindowItems"), ClassExecutor.INT, "a", "Gets the window id of the PacketPlayOutWindowItems packet"));

        load(V1_17_R1.class, "NBTTagString:getData", new FieldExecutor(getClassExec("NBTBase:NBTTagString"), ClassExecutor.STRING, "A", "Gets a NBTTagString's data"));
        load(V1_17_R1.class, "EntityPlayer:playerConnection", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("PlayerConnection"), "b", "Gets the PlayerConnection of the player"));
        load(V1_17_R1.class, "NBTTagList:list", new FieldExecutor(getClassExec("NBTBase:NBTTagList"), new ClassExecutor(List.class) {}, "c", "Gets the list of a NBTTagList"));

        load(V1_17_R1.class, "PlayerConnection:networkManager", new FieldExecutor(getClassExec("PlayerConnection"), getClassExec("NetworkManager"), "a", "Gets the NetworkManager of a PlayerConnection"));
        load(V1_17_R1.class, "EntityPlayer:activeContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bV", "Gets the active container inventory of an EntityPlayer"));
        load(V1_17_R1.class, "EntityPlayer:defaultContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "bU", "Gets the default container inventory of an EntityPlayer"));
        load(V1_17_R1.class, "Container:windowId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "j", "Gets the id of a Container"));
        load(V1_17_R1.class, "NetworkManager:channel", new FieldExecutor(getClassExec("NetworkManager"), new ClassExecutor(Channel.class), "k", "Gets the Channel of a NetworkManager"));

        load(V1_17_R1.class, "Slot:index", new FieldExecutor(getClassExec("Slot"), ClassExecutor.INT, "a", "Gets the index of a Slot"));

        load(V1_17_R1.class, "Container:slots", new FieldExecutor(getClassExec("Container"), new ClassExecutor(List.class), "i", "Gets the slots list of a Container"));

        load(V1_17_R1.class, "NonNullList:list", new FieldExecutor(getClassExec("NonNullList"), new ClassExecutor(List.class), "a", "Gets the list of a NonNullList"));
        load(V1_17_R1.class, "NonNullList:default", new FieldExecutor(getClassExec("NonNullList"), ClassExecutor.OBJECT, "b", "Gets the default object of a NonNullList"));
    }

    @Override
    public @NotNull PacketPlayOutWindowItems translateWindowItems(@NotNull PacketPlayOutWindowItems original, @NotNull Player player) {
        if (isStateEnabled()) {
            BukkitItemTranslator translator = getApi().getItemTranslator();

            List<codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack> items = new LinkedList<>();
            // Window id and items of the original packet
            int windowId = original.getWindowId();
            int stateId = original.getStateId();
            codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack[] list = original.getItems();
            ItemStack held = original.getHeldItem();
            // Translation foreach
            for (codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack itemNms : list) {
                org.bukkit.inventory.ItemStack item = itemNms.getCraftItemStack().getItemStack().clone();

                if (translator.isTranslatable(item)) {
                    translator.translate(item, player);
                }

                items.add(codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack(item));
            }
            // Creating the new packet with translated items applied
            return getDefApi().getVersion().createWindowItemsPacket(windowId, stateId, items.toArray(new codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack[0]), held);
        } else {
            return super.translateWindowItems(original, player);
        }
    }

    @Override
    public @NotNull PacketPlayOutSetSlot createSetSlotPacket(int windowId, int slot, int state, @NotNull ItemStack itemStack) {
        if (isStateEnabled()) {
            return new PacketPlayOutSetSlot(getClassExec("PacketPlayOutSetSlot").getConstructor(ClassExecutor.INT, ClassExecutor.INT, ClassExecutor.INT, getDefApi().getVersion().getClassExec("ItemStack")).newInstance(new IntegerObjExec(windowId), new IntegerObjExec(state), new IntegerObjExec(slot), itemStack));
        } else {
            return super.createSetSlotPacket(windowId, slot, state, itemStack);
        }
    }

    @Override
    public @NotNull PacketPlayOutWindowItems createWindowItemsPacket(int windowId, int state, @NotNull ItemStack[] items, @Nullable ItemStack held) {
        if (isStateEnabled()) {
            if (held == null) {
                throw new UnsupportedOperationException("Since 1.17.1, the held item cannot be null!");
            }

            List<Object> list = new LinkedList<>();
            for (ItemStack itemStack : items) {
                list.add(itemStack.getValue());
            }

            return new PacketPlayOutWindowItems(getClassExec("PacketPlayOutWindowItems").getConstructor(ClassExecutor.INT, ClassExecutor.INT, getClassExec("NonNullList"), getClassExec("ItemStack")).newInstance(new IntegerObjExec(windowId), new IntegerObjExec(state), NonNullList.create(null, list), held));
        } else {
            List<Object> list = new LinkedList<>();
            for (ItemStack itemStack : items) {
                list.add(itemStack.getValue());
            }

            return new PacketPlayOutWindowItems(getClassExec("PacketPlayOutWindowItems").getConstructor(ClassExecutor.INT, getClassExec("NonNullList")).newInstance(new IntegerObjExec(windowId), NonNullList.create(null, list)));
        }
    }

    @Override
    public void translateInventory(@NotNull Player player) {
        if (isStateEnabled()) {
            BukkitItemTranslator translator = getApi().getItemTranslator();
            EntityPlayer entityPlayer = EntityPlayer.getEntityPlayer(player);

            Container container = EntityPlayer.getEntityPlayer(player).getDefaultContainer();
            List<Packet> packets = new LinkedList<>();

            // State id
            int activeState = entityPlayer.getActiveContainer().getStateId();
            entityPlayer.getActiveContainer().setStateId(activeState + 1);
            int defaultState = entityPlayer.getDefaultContainer().getStateId();
            entityPlayer.getDefaultContainer().setStateId(defaultState + 1);
            // Items translation
            int index = 0;
            for (Slot slot : container.getSlots()) {
                if (slot.getItem() != null && slot.getItem().getValue() != null) {
                    org.bukkit.inventory.ItemStack item = slot.getItem().getCraftItemStack().getItemStack();
                    if (item.getType() != Material.AIR) {
                        if (translator.isTranslatable(item)) {
                            packets.add(translator.translate(item, player, 0, index, defaultState));
                        }
                    }
                }
                index++;
            }

            if (isInventoryOpened(player)) {
                Container activeContainer = EntityPlayer.getEntityPlayer(player).getActiveContainer();
                index = 0;
                for (org.bukkit.inventory.ItemStack item : player.getOpenInventory().getTopInventory()) {
                    if (item != null && item.getType() != Material.AIR) {
                        if (translator.isTranslatable(item)) {
                            packets.add(translator.translate(item, player, activeContainer.getId(), index, activeState));
                        }
                    }
                    index++;
                }
            }

            for (Packet packet : packets) {
                EntityPlayer.getEntityPlayer(player).getConnection().sendPacket(packet);
            }
        } else {
            super.translateInventory(player);
        }
    }
}
