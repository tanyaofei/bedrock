package io.github.hello09x.bedrock.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Usage {

    @NotNull
    private final TextComponent component;

    @Nullable
    private String permission;

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description, @Nullable String permission) {
        return new Usage(
                Component.text("§6" + usage + " §7- §f" + description),
                permission
        );
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

}
