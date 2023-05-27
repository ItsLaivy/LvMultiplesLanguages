package codes.laivy.mlanguage.utils;

import com.google.gson.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// TODO: 07/04/2023 OOP
public class ComponentUtils {

    private ComponentUtils() {
    }

    public static @NotNull String serialize(@NotNull BaseComponent component) {
        if (component instanceof TextComponent) {
            TextComponent text = (TextComponent) component;
            if (!text.hasFormatting() && (text.getExtra() == null || text.getExtra().isEmpty())) {
                JsonObject object = new JsonObject();
                object.addProperty("text", text.getText());
                return object.toString();
            }
        }
        return ComponentSerializer.toString(component);
    }
    public static @NotNull String serialize(@NotNull BaseComponent[] components) {
        if (components.length == 1) {
            return serialize(components[0]);
        } else {
            JsonArray array = new JsonArray();
            for (BaseComponent component : components) {
                String serialized = serialize(component);

                try {
                    //noinspection deprecation
                    array.add(new JsonParser().parse(serialized));
                } catch (JsonSyntaxException ignore) {
                    array.add(serialized);
                }
            }
            return array.toString();
        }
    }

    public static @NotNull String getText(@NotNull BaseComponent... components) {
        StringBuilder str = new StringBuilder();

        for (BaseComponent component : components) {
            str.append(component.toLegacyText());
        }

        if (str.toString().startsWith("§f")) {
            return str.toString().replaceFirst("§f", "");
        }

        return str.toString();
    }

    /**
     * Clones the components at the array
     * @param original the components that will be cloned
     * @return the component clones (including extras recursively)
     */
    public static @NotNull BaseComponent[] cloneComponent(@NotNull BaseComponent[] original) {
        List<BaseComponent> componentList = new LinkedList<>();

        for (BaseComponent component : original) {
            BaseComponent cloned = component.duplicate();
            if (component.getExtra() != null) {
                List<BaseComponent> clonedExtras = new ArrayList<>(new LinkedList<>(Arrays.asList(cloneComponent(component.getExtra().toArray(new BaseComponent[0])))));
                cloned.setExtra(clonedExtras);
            }
            componentList.add(cloned);
        }

        return componentList.toArray(new BaseComponent[0]);
    }

    public static @NotNull BaseComponent[] getComponents(@NotNull BaseComponent component) {
        List<BaseComponent> componentList = new LinkedList<>();

        componentList.add(component);
        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                componentList.addAll(Arrays.asList(getComponents(extra)));
            }
        }

        return componentList.toArray(new BaseComponent[0]);
    }

}
