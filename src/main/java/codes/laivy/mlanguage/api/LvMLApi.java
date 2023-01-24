package codes.laivy.mlanguage.api;

import org.jetbrains.annotations.NotNull;

public interface LvMLApi {

    @NotNull LocaleAPI getLocaleAPI();
    void setLocaleAPI(@NotNull LocaleAPI api);

    @NotNull MenusAPI getMenusAPI();
    void setMenusAPI(@NotNull MenusAPI api);

}
