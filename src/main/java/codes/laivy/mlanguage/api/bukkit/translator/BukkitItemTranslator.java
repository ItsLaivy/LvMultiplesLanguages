package codes.laivy.mlanguage.api.bukkit.translator;

import codes.laivy.mlanguage.api.bukkit.IBukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.events.ItemTranslateEvent;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagByte;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagString;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;
import static codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack;
import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

/**
 * The default Bukkit item translator of the LvMultiplesLanguages
 */
public final class BukkitItemTranslator implements IBukkitItemTranslator {

    public BukkitItemTranslator() {
    }

    @Override
    public boolean isTranslatable(@NotNull ItemStack item) {
        codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack nmsItem = getNMSItemStack(item);
        if (nmsItem.getValue() != null && nmsItem.getTag() != null) {
            return nmsItem.getTag().contains("Translatable");
        }
        return false;
    }

    @Override
    public @NotNull ItemStack setTranslatable(@NotNull ItemStack item, @Nullable Message<BaseComponent> name, @Nullable Message<BaseComponent> lore) {
        codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack nmsItem = codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack(item);
        NBTTagCompound compound = nmsItem.getTag();

        if (compound == null) {
            compound = (NBTTagCompound) getDefApi().getVersion().nbtTag(Version.NBTTag.COMPOUND);
        }

        compound.set("Translatable", new NBTTagByte((byte) 1));
        if (name != null) {
            compound.set("NameTranslation", new NBTTagString(name.serialize().serialize().toString()));
        }
        if (lore != null) {
            compound.set("LoreTranslation", new NBTTagString(lore.serialize().serialize().toString()));
        }

        nmsItem.setTag(compound);

        return nmsItem.getCraftItemStack().getItemStack();
    }

    @Override
    public void translateInventory(@NotNull Player player) {
        getDefApi().getVersion().translateInventory(player);
    }

    /**
     * Gets the SetSlot packet
     * @param item the item
     * @param player the player
     * @param window the windowId
     * @param slot the slot
     * @param state the stateId (since 1.17.1, leave it -1 before this version)
     * @return the SetSlot packet
     */
    public @NotNull PacketPlayOutSetSlot translate(@NotNull ItemStack item, @NotNull Player player, int window, int slot, int state) {
        if (isTranslatable(item)) {
            translate(item, player);
            return getDefApi().getVersion().createSetSlotPacket(window, slot, state, getNMSItemStack(item));
        }
        throw new IllegalArgumentException("This item isn't translatable!");
    }

    @Override
    public @Nullable Message<BaseComponent> getName(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final Message<BaseComponent> name;

            if (tag.contains("NameTranslation")) {
                JsonObject serializedJson = new JsonParser().parse(Objects.requireNonNull(new NBTTagString(tag.get("NameTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                name = data.get();
            } else {
                name = null;
            }
            return name;
        }
        return null;
    }

    @Override
    public @Nullable Message<BaseComponent> getLore(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final Message<BaseComponent> lore;

            if (tag.contains("LoreTranslation")) {
                JsonObject serializedJson = new JsonParser().parse(Objects.requireNonNull(new NBTTagString(tag.get("LoreTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                lore = data.get();
            } else {
                lore = null;
            }
            return lore;
        }
        return null;
    }

    @Override
    public void translate(@NotNull ItemStack item, @NotNull Player player) {
        if (isTranslatable(item)) {
            @Nullable Locale locale = multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId());

            @Nullable Message<BaseComponent> name = getName(item);
            @Nullable Message<BaseComponent> lore = getLore(item);

            @NotNull Object[] nameReplaces = new Object[0];
            @NotNull Object[] loreReplaces = new Object[0];

            // Event calling
            ItemTranslateEvent event = new ItemTranslateEvent(!Bukkit.isPrimaryThread(), item, player, locale, name, lore, nameReplaces, loreReplaces);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            name = event.getName();
            lore = event.getLore();
            locale = event.getLocale();

            nameReplaces = event.getNameReplaces();
            loreReplaces = event.getLoreReplaces();
            //

            if (name != null) {
                final Locale nameLocale = (locale != null ? locale : name.getStorage().getDefaultLocale());

                getDefApi().getVersion().setItemBukkitDisplayName(
                        item,
                        ComponentUtils.merge(name.getText(nameLocale, nameReplaces))
                );
            } else {
                getDefApi().getVersion().setItemBukkitDisplayName(item, null);
            }

            if (lore != null) {
                final Locale loreLocale = (locale != null ? locale : lore.getStorage().getDefaultLocale());

                getDefApi().getVersion().setItemBukkitLore(
                        item,
                        lore.getText(loreLocale, loreReplaces)
                );
            } else {
                getDefApi().getVersion().setItemBukkitLore(item, null);
            }
        } else {
            throw new IllegalArgumentException("This item isn't translatable!");
        }
    }

    /**
     * Called everytime the item needs to be reset to the default messages (name/lore) locale
     * @param item the item
     */
    public void reset(@NotNull ItemStack item) {
        final @Nullable Message<BaseComponent> name = getName(item);
        final @Nullable Message<BaseComponent> lore = getLore(item);

        if (name != null) {
            getDefApi().getVersion().setItemBukkitDisplayName(item, ComponentUtils.merge(name.getText(name.getStorage().getDefaultLocale())));
        } if (lore != null) {
            getDefApi().getVersion().setItemBukkitLore(item, lore.getText(lore.getStorage().getDefaultLocale()));
        }
    }
}
