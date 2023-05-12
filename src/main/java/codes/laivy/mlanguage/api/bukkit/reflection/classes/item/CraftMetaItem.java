package codes.laivy.mlanguage.api.bukkit.reflection.classes.item;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_13_R1;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_14_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class CraftMetaItem extends ObjectExecutor {
    public CraftMetaItem(@Nullable Object value) {
        super(value);
    }

    public void setDisplayName(@NotNull BaseComponent[] displayName) {
        if (ReflectionUtils.isCompatible(V1_13_R1.class)) {
            V1_13_R1 version = (V1_13_R1) multiplesLanguagesBukkit().getVersion();
            version.setCraftItemMetaDisplayName(this, displayName);
        } else {
            throw new UnsupportedOperationException("This method is only available since 1.14+");
        }
    }
    public void setLore(@NotNull BaseComponent[] lore) {
        if (ReflectionUtils.isCompatible(V1_14_R1.class)) {
            V1_13_R1 version = (V1_13_R1) multiplesLanguagesBukkit().getVersion();
            version.setCraftItemMetaLore(this, lore);
        } else {
            throw new UnsupportedOperationException("This method is only available since 1.14+");
        }
    }

    @Override
    public @NotNull CraftMetaItemClass getClassExecutor() {
        return (CraftMetaItemClass) multiplesLanguagesBukkit().getVersion().getClassExec("CraftMetaItem");
    }

    public static class CraftMetaItemClass extends ClassExecutor {
        public CraftMetaItemClass(@NotNull String className) {
            super(className);
        }
    }
}
