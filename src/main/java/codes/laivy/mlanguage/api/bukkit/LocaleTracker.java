package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class LocaleTracker extends BukkitRunnable {

    private final @NotNull Map<@NotNull UUID, @Nullable Locale> locales = new LinkedHashMap<>();

    public LocaleTracker() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            locales.put(player.getUniqueId(), multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId()));
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Locale currentLocale = multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId());
            if (!Objects.equals(currentLocale, locales.getOrDefault(player.getUniqueId(), null))) { // Locale changed if true
                multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory(player);
                locales.put(player.getUniqueId(), currentLocale);
            }
        }
    }

}
