package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessage;
import codes.laivy.mlanguage.lang.Locale;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
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
    public static @NotNull String serialize(@NotNull BaseComponent... components) {
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
    public static @NotNull BaseComponent[] cloneComponent(@NotNull BaseComponent... original) {
        List<BaseComponent> componentList = new LinkedList<>();

        for (BaseComponent component : original) {
            BaseComponent cloned = component.duplicate();
            List<BaseComponent> extra = new LinkedList<>();

            if (component.getExtra() != null) {
                extra.addAll(Arrays.asList(cloneComponent(component.getExtra().toArray(new BaseComponent[0]))));
            }

            cloned.setExtra(extra);
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

    public static @NotNull BaseComponent[] convert(@NotNull Locale locale, @NotNull Object replace) {
        BaseComponent[] index;

        if (replace instanceof BaseComponentMessage) {
            index = ((BaseComponentMessage) replace).getText(locale);
        } else if (replace instanceof BaseComponent) {
            index = new BaseComponent[]{(BaseComponent) replace};
        } else if (replace instanceof BaseComponent[]) {
            index = (BaseComponent[]) replace;
        } else if (replace instanceof Collection || replace instanceof Object[]) {
            Object[] array;

            if (replace instanceof Collection) {
                array = ((Collection<?>) replace).toArray();
            } else {
                array = (Object[]) replace;
            }

            List<BaseComponent> componentList2 = new LinkedList<>();

            int r = 0;
            for (Object object : array) {
                if (r > 0) componentList2.add(new TextComponent("\n"));

                if (object instanceof BaseComponentMessage) {
                    componentList2.add(new TextComponent(((BaseComponentMessage) object).getText(locale)));
                } else if (object instanceof BaseComponent) {
                    componentList2.add((BaseComponent) object);
                } else if (object instanceof BaseComponent[]) {
                    componentList2.add(new TextComponent((BaseComponent[]) object));
                } else {
                    componentList2.add(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(object))));
                }

                r++;
            }

            index = componentList2.toArray(new BaseComponent[0]);
        } else {
            index = new BaseComponent[]{new TextComponent(ChatColor.translateAlternateColorCodes('&', String.valueOf(replace)))};
        }

        return index;
    }

}
