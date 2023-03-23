package codes.laivy.mlanguage.api;

import codes.laivy.data.sql.SqlDatabase;
import codes.laivy.data.sql.SqlReceptor;
import codes.laivy.data.sql.SqlTable;
import codes.laivy.data.sql.SqlVariable;
import codes.laivy.data.sql.mysql.MysqlDatabase;
import codes.laivy.data.sql.mysql.MysqlTable;
import codes.laivy.data.sql.mysql.natives.MysqlDatabaseNative;
import codes.laivy.data.sql.mysql.natives.MysqlReceptorNative;
import codes.laivy.data.sql.mysql.natives.MysqlTableNative;
import codes.laivy.data.sql.mysql.natives.MysqlVariableNative;
import codes.laivy.data.sql.mysql.natives.manager.MysqlManagerNative;
import codes.laivy.data.sql.mysql.variable.type.MysqlTextVariableType;
import codes.laivy.data.sql.sqlite.SqliteDatabase;
import codes.laivy.data.sql.sqlite.SqliteTable;
import codes.laivy.data.sql.sqlite.natives.SqliteDatabaseNative;
import codes.laivy.data.sql.sqlite.natives.SqliteReceptorNative;
import codes.laivy.data.sql.sqlite.natives.SqliteTableNative;
import codes.laivy.data.sql.sqlite.natives.SqliteVariableNative;
import codes.laivy.data.sql.sqlite.natives.manager.SqliteManagerNative;
import codes.laivy.data.sql.sqlite.variable.type.SqliteTextVariableType;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.JsonConfigUtils;
import codes.laivy.mlanguage.utils.Platform;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class MultiplesLanguagesAPI implements IMultiplesLanguagesAPI {

    private final @NotNull Platform platform;

    private @NotNull ItemTranslator<?> itemTranslator;

    private @Nullable Set<Language> languages;
    private @Nullable Locale defaultLocale;
    private @Nullable JsonObject configuration;

    private @Nullable SqlDatabase database;
    private @Nullable SqlTable table;
    private @Nullable SqlVariable variable;

    public MultiplesLanguagesAPI(@NotNull Platform platform, @NotNull ItemTranslator<?> itemTranslator) {
        this.platform = platform;
        this.itemTranslator = itemTranslator;
    }

    public @NotNull Platform getPlatform() {
        return platform;
    }

    public @NotNull SqlDatabase getDatabase() {
        if (database != null) {
            return database;
        }
        throw new NullPointerException("The API isn't loaded yet.");
    }

    public @NotNull SqlTable getTable() {
        if (table != null) {
            return table;
        }
        throw new NullPointerException("The API isn't loaded yet.");
    }

    public SqlVariable getVariable() {
        if (variable != null) {
            return variable;
        }
        throw new NullPointerException("The API isn't loaded yet.");
    }

    public @Nullable JsonObject getConfiguration() {
        return configuration;
    }

    @Override
    public void load() {
        if (getPlatform().getDataFolder() != null) {
            try {
                this.configuration = JsonConfigUtils.safeGet("/config.json", getPlatform().getDataFolder().toPath()).getAsJsonObject();

                // Configurations
                JsonObject config = configuration.getAsJsonObject("configurations");
                // Default language code
                try {
                    defaultLocale = Locale.valueOf(config.get("default language code").getAsString());
                } catch (IllegalArgumentException ignore) {
                    StringBuilder available = new StringBuilder();
                    for (int row = 0; row < Locale.values().length; row++) {
                        available.append(Locale.values()[row].name());
                        if (row != 0) available.append(", ");
                    }

                    throw new IllegalArgumentException("Cannot get this default language code! The available language codes are: " + available);
                }
                // Database
                JsonObject dbJson = configuration.getAsJsonObject("database");
                JsonObject sqlInfos = dbJson.getAsJsonObject("sql informations");

                String type = dbJson.get("type").getAsString();
                String tableName = sqlInfos.get("table").getAsString();
                String databaseName = sqlInfos.get("database").getAsString();
                String localeVariable = sqlInfos.get("variable_locale").getAsString();

                if (type.equalsIgnoreCase("sqlite")) {
                    String path = "";
                    if (sqlInfos.has("sqlite")) {
                        path = sqlInfos.getAsJsonObject("sqlite").get("path").getAsString();
                    }

                    database = new SqliteDatabaseNative(new SqliteManagerNative(new File(getPlatform().getDataFolder() + File.separator + path)), databaseName);
                    table = new SqliteTableNative((SqliteDatabase) database, tableName);
                    variable = new SqliteVariableNative((SqliteTable) table, localeVariable, new SqliteTextVariableType(), getDefaultLocale().name());
                } else if (type.equalsIgnoreCase("mysql")) {
                    if (sqlInfos.has("mysql")) {
                        JsonObject mysqlInfos = sqlInfos.getAsJsonObject("mysql");
                        String address = mysqlInfos.get("address").getAsString();
                        int port = mysqlInfos.get("port").getAsInt();
                        String username = mysqlInfos.get("username").getAsString();
                        String password = mysqlInfos.get("password").getAsString();

                        database = new MysqlDatabaseNative(new MysqlManagerNative(address, username, password, port), databaseName);
                        table = new MysqlTableNative((MysqlDatabase) database, tableName);
                        variable = new MysqlVariableNative((MysqlTable) table, localeVariable, new MysqlTextVariableType(MysqlTextVariableType.Size.TINYTEXT), getDefaultLocale().name());
                    } else {
                        throw new NullPointerException("Couldn't find mysql informations!");
                    }
                } else {
                    throw new NullPointerException("Couldn't get database type '" + type + "'. The allowed are 'mysql' or 'sqlite'.");
                }
                //
            } catch (Throwable e) {
                throw new RuntimeException("Multiples languages loading", e);
            }
        } else {
            throw new NullPointerException("Couldn't find plataform datafolder.");
        }
    }

    protected @NotNull SqlReceptor createReceptor(@NotNull UUID user) {
        if (database instanceof MysqlDatabase) {
            return new MysqlReceptorNative((MysqlTable) getTable(), user.toString());
        } else if (database instanceof SqliteDatabase) {
            return new SqliteReceptorNative((SqliteTable) getTable(), user.toString());
        } else {
            throw new IllegalStateException("Couldn't find this database type '" + getDatabase().getClass().getName() + "'");
        }
    }

    @Override
    public void unload() {
        getDatabase().unload();

        database = null;
        table = null;
        variable = null;

        languages = null;
        defaultLocale = null;
    }

    @Override
    public @NotNull Set<Language> getLanguages() {
        if (languages != null) {
            return languages;
        }
        throw new NullPointerException("The API isn't loaded yet.");
    }

    @Override
    public @NotNull BaseComponent get(@NotNull Locale locale, @NotNull Language language, @NotNull String id, @NotNull Object... replaces) {
        return language.get(locale, id, replaces);
    }

    @Override
    public @NotNull Message get(final @NotNull Language language, final @NotNull String id, final @NotNull Object... replaces) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Locale getLocale(@NotNull UUID user) {
        SqlReceptor receptor = createReceptor(user);
        receptor.load();

        String localeStr = receptor.get(getVariable().getId());
        Locale locale;

        if (localeStr != null) {
            locale = Locale.valueOf(localeStr);
        } else {
            locale = null;
        }

        receptor.unload(false);

        return locale;
    }

    @Override
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        SqlReceptor receptor = createReceptor(user);
        receptor.load();
        receptor.set(getVariable().getId(), locale != null ? locale.name() : null);
        receptor.unload(false);
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        if (defaultLocale != null) {
            return defaultLocale;
        } else {
            throw new NullPointerException("The API isn't loaded yet.");
        }
    }

    @Override
    public @NotNull ItemTranslator<?> getItemTranslator() {
        return itemTranslator;
    }

    public void setItemTranslator(@NotNull ItemTranslator<?> itemTranslator) {
        this.itemTranslator = itemTranslator;
    }
}
