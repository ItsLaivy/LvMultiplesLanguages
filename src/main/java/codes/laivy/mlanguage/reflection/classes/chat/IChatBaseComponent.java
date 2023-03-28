package codes.laivy.mlanguage.reflection.classes.chat;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class IChatBaseComponent extends ObjectExecutor {

    public static @NotNull IChatBaseComponent convert(@NotNull BaseComponent... components) {
        return multiplesLanguagesBukkit().getVersion().baseComponentToIChatComponent(components);
    }
    public static @NotNull BaseComponent[] convert(@NotNull IChatBaseComponent iChatBaseComponent) {
        return multiplesLanguagesBukkit().getVersion().iChatComponentToBaseComponent(iChatBaseComponent);
    }

    public IChatBaseComponent(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull IChatBaseComponentClass getClassExecutor() {
        return (IChatBaseComponentClass) multiplesLanguagesBukkit().getVersion().getClassExec("IChatBaseComponent");
    }

    public static class IChatBaseComponentClass extends ClassExecutor {
        public IChatBaseComponentClass(@NotNull String className) {
            super(className);
        }
    }
    public static class ChatSerializerClass extends ClassExecutor {
        public ChatSerializerClass(@NotNull String className) {
            super(className);
        }
    }
}
