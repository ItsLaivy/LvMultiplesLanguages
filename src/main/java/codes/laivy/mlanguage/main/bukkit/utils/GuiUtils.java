package codes.laivy.mlanguage.main.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiUtils {

    public static @NotNull ItemStack BLACK_STAINED_GLASS_PANE;
    public static @NotNull ItemStack LIME_STAINED_GLASS_PANE;
    public static @NotNull ItemStack RED_STAINED_GLASS_PANE;
    public static @NotNull ItemStack COBWEB;

    static {
        ItemStack item;
        try {
            item = new ItemStack(Material.valueOf("BLACK_STAINED_GLASS_PANE"));
        } catch (IllegalArgumentException ignore) {
            item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 0, (byte) 15);
        }
        BLACK_STAINED_GLASS_PANE = item;
        //
        try {
            item = new ItemStack(Material.valueOf("LIME_STAINED_GLASS_PANE"));
        } catch (IllegalArgumentException ignore) {
            item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 0, (byte) 5);
        }
        LIME_STAINED_GLASS_PANE = item;
        //
        try {
            item = new ItemStack(Material.valueOf("RED_STAINED_GLASS_PANE"));
        } catch (IllegalArgumentException ignore) {
            item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 0, (byte) 14);
        }
        RED_STAINED_GLASS_PANE = item;
        //
        try {
            item = new ItemStack(Material.valueOf("COBWEB"));
        } catch (IllegalArgumentException ignore) {
            item = new ItemStack(Material.valueOf("WEB"));
        }
        COBWEB = item;
    }

    public static ItemStack getPreviousPageItem(int actualPage) {
        return createItem(RED_STAINED_GLASS_PANE, "§cPrevious page", "§7Click to go to the\n§7previous page", "", "§7Current Page: §f" + actualPage);
    }
    public static ItemStack getNextPageItem(int actualPage) {
        return createItem(LIME_STAINED_GLASS_PANE, "§aNext page", "§7Click to go to the\n§7next page", "", "§7Current Page: §f" + actualPage);
    }

    public static boolean hasMeta(ItemStack i) {
        if (!i.hasItemMeta()) {
            i.setItemMeta(Bukkit.getItemFactory().getItemMeta(i.getType()));
        }

        return i.hasItemMeta();
    }
    public static boolean hasLore(ItemStack i) {
        if (hasMeta(i)) {
            return i.getItemMeta().hasLore();
        }
        return false;
    }

    public static List<String> getLore(ItemStack i) {
        List<String> lore = new ArrayList<>();

        if (hasMeta(i)) {
            if (hasLore(i)) {
                lore = i.getItemMeta().getLore();
            }
        }

        if (lore == null) {
            lore = new ArrayList<>();
        }

        return lore;
    }

    public static int getPageOfItem(ItemStack item) {
        List<String> lore = getLore(item);
        for (String l : lore) {
            if (l.contains("§7Current Page: §f")) {
                return Integer.parseInt(l.replace("§7Current Page: §f", "").replace(" ", ""));
            }
        }
        throw new NullPointerException();
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(new ItemStack(material), name, Arrays.asList(lore));
    }
    public static ItemStack createItem(ItemStack item, String name, String... lore) {
        return createItem(item, name, Arrays.asList(lore));
    }
    public static ItemStack createItem(ItemStack item, String name, List<String> l) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        for (String e : l) {
            lore.add(e.replace("\n", "%nl%"));
        }

        if (name != null && meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        ArrayList<String> array = new ArrayList<>();
        for (String str : lore) {
            for (String str2 : str.split("%nl%")) {
                if (!str2.contains("%noline%")) {
                    array.add(ChatColor.translateAlternateColorCodes('&', str2));
                }
            }
        }

        if (meta != null) {
            meta.setLore(array);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static String getLoreFormattedMessage(String message) {
        return getLoreFormattedMessage(message, 25);
    }
    public static String getLoreFormattedMessage(String message, @Range(from = 15, to = 100) int size) {
        StringBuilder formattedMessage = new StringBuilder();
        int length = 0;

        String lastColorCode = "§f";
        for (String split : message.split(" ")) {
            if (split.contains("§")) {
                lastColorCode = ChatColor.getLastColors(split);
            }

            length += split.length();
            formattedMessage.append(lastColorCode).append(split).append(" ");

            if (length > size) {
                formattedMessage.append("\n");
                length = 0;
            }
        }

        String returnString = formattedMessage.toString();
        if (returnString.endsWith(" \n")) {
            returnString = removeSuffix(returnString, " \n");
        }

        return returnString.replace(" \n", "\n");
    }
    public static String removeSuffix(final String s, final String suffix) {
        if (s != null && suffix != null && s.endsWith(suffix)) {
            return s.substring(0, s.length() - suffix.length());
        }
        return s;
    }

}
