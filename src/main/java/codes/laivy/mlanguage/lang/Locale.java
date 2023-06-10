package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// TODO: 06/06/2023 Pseudo enum
public enum Locale {
    EN_US("en_US", "English (US)"),
    ES_ES("es_ES", "Español (España)"),
    ES_MX("es_MX", "Español (México)"),
    DE_DE("de_DE", "Deutsch"),
    FR_FR("fr_FR", "Français"),
    IT_IT("it_IT", "Italiano"),
    JA_JP("ja_JP", "日本語"),
    KO_KR("ko_KR", "한국어"),
    PT_BR("pt_BR", "Português (Brasil)"),
    PT_PT("pt_PT", "Português (Portugal)"),
    RU_RU("ru_RU", "Русский"),
    ZH_CN("zh_CN", "简体中文"),
    ZH_TW("zh_TW", "繁體中文"),
    AR_SA("ar_SA", "العربية"),
    BG_BG("bg_BG", "Български"),
    CS_CZ("cs_CZ", "Čeština"),
    DA_DK("da_DK", "Dansk"),
    NL_NL("nl_NL", "Nederlands"),
    ET_EE("et_EE", "Eesti keel"),
    FI_FI("fi_FI", "Suomi"),
    EL_GR("el_GR", "Ελληνικά"),
    HE_IL("he_IL", "עברית"),
    HI_IN("hi_IN", "हिन्दी"),
    HU_HU("hu_HU", "Magyar"),
    ID_ID("id_ID", "Bahasa Indonesia"),
    IS_IS("is_IS", "Íslenska"),
    LV_LV("lv_LV", "Latviešu"),
    LT_LT("lt_LT", "Lietuvių"),
    MS_MY("ms_MY", "Bahasa Melayu"),
    NB_NO("nb_NO", "Norsk bokmål"),
    NN_NO("nn_NO", "Norsk nynorsk"),
    PL_PL("pl_PL", "Polski"),
    RO_RO("ro_RO", "Română"),
    SR_RS("sr_RS", "Српски"),
    SK_SK("sk_SK", "Slovenčina"),
    SL_SI("sl_SI", "Slovenščina"),
    SV_SE("sv_SE", "Svenska"),
    TH_TH("th_TH", "ไทย"),
    TR_TR("tr_TR", "Türkçe"),
    UK_UA("uk_UA", "Українська"),
    VI_VN("vi_VN", "Tiếng Việt");

    private final @NotNull String code;
    private final @NotNull String name;

    Locale(@NotNull String code, @NotNull String name) {
        this.code = code;
        this.name = name;
    }

    @Contract(pure = true)
    public @NotNull String getCode() {
        return code;
    }

    @Contract(pure = true)
    public @NotNull String getName() {
        return name;
    }

    @Contract(pure = true)
    public static @NotNull Locale getByCode(@NotNull String code) {
        for (Locale locale : values()) {
            if (locale.getCode().equalsIgnoreCase(code)) {
                return locale;
            }
        }
        throw new IllegalArgumentException("Couldn't find a locale with code '" + code + "'");
    }

    @Contract(pure = true)
    public static @NotNull Locale fromJavaLocale(@NotNull java.util.Locale javaLocale) {
        String language = javaLocale.getLanguage();
        String country = javaLocale.getCountry();
        String code = language + "_" + country;

        try {
            return Locale.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Couldn't find a locale with code '" + code + "'");
        }
    }

}