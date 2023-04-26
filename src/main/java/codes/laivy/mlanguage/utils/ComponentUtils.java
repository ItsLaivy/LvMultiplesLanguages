package codes.laivy.mlanguage.utils;

import com.google.gson.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// TODO: 07/04/2023 OOP
public class ComponentUtils {

    private ComponentUtils() {
    }

    public static @NotNull TextComponent merge(BaseComponent[] components) {
        return new TextComponent(components);
    }

    public static @NotNull String serialize(BaseComponent components) {
        return serialize(new BaseComponent[] { components });
    }
    public static @NotNull String serialize(BaseComponent[] components) {
        if (components.length == 1) {
            return ComponentSerializer.toString(components[0]);
        } else {
            JsonArray array = new JsonArray();
            for (BaseComponent component : components) {
                try {
                    //noinspection deprecation
                    array.add(new JsonParser().parse(ComponentSerializer.toString(component)));
                } catch (JsonSyntaxException ignore) {
                    array.add(ComponentSerializer.toString(component));
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

        return str.substring(2);
    }

    /**
     * Clones the components at the array
     * @param original the components that will be cloned
     * @return the component clones (including extras recursively)
     */
    public static @NotNull BaseComponent[] cloneComponent(@NotNull BaseComponent[] original) {
        Set<BaseComponent> componentSet = new LinkedHashSet<>();

        for (BaseComponent component : original) {
            BaseComponent cloned = component.duplicate();
            if (component.getExtra() != null) {
                List<BaseComponent> clonedExtras = new ArrayList<>(new LinkedList<>(Arrays.asList(cloneComponent(component.getExtra().toArray(new BaseComponent[0])))));
                cloned.setExtra(clonedExtras);
            }
            componentSet.add(cloned);
        }

        return componentSet.toArray(new BaseComponent[0]);
    }

    public static @NotNull BaseComponent[] getComponents(@NotNull BaseComponent component) {
        Set<BaseComponent> componentSet = new LinkedHashSet<>();
        componentSet.add(component);
        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                componentSet.addAll(Arrays.asList(getComponents(extra)));
            }
        }
        return componentSet.toArray(new BaseComponent[0]);
    }

}
