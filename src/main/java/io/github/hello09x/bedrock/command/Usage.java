package io.github.hello09x.bedrock.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Usage {

    @NotNull
    private final TextComponent component;

    @Nullable
    private String permission;

    @Nullable
    private Predicate<CommandSender> requirement;

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description, @Nullable String permission, @Nullable Predicate<CommandSender> requirement) {
        return new Usage(
                Component.text("§6" + usage + " §7- §f" + description),
                permission,
                requirement
        );
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description, @Nullable String permission) {
        return of(usage, description, permission, null);
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description) {
        return of(usage, description, null);
    }

    public @NotNull TextComponent asComponent() {
        return component;
    }

    public @Nullable String permission() {
        return this.permission;
    }

    public @Nullable Predicate<CommandSender> requirement() {
        return this.requirement;
    }

}
