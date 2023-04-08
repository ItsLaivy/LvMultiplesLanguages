package codes.laivy.mlanguage.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

// TODO: 07/04/2023 OOP
public class ComponentUtils {

    private ComponentUtils() {
    }

    @ApiStatus.Experimental
    public static @NotNull TextComponent merge(BaseComponent[] components) {
        return new TextComponent(BaseComponent.toLegacyText(components));
    }

    public static @NotNull JsonElement serialize(BaseComponent[] components) {
        if (components.length == 1) {
            //noinspection deprecation
            return new JsonParser().parse(ComponentSerializer.toString(components[0]));
        } else {
            JsonArray array = new JsonArray();
            for (BaseComponent component : components) {
                //noinspection deprecation
                array.add(new JsonParser().parse(ComponentSerializer.toString(component)));
            }
            return array;
        }
    }

    public static @NotNull String getText(@NotNull BaseComponent... components) {
        StringBuilder str = new StringBuilder();

        Set<BaseComponent> total = new LinkedHashSet<>();
        for (BaseComponent component : components) {
            total.addAll(Arrays.asList(getTextRecursive(component)));
        }

        for (BaseComponent component : total) {
            str.append(component.toLegacyText());
        }

        return str.toString();
    }

    private static @NotNull BaseComponent[] getTextRecursive(@NotNull BaseComponent component) {
        Set<BaseComponent> componentSet = new LinkedHashSet<>();
        componentSet.add(component);
        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                componentSet.addAll(Arrays.asList(getTextRecursive(extra)));
            }
        }
        return componentSet.toArray(new BaseComponent[0]);
    }

}