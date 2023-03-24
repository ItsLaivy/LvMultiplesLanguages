package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagString;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.objects.IntegerObjExec;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class ItemTranslatorBukkitImpl implements ItemTranslator<ItemStack, Player> {
    @Override
    public boolean isTranslatable(@NotNull ItemStack item) {
        codes.laivy.mlanguage.reflection.classes.item.ItemStack nmsItem = codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(item);
        if (nmsItem.getValue() != null && nmsItem.getTag() != null) {
            return nmsItem.getTag().contains("Translatable");
        }
        return false;
    }

    @Override
    public @NotNull PacketPlayOutSetSlot translate(@NotNull ItemStack item, @NotNull Player player, int window, int slot) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(item).getTag());
            final Message name;
            final Message lore;

            if (tag.contains("NameTranslation")) {
                JsonObject serializedJson = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("NameTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                name = data.get(null);
            } else {
                name = null;
            }

            if (tag.contains("LoreTranslation")) {
                JsonObject serializedJson = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("LoreTranslation").getValue()).getData())).getAsJsonObject();
                SerializedData data = SerializedData.deserialize(serializedJson);
                lore = data.get(null);
            } else {
                lore = null;
            }

            if (name != null || lore != null) {
                Locale l = multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId());
                if (player.getGameMode() == GameMode.CREATIVE) {
                    l = multiplesLanguagesBukkit().getApi().getDefaultLocale();
                } final Locale locale = l;

                org.bukkit.inventory.ItemStack bukkitItem = item.clone();
                ItemMeta meta = bukkitItem.getItemMeta();
                if (name != null) {
                    meta.setDisplayName(ComponentSerializer.toString(name.get(locale)));
                }
                if (lore != null) {
                    meta.setLore(new LinkedList<String>() {{
                        add(ComponentSerializer.toString(lore.get(locale)));
                    }});
                }
                bukkitItem.setItemMeta(meta);

                return new PacketPlayOutSetSlot(multiplesLanguagesBukkit().getVersion().getClassExec("PacketPlayOutSetSlot").getConstructor(ClassExecutor.INT, ClassExecutor.INT, multiplesLanguagesBukkit().getVersion().getClassExec("ItemStack")).newInstance(new IntegerObjExec(window), new IntegerObjExec(slot), codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(bukkitItem)));
            }
        }
        throw new IllegalArgumentException("This item isn't translatable!");
    }
}
