package codes.laivy.mlanguage.utils;

import codes.laivy.mlanguage.api.bungee.components.BaseComponentMessage;
import codes.laivy.mlanguage.lang.Locale;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
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

    public static @NotNull BaseComponent[] removeExtras(@NotNull BaseComponent... components) throws NoSuchFieldException, IllegalAccessException {
        List<BaseComponent> componentList = new LinkedList<>();

        for (BaseComponent component : components) {
            BaseComponent clone = component.duplicate();
            componentList.add(clone);

            Config config = getConfig(component);
            config.apply(clone);

            if (component.getExtra() != null) {
                for (BaseComponent extra : component.getExtra()) {
                    componentList.addAll(Arrays.asList(removeExtras(extra)));
                }
            }

            removeExtra(clone);
        }

        for (BaseComponent component : componentList) {
            removeExtra(component);
            removeParent(component);
        }

        return componentList.toArray(new BaseComponent[0]);
    }

    private static @NotNull Config getConfig(@NotNull BaseComponent component) throws NoSuchFieldException, IllegalAccessException {
        @Nullable ChatColor color = component.getColorRaw();
        @Nullable Boolean bold = component.isBoldRaw();
        @Nullable Boolean italic = component.isItalicRaw();
        @Nullable Boolean underlined = component.isUnderlinedRaw();
        @Nullable Boolean strikethrough = component.isStrikethroughRaw();

        @Nullable BaseComponent parent = getParent(component);
        while (parent != null) {
            if (color == null) {
                color = parent.getColorRaw();
            }
            if (bold == null) {
                bold = parent.isBoldRaw();
            }
            if (italic == null) {
                italic = parent.isItalicRaw();
            }
            if (underlined == null) {
                underlined = parent.isUnderlinedRaw();
            }
            if (strikethrough == null) {
                strikethrough = parent.isStrikethroughRaw();
            }

            parent = getParent(parent);
        }

        return new Config(color, bold, italic, underlined, strikethrough);
    }

    private static final class Config {
        public @Nullable ChatColor color;
        public @Nullable Boolean bold;
        public @Nullable Boolean italic;
        public @Nullable Boolean underlined;
        public @Nullable Boolean strikethrough;

        public Config(@Nullable ChatColor color, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough) {
            this.color = color;
            this.bold = bold;
            this.italic = italic;
            this.underlined = underlined;
            this.strikethrough = strikethrough;
        }

        @NotNull Config apply(@NotNull BaseComponent component) {
            if (component.isItalicRaw() == null) {
                component.setColor(color);
            }
            if (component.isBoldRaw() == null) {
                component.setBold(bold);
            }
            if (component.isItalicRaw() == null) {
                component.setItalic(italic);
            }
            if (component.isUnderlinedRaw() == null) {
                component.setUnderlined(underlined);
            }
            if (component.isStrikethroughRaw() == null) {
                component.setStrikethrough(strikethrough);
            }
            return new Config(component.getColorRaw(), component.isBoldRaw(), component.isItalicRaw(), component.isUnderlinedRaw(), component.isStrikethroughRaw());
        }
    }

    private static @Nullable BaseComponent getParent(@NotNull BaseComponent component) throws NoSuchFieldException, IllegalAccessException {
        Field parentField = BaseComponent.class.getDeclaredField("parent");
        parentField.setAccessible(true);
        return (BaseComponent) parentField.get(component);
    }
    private static void removeParent(@NotNull BaseComponent component) throws NoSuchFieldException, IllegalAccessException {
        Field parentField = BaseComponent.class.getDeclaredField("parent");
        parentField.setAccessible(true);
        parentField.set(component, null);
    }
    private static void removeExtra(@NotNull BaseComponent component) throws NoSuchFieldException, IllegalAccessException {
        Field parentField = BaseComponent.class.getDeclaredField("extra");
        parentField.setAccessible(true);
        parentField.set(component, null);
    }

    public static @NotNull BaseComponent[] mergeBetween(@NotNull TextComponent before, @NotNull TextComponent after, @NotNull BaseComponent[] center) {
        JsonArray array = new JsonArray();

        JsonElement beforeElement = new JsonParser().parse(ComponentUtils.serialize(before));
        JsonElement afterElement = new JsonParser().parse(ComponentUtils.serialize(after));
        JsonElement centerElement = new JsonParser().parse(ComponentUtils.serialize(center));

        if (beforeElement.isJsonArray()) array.addAll(beforeElement.getAsJsonArray());
        else array.add(beforeElement); // Before
        if (centerElement.isJsonArray()) array.addAll(centerElement.getAsJsonArray());
        else array.add(centerElement); // Center
        if (afterElement.isJsonArray()) array.addAll(afterElement.getAsJsonArray());
        else array.add(afterElement); // After


        return ComponentSerializer.parse(array.toString());
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
