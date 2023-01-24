package codes.laivy.mlanguage;

import codes.laivy.data.sql.SQLDatabase;
import codes.laivy.data.sql.SQLTable;
import codes.laivy.data.sql.mysql.MySQLDatabase;
import codes.laivy.data.sql.mysql.MySQLDatabaseType;
import codes.laivy.data.sql.sqlite.SQLiteDatabase;
import codes.laivy.data.sql.sqlite.SQLiteDatabaseType;
import codes.laivy.mlanguage.api.DefaultLvMLApi;
import codes.laivy.mlanguage.api.LvMLApi;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ChatColor;
import codes.laivy.mlanguage.utils.ClassUtils;
import codes.laivy.mlanguage.utils.PlatformType;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class LvMultiplesLanguages {

    public static void main(String[] args) {
        getPlataform();
    }

    public static PlatformType getPlataform() {
        if (ClassUtils.isPresent("org.bukkit.Bukkit")) {
            return PlatformType.BUKKIT;
        } else if (ClassUtils.isPresent("net.md_5.bungee.api.ProxyServer")) {
            return PlatformType.BUNGEE;
        } else {
            throw new NullPointerException("This server platform isn't supported yet!");
        }
    }

    public static void log(@NotNull Level level, @NotNull String message) {
        if (getPlataform() == PlatformType.BUKKIT) {
            Bukkit.getLogger().log(level, message);
        } else if (getPlataform() == PlatformType.BUNGEE) {
            ProxyServer.getInstance().getLogger().log(level, message);
        } else {
            throw new NullPointerException("This server platform isn't supported yet!");
        }
    }

    /**
     * @return the 'plugins/LvMultiplesLanguages' folder
     */
    public static @NotNull File getPluginFolder() {
        File file;
        if (getPlataform() == PlatformType.BUKKIT) {
            file = new File(Bukkit.getUpdateFolderFile().getParentFile(), "LvMultiplesLanguages");
        } else if (getPlataform() == PlatformType.BUNGEE) {
            file = new File(ProxyServer.getInstance().getPluginsFolder(), "LvMultiplesLanguages");
        } else {
            throw new NullPointerException("This server platform isn't supported yet!");
        }

        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();

        return file;
    }

    // APIs
    @SuppressWarnings("FieldMayBeFinal")
    private static @Nullable LvMLApi api;

    public static @NotNull LvMLApi getApi() {
        return Objects.requireNonNull(api);
    }
    public static void setApi(@NotNull LvMLApi api) {
        LvMultiplesLanguages.api = api;
    }
    //

    public static @NotNull SQLTable setup(@NotNull JsonObject configuration) {
        JsonObject dbJson = configuration.getAsJsonObject("database");
        JsonObject sqlInfos = dbJson.getAsJsonObject("sql informations");

        String type = dbJson.get("type").getAsString();
        String tableName = sqlInfos.get("table").getAsString();
        String databaseName = sqlInfos.get("database").getAsString();
        SQLDatabase database;

        if (type.equalsIgnoreCase("sqlite")) {
            String path = "";
            if (sqlInfos.has("sqlite")) {
                path = sqlInfos.getAsJsonObject("sqlite").get("path").getAsString();
            }

            database = new SQLiteDatabase(new SQLiteDatabaseType(getPluginFolder() + File.separator + path), databaseName);
        } else if (type.equalsIgnoreCase("mysql")) {
            if (sqlInfos.has("mysql")) {
                JsonObject mysqlInfos = sqlInfos.getAsJsonObject("mysql");
                String address = mysqlInfos.get("address").getAsString();
                int port = mysqlInfos.get("port").getAsInt();
                String username = mysqlInfos.get("username").getAsString();
                String password = mysqlInfos.get("password").getAsString();

                database = new MySQLDatabase(new MySQLDatabaseType(username, password, port, address), databaseName);
            } else {
                throw new NullPointerException("Couldn't find mysql informations!");
            }
        } else {
            throw new NullPointerException("Couldn't get database type '" + type + "'. The allowed are 'mysql' or 'sqlite'.");
        }

        // Configurations
        JsonObject config = configuration.getAsJsonObject("configurations");

        // Default language code
        try {
            Language.DEFAULT_LANGUAGE_CODE = Locale.valueOf(config.get("default language code").getAsString());
        } catch (IllegalArgumentException ignore) {
            StringBuilder available = new StringBuilder();
            for (int row = 0; row < Locale.values().length; row++) {
                available.append(Locale.values()[row].name());
                if (row != 0) available.append(", ");
            }

            throw new IllegalArgumentException("Cannot get this default language code! The available language codes are: " + available);
        }
        Language.setUnknownTranslationMessage(ChatColor.translateAlternateColorCodes('&', config.get("default unknown translation message").getAsString()));
        //

        SQLTable table = new SQLTable(database, tableName);
        LvMultiplesLanguages.setApi(new DefaultLvMLApi(table));
        return table;
    }

}
