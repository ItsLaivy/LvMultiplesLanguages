package codes.laivy.mlanguage.lang;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("unused")
public enum Locale {

    AF("Afrikaans"),

    AR_AE("Arabic (U.A.E.)"),
    AR_BH("Arabic (Bahrain)"),
    AR_DZ("Arabic (Algeria)"),
    AR_EG("Arabic (Egypt)"),
    AR_IQ("Arabic (Iraq)"),
    AR_JO("Arabic (Jordan)"),
    AR_KW("Arabic (Kuwait)"),
    AR_LB("Arabic (Lebanon)"),
    AR_LY("Arabic (Libya)"),
    AR_MA("Arabic (Morocco)"),
    AR_OM("Arabic (Oman)"),
    AR_QA("Arabic (Qatar)"),
    AR_SA("Arabic (Saudi Arabia)"),
    AR_SY("Arabic (Syria)"),
    AR_TN("Arabic (Tunisia)"),
    AR_YE("Arabic (Yemen)"),

    BE("Belarusian"),

    BG("Bulgarian"),

    CA("Catalan"),

    CS("Czech"),

    CY("Welsh"),

    DA("Danish"),

    DE("German (Standard)"),
    DE_AT("German (Austria)"),
    DE_CH("German (Switzerland)"),
    DE_LI("German (Liechtenstein)"),
    DE_LU("German (Luxembourg)"),

    EL("Greek"),

    EN("English"),
    EN_AU("English (Australia)"),
    EN_BZ("English (Belize)"),
    EN_CA("English (Canada)"),
    EN_GB("English (United Kingdom)"),
    EN_IE("English (Ireland)"),
    EN_JM("English (Jamaica)"),
    EN_NZ("English (New Zealand)"),
    EN_TT("English (Trinidad)"),
    EN_US("English (United States)"),
    EN_ZA("English (South Africa)"),

    ES("Spanish (Spain)"),
    ES_AR("Spanish (Argentina)"),
    ES_BO("Spanish (Bolivia)"),
    ES_CL("Spanish (Chile)"),
    ES_CO("Spanish (Colombia)"),
    ES_CR("Spanish (Costa Rica)"),
    ES_DO("Spanish (Dominican Republic)"),
    ES_EC("Spanish (Ecuador)"),
    ES_GT("Spanish (Guatemala)"),
    ES_HN("Spanish (Honduras)"),
    ES_MX("Spanish (Mexico)"),
    ES_NI("Spanish (Nicaragua)"),
    ES_PA("Spanish (Panama)"),
    ES_PE("Spanish (Peru)"),
    ES_PR("Spanish (Puerto Rico)"),
    ES_PY("Spanish (Paraguay)"),
    ES_SV("Spanish (El Salvador)"),
    ES_UY("Spanish (Uruguay)"),
    ES_VE("Spanish (Venezuela)"),

    ET("Estonian"),
    EU("Basque"),
    FA("Farsi"),
    FI("Finnish"),
    FO("Faeroese"),

    FR("French (Standard)"),
    FR_BE("French (Belgium)"),
    FR_CA("French (Canada)"),
    FR_CH("French (Switzerland)"),
    FR_LU("French (Luxembourg)"),

    GA("Irish"),
    GD("Gaelic (Scotland)"),
    HE("Hebrew"),
    HI("Hindi"),
    HR("Croatian"),
    HU("Hungarian"),
    ID("Indonesian"),
    IS("Icelandic"),

    IT("Italian (Standard)"),
    IT_CH("Italian (Switzerland)"),

    JA("Japanese"),
    JI("Yiddish"),
    KO("Korean"),
    KU("Kurdish"),
    LT("Lithuanian"),
    LV("Latvian"),
    MK("Macedonian (FYROM)"),
    ML("Malayalam"),
    MS("Malaysian"),
    MT("Maltese"),
    NB("Norwegian (Bokmål)"),

    NL("Dutch (Standard)"),
    NL_BE("Dutch (Belgium)"),

    NN("Norwegian (Nynorsk)"),
    NO("Norwegian"),
    PA("Punjabi"),
    PL("Polish"),

    PT("Portuguese (Portugal)"),
    PT_BR("Portuguese (Brazil)"),

    RM("Rhaeto-Romanic"),

    RO("Romanian"),
    RO_MD("Romanian (Republic of Moldova)"),

    RU("Russian"),
    RU_MD("Russian (Republic of Moldova)"),

    SB("Sorbian"),
    SK("Slovak"),
    SL("Slovenian"),
    SQ("Albanian"),
    SR("Serbian"),

    SV("Swedish"),
    SV_FI("Swedish (Finland)"),

    TH("Thai"),
    TN("Tswana"),
    TR("Turkish"),
    TS("Tsonga"),
    UA("Ukrainian"),
    UR("Urdu"),
    VE("Venda"),
    VI("Vietnamese"),
    XH("Xhosa"),

    ZH_CN("Chinese (PRC)"),
    ZH_HK("Chinese (Hong Kong)"),
    ZH_SG("Chinese (Singapore)"),
    ZH_TW("Chinese (Taiwan)"),

    ZU("Zulu"),
    ;

    private final @NotNull String name;

    Locale(@NotNull String name) {
        this.name = name;
        Language.LANGUAGES.put(this, new HashMap<>());
    }

    public @NotNull String getName() {
        return name;
    }
}
