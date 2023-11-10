package io.github.hello09x.bedrock.command;

import com.google.common.base.Strings;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import io.github.hello09x.bedrock.page.Page;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Commands {

    public static @NotNull CommandAPICommand command(@NotNull String name) {
        return new CommandAPICommand(name);
    }

    public static @NotNull CommandTree commandTree(@NotNull String name) {
        return new CommandTree(name);
    }

    public static @NotNull IntegerArgument int32(@NotNull String name) {
        return new IntegerArgument(name);
    }

    public static @NotNull IntegerArgument int32(@NotNull String name, int min) {
        return new IntegerArgument(name, min);
    }

    public static @NotNull IntegerArgument int32(@NotNull String name, int min, int max) {
        return new IntegerArgument(name, min, max);
    }

    public static @NotNull LongArgument int64(@NotNull String name) {
        return new LongArgument(name);
    }

    public static @NotNull LongArgument int64(@NotNull String name, long min) {
        return new LongArgument(name, min);
    }

    public static @NotNull LongArgument int64(@NotNull String name, long min, long max) {
        return new LongArgument(name, min, max);
    }

    public static @NotNull FloatArgument float32(@NotNull String name) {
        return new FloatArgument(name);
    }

    public static @NotNull FloatArgument float32(@NotNull String name, float min) {
        return new FloatArgument(name, min);
    }

    public static @NotNull FloatArgument float32(@NotNull String name, float min, float max) {
        return new FloatArgument(name, min, max);
    }

    public static @NotNull DoubleArgument float64(@NotNull String name) {
        return new DoubleArgument(name);
    }

    public static @NotNull DoubleArgument float64(@NotNull String name, double min) {
        return new DoubleArgument(name, min);
    }

    public static @NotNull DoubleArgument float64(@NotNull String name, double min, double max) {
        return new DoubleArgument(name, min, max);
    }

    public static @NotNull MultiLiteralArgument literals(@NotNull String name, @NotNull List<String> literals) {
        return new MultiLiteralArgument(name, literals);
    }

    public static @NotNull LiteralArgument literal(@NotNull String literal) {
        return new LiteralArgument(literal);
    }

    public static @NotNull LiteralArgument literal(@NotNull String name, @NotNull String literal) {
        return new LiteralArgument(name, literal);
    }

    public static @NotNull CommandArgument cmd(@NotNull String name) {
        return new CommandArgument(name);
    }

    public static <E extends Enum<E>> @NotNull Argument<E> enumerate(@NotNull String name, Class<E> enumClass) {
        return new CustomArgument<>(
                new StringArgument(name),
                info -> {
                    E value;
                    try {
                        value = Enum.valueOf(enumClass, info.input());
                    } catch (IllegalArgumentException e) {
                        throw CustomArgument.CustomArgumentException.fromString("Invalid value: " + info.input());
                    }

                    return value;
                }).replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(
                String[]::new)));
    }

    public static Argument<File> file(@NotNull File folder, @NotNull String name, @Nullable String suffix) {
        return new CustomArgument<>(
                new GreedyStringArgument(name),
                info -> {
                    var filename = info.input();
                    var file = new File(folder, filename);
                    if (!file.exists()) {
                        throw CustomArgument.CustomArgumentException.fromString("File not found: " + file.getPath());
                    }
                    if (suffix != null && !file.getName().endsWith(suffix)) {
                        throw CustomArgument.CustomArgumentException.fromString("Unsupported file: " + file.getPath());
                    }
                    return file;
                }
        ).replaceSuggestions(ArgumentSuggestions.strings(info -> {
            var arg = info.currentArg();
            return Arrays.stream(Optional.ofNullable(folder.listFiles((dir, file) -> file.contains(arg) && (suffix == null || file.endsWith(
                                    suffix))))
                            .orElse(new File[0]))
                    .map(File::getName)
                    .toArray(String[]::new);
        }));
    }

    public static @NotNull LocationArgument location(@NotNull String name) {
        return new LocationArgument(name);
    }

    public static @NotNull RotationArgument rotation(@NotNull String name) {
        return new RotationArgument(name);
    }

    public static @NotNull WorldArgument world(@NotNull String name) {
        return new WorldArgument(name);
    }

    public static @NotNull TextArgument text(@NotNull String name) {
        return new TextArgument(name);
    }

    public static @NotNull PlayerArgument player(@NotNull String name) {
        return new PlayerArgument(name);
    }

    public static @NotNull OfflinePlayerArgument offlinePlayer(@NotNull String name) {
        return new OfflinePlayerArgument(name);
    }

    public static @NotNull UUIDArgument uuid(@NotNull String name) {
        return new UUIDArgument(name);
    }

    public static @NotNull FloatRangeArgument floatRange(@NotNull String nodeName) {
        return new FloatRangeArgument(nodeName);
    }

    public static @NotNull IntegerRangeArgument integerRange(@NotNull String name) {
        return new IntegerRangeArgument(name);
    }

    public static @NotNull String usage(@NotNull String command, @NotNull String description) {
        return "§6" + command + " §7-" + " §f" + description;
    }

    public static @NotNull GreedyStringArgument strings(@NotNull String name) {
        return new GreedyStringArgument(name);
    }

    public static @NotNull ItemStackArgument itemstack(@NotNull String name) {
        return new ItemStackArgument(name);
    }

    public static @NotNull CommandAPICommand helpCommand(@NotNull String parent, @NotNull Usage... usages) {
        var command = parent.startsWith("/") ? parent : "/" + parent;

        var title = " Help: " + parent + " ";
        var prefix = "---------";
        var suffix = Strings.repeat("-", Math.min(50 - title.length() - prefix.length(), prefix.length()));

        var header = Component.textOfChildren(
                Component.text(prefix, NamedTextColor.YELLOW),
                Component.text(title, NamedTextColor.WHITE),
                Component.text(suffix, NamedTextColor.YELLOW)
        );

        return command("help")
                .withAliases("?")
                .withOptionalArguments(
                        int32("page", 1),
                        int32("size", 1)
                )
                .executes((sender, args) -> {
                    var availableUsages = Arrays
                            .stream(usages)
                            .filter(usage -> {
                                var permission = usage.permission();
                                if (permission != null && !sender.hasPermission(permission)) {
                                    return false;
                                }

                                var requirement = usage.requirement();
                                if (requirement != null && !requirement.test(sender)) {
                                    return false;
                                }

                                return true;
                            }).toList();

                    var current = (int) args.getOptional("page").orElse(1);
                    var size = (int) args.getOptional("size").orElse(10);

                    var page = Page.of(availableUsages, current, size);

                    var message = page.asComponent(
                            header,
                            usage -> {
                                return Component.textOfChildren(
                                        Component.text("- "),
                                        usage.asComponent()
                                );
                            },
                            i -> "%s ? %d %d".formatted(command, i, size)
                    );

                    sender.sendMessage(message);
                });

    }


}
