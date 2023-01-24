package codes.laivy.mlanguage.api;

import codes.laivy.data.DataAPI;
import codes.laivy.data.sql.SQLReceptor;
import codes.laivy.data.sql.SQLTable;
import codes.laivy.data.sql.SQLVariable;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DefaultLvMLApi implements LvMLApi {

    private final @NotNull SQLTable table;

    public DefaultLvMLApi(@NotNull SQLTable table) {
        this.table = table;
        new SQLVariable("language", table, "", false, true);
    }

    public @NotNull SQLTable getTable() {
        return table;
    }

    private @NotNull LocaleAPI localeAPI = new LocaleAPI() {
        @Override
        public @Nullable Locale getLocale(@NotNull UUID uuid) {
            SQLReceptor receptor;
            if ((receptor = DataAPI.getSQLReceptor(getTable(), uuid.toString())) != null) {
                try {
                    return Locale.valueOf(receptor.get("language"));
                } catch (IllegalArgumentException ignore) {
                }
            } else {
                receptor = new SQLReceptor(getTable(), "", uuid.toString()) {{
                    load();
                }};
                try {
                    return Locale.valueOf(receptor.get("language"));
                } catch (IllegalArgumentException ignore) {
                }
                receptor.unload(false);
            }
            return null;
        }

        @Override
        public void setLocale(@NotNull UUID uuid, @NotNull Locale locale) {
            SQLReceptor receptor;
            if ((receptor = DataAPI.getSQLReceptor(getTable(), uuid.toString())) != null) {
                receptor.set("language", locale.name());
            } else {
                receptor = new SQLReceptor(getTable(), "", uuid.toString()) {{
                    load();
                }};
                receptor.set("language", locale.name());
                receptor.unload(true);
            }
        }

        public @Nullable String getNullableMessage(@NotNull String plugin, @NotNull String key, @Nullable Locale locale) {
            if (locale != null) {
                if (Language.LANGUAGES.get(locale).containsKey(plugin)) {
                    for (@NotNull Language language : Language.LANGUAGES.get(locale).get(plugin)) {
                        String msg = language.getIfContains(locale, key);
                        if (msg != null) {
                            return msg;
                        }
                    }
                }
                if (Language.DEFAULT_LANGUAGE_CODE != locale) {
                    for (Language language : getLanguages(plugin)) {
                        String msg = language.getIfContains(locale, key);
                        if (msg != null) {
                            return msg;
                        }
                    }
                }
                if (Language.LANGUAGES.get(locale).containsKey(plugin)) {
                    for (@NotNull Language language : Language.LANGUAGES.get(locale).get(plugin)) {
                        for (Locale fLocale : language.getDefaultPack().getLocales()) {
                            if (language.getPlugin().equals(plugin)) {
                                String msg = language.getIfContains(fLocale, key);
                                if (msg != null) {
                                    return msg;
                                }
                            }
                        }
                    }
                }
            } else {
                for (@NotNull Language language : Language.LANGUAGES.get(Language.DEFAULT_LANGUAGE_CODE).get(plugin)) {
                    for (Locale fLocale : language.getDefaultPack().getLocales()) {
                        if (language.getPlugin().equals(plugin)) {
                            String msg = language.getIfContains(fLocale, key);
                            if (msg != null) {
                                return msg;
                            }
                        }
                    }
                }
                for (@NotNull Language language : Language.LANGUAGES_ALL) {
                    if (language.getPlugin().equals(plugin)) {
                        String msg = language.getDefaultPack().get(key);
                        if (msg != null) {
                            return msg;
                        }
                    }
                }
            }

            return null;
        }

        @Override
        public @NotNull String getMessage(@NotNull String plugin, @NotNull String key) {
            return getMessage(plugin, key, Language.DEFAULT_LANGUAGE_CODE);
        }

        @Override
        public @NotNull String getMessage(@NotNull String plugin, @NotNull String key, @Nullable Locale locale) {
            String msg = getNullableMessage(plugin, key, locale);

            if (msg == null) {
                return Language.getUnknownTranslationMessage(key, (locale != null ? locale : Language.DEFAULT_LANGUAGE_CODE));
            } else {
                return msg;
            }
        }

        @Override
        public boolean hasKey(@NotNull String plugin, @NotNull String key, @Nullable Locale locale) {
            return getNullableMessage(plugin, key, locale) != null;
        }

        @Override
        public @NotNull Set<@NotNull Language> getLanguages() {
            Set<Language> languages = new HashSet<>();
            for (Locale locale : Locale.values()) {
                for (Set<Language> set : Language.LANGUAGES.get(locale).values()) {
                    languages.addAll(set);
                }
            }
            return languages;
        }

        @Override
        public @NotNull Set<@NotNull Language> getLanguages(@NotNull String plugin) {
            Set<Language> languages = new HashSet<>();
            for (@NotNull Language language : getLanguages()) {
                if (language.getPlugin().equals(plugin)) {
                    languages.add(language);
                }
            }
            return languages;
        }
    };
    private @NotNull MenusAPI menusAPI = player -> null;

    @Override
    public @NotNull LocaleAPI getLocaleAPI() {
        return localeAPI;
    }

    @Override
    public void setLocaleAPI(@NotNull LocaleAPI api) {
        this.localeAPI = api;
    }

    @Override
    public @NotNull MenusAPI getMenusAPI() {
        return menusAPI;
    }

    @Override
    public void setMenusAPI(@NotNull MenusAPI api) {
        this.menusAPI = api;
    }

}
