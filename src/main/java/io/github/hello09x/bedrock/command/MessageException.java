package io.github.hello09x.bedrock.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class MessageException extends Exception {

    private final Component message;

    public MessageException(@NotNull String message) {
        super(message);
        this.message = Component.text(message, RED);
    }

    public MessageException(@NotNull TextComponent message) {
        super(message.content());
        this.message = message;
    }

    public MessageException(@NotNull Component message) {
        super(PlainTextComponentSerializer.plainText().serialize(message));
        this.message = message;
    }

    public @NotNull Component asComponent() {
        return message;
    }

}
