package codes.laivy.mlanguage;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LvMultiplesLanguages {

    private static @Nullable IMultiplesLanguagesAPI<?, ?, ?, ?> api;

    public static @Nullable IMultiplesLanguagesAPI<?, ?, ?, ?> getApi() {
        return LvMultiplesLanguages.api;
    }
    public static void setApi(@NotNull IMultiplesLanguagesAPI<?, ?, ?, ?> api) {
        LvMultiplesLanguages.api = api;
    }

    public static void main(String[] args) {
        System.out.println("Hello, LvML users! :)");
    }

}
