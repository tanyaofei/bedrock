package io.github.hello09x.bedrock.i18n;

import lombok.SneakyThrows;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    @SneakyThrows
    public static <P extends JavaPlugin & I18nSupported> void register(
            @NotNull P plugin,
            @NotNull String basename
    ) {
        var locales = plugin
                .getConfig()
                .getStringList("i18n.locales")
                .stream()
                .map(local -> {
                    var parts = local.split("_");
                    if (parts.length == 1) {
                        return new Locale(parts[0]);
                    } else if (parts.length == 2) {
                        return new Locale(parts[0], parts[1]);
                    } else {
                        throw new UnsupportedOperationException("Invalid i18n.locales format: " + local);
                    }
                })
                .toList();

        var dataLoader = new URLClassLoader(new URL[]{
                plugin.getDataFolder().toURI().toURL(),
        });
        var pluginLoader = plugin.classLoader();

        var registry = TranslationRegistry.create(Key.key(plugin.identifier()));
        for (var local : locales) {
            ResourceBundle rb;
            try {
                rb = ResourceBundle.getBundle(basename, local, dataLoader, new UTF8ResourceBundleControl());
            } catch (Throwable e) {
                rb = ResourceBundle.getBundle(basename, local, pluginLoader, new UTF8ResourceBundleControl());
            }
            registry.registerAll(local, rb, false);
        }
        GlobalTranslator.translator().addSource(registry);
    }

    public static @NotNull Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return GlobalTranslator.render(component, locale);
    }

    public static @NotNull Component translate(@NotNull String translateKey, @NotNull Locale locale) {
        return translate(Component.translatable(translateKey), locale);
    }

    public static @NotNull Component translate(@NotNull TranslatableComponent component) {
        return translate(component, Locale.getDefault());
    }

    private static @NotNull String asString(@NotNull Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static @NotNull String asString(@NotNull String translateKey) {
        return asString(translateKey, Locale.getDefault());
    }

    public static @NotNull String asString(@NotNull String translateKey, @NotNull Locale locale) {
        return asString(translate(translateKey, locale));
    }

    public static @NotNull String asString(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return asString(translate(component, locale));
    }

}
