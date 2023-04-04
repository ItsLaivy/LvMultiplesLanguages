package codes.laivy.mlanguage.api.bukkit.translator;

import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagString;
import codes.laivy.mlanguage.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.MODE;
import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;
import static codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack;

public interface BukkitItemTranslator extends ItemTranslator<ItemStack, Player> {
    @Override
    default boolean isTranslatable(@NotNull ItemStack item) {
        codes.laivy.mlanguage.reflection.classes.item.ItemStack nmsItem = getNMSItemStack(item);
        if (nmsItem.getValue() != null && nmsItem.getTag() != null) {
            return nmsItem.getTag().contains("Translatable");
        }
        return false;
    }
    default void translateInventory(@NotNull Player player) {
        EntityPlayer entityPlayer = EntityPlayer.getEntityPlayer(player);
        Set<PacketPlayOutSetSlot> packets = new LinkedHashSet<>();

        PlayerInventory playerInventory = player.getInventory();
        InventoryView view = player.getOpenInventory();
        // State id
        int state = entityPlayer.getActiveContainer().getStateId();
        entityPlayer.getActiveContainer().setStateId(state + 1);
        // Item changing

//        for (int slot = 0; slot < playerInventory.getSize(); slot++) {
//            ItemStack item = player.getInventory().getItem(slot);
//
//            if (item != null && item.getType() != Material.AIR && isTranslatable(item)) {
//                packets.add(translate(item, player, entityPlayer.getActiveContainer().getId(), 36 + slot));
//            }
//        }
        if () {

        }

//        for (int slot = 0; slot < view.getTopInventory().getSize(); slot++) {
//            ItemStack item = view.getTopInventory().getItem(slot);
//
//            if (item != null && item.getType() != Material.AIR && isTranslatable(item)) {
////                packets.add(multiplesLanguagesBukkit().getVersion().createSetSlotPacket(entityPlayer.getDefaultContainer().getId(), slot, getNMSItemStack()));
//                packets.add(translate(item, player, entityPlayer.getDefaultContainer().getId(), slot));
//            }
//        }
//        for (int row = 0; row < view.getTopInventory().getSize(); row++) {
//            ItemStack item = view.getTopInventory().getItem(row);
//
//            if (item != null && item.getType() != Material.AIR) {
//                if (isTranslatable(item)) {
//                    Bukkit.broadcastMessage("Top inv: '" + item.getType().name() + "'");
//                    // translate(ItemStack, Player, windowId, slot)
//                    packets.add(translate(item.clone(), player, entityPlayer.getActiveContainer().getId(), row));
//                    reset(item);
//                }
//            }
//        }

//        //int row = 36;
//        for (ItemStack item : player.getInventory().getContents()) {
//            if (item != null) {
//                if (isTranslatable(item)) {
//                    packets.add(translate(item.clone(), player, 0, row));
//                    reset(item);
//                }
//            }
//            row++;
//        }
//        row = 0;
//        if (player.getOpenInventory() != null) {
//            for (ItemStack item : player.getOpenInventory().getTopInventory()) {
//                if (item != null) {
//                    if (isTranslatable(item)) {
//                        packets.add(translate(item.clone(), player, entityPlayer.getActiveContainer().getId(), row));
//                        reset(item);
//                    }
//                }
//                row++;
//            }
//        }

        for (Packet packet : packets) {
            entityPlayer.getConnection().sendPacket(packet);
        }
    }

    default @NotNull PacketPlayOutSetSlot translate(@NotNull ItemStack item, @NotNull Player player, int window, int slot) {
        if (isTranslatable(item) && player.isOnline()) {
            translate(item, player);
            return multiplesLanguagesBukkit().getVersion().createSetSlotPacket(window, slot, getNMSItemStack(item));
        }
        throw new IllegalArgumentException("This item isn't translatable!");
    }

    @Override
    default @Nullable Message getName(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final Message name;

            if (tag.contains("NameTranslation")) {
                JsonObject serializedJson = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("NameTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                name = data.get(null);
            } else {
                name = null;
            }
            return name;
        }
        return null;
    }

    @Override
    default @Nullable Message getLore(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final Message lore;

            if (tag.contains("LoreTranslation")) {
                JsonObject serializedJson = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("LoreTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                lore = data.get(null);
            } else {
                lore = null;
            }
            return lore;
        }
        return null;
    }

    @Override
    default void translate(@NotNull ItemStack item, @NotNull Player player) {
        if (isTranslatable(item)) {
            final @Nullable Message name = getName(item);
            final @Nullable Message lore = getLore(item);

            if (name != null || lore != null) {
                Locale l = multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId());
                if (player.getGameMode() == GameMode.CREATIVE) {
                    l = multiplesLanguagesBukkit().getApi().getDefaultLocale();
                } final Locale locale = l;

                if (name != null) {
                    multiplesLanguagesBukkit().getVersion().setItemBukkitDisplayName(item, BukkitMessageStorage.mergeBaseComponents(name.get(locale)));
                }
                if (lore != null) {
                    multiplesLanguagesBukkit().getVersion().setItemBukkitLore(item, lore.get(locale));
                }
            }
        } else {
            throw new IllegalArgumentException("This item isn't translatable!");
        }
    }

    /**
     * Called everytime the item needs to be reset to the default messages (name/lore) locale
     * @param item the item
     */
    default void reset(@NotNull ItemStack item) {
        final @Nullable Message name = getName(item);
        final @Nullable Message lore = getLore(item);

        if (name != null) {
            multiplesLanguagesBukkit().getVersion().setItemBukkitDisplayName(item, BukkitMessageStorage.mergeBaseComponents(name.get(name.getStorage().getDefaultLocale())));
        } if (lore != null) {
            multiplesLanguagesBukkit().getVersion().setItemBukkitLore(item, lore.get(lore.getStorage().getDefaultLocale()));
        }
    }

}
