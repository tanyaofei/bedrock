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
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static Locale LOCAL = Locale.getDefault();

    @SneakyThrows
    public static <P extends JavaPlugin & I18nSupported> void register(
            @NotNull P plugin,
            @NotNull String basename
    ) {
        var localeString = plugin.getConfig().getString("i18n.locale");
        if (localeString == null) {
            throw new IllegalStateException("Missing i18n.local property in your config.yml");
        }

        var locale = parseLocale(localeString);
        LOCAL = locale;

        var dataLoader = new URLClassLoader(new URL[]{
                plugin.getDataFolder().toURI().toURL(),
        });
        var pluginLoader = plugin.classLoader();

        var registry = TranslationRegistry.create(Key.key(plugin.identifier()));
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle(basename, locale, dataLoader, new UTF8ResourceBundleControl());
        } catch (Throwable e) {
            rb = ResourceBundle.getBundle(basename, locale, pluginLoader, new UTF8ResourceBundleControl());
        }
        registry.registerAll(locale, rb, false);

        GlobalTranslator.translator().addSource(registry);
    }

    public static @NotNull Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return GlobalTranslator.render(component, locale);
    }

    public static @NotNull Component translate(@NotNull String translateKey, @NotNull Locale locale) {
        return translate(Component.translatable(translateKey), locale);
    }

    public static @NotNull Component translate(@NotNull TranslatableComponent component) {
        return translate(component, LOCAL);
    }

    public static @NotNull Component translate(@NotNull TranslateKey translateKey) {
        return translate(Component.translatable(translateKey.translateKey()), LOCAL);
    }

    public static @NotNull Component translate(@NotNull TranslateKey translateKey, @NotNull Locale locale) {
        return translate(Component.translatable(translateKey.translateKey()), locale);
    }

    private static @NotNull String asString(@NotNull Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static @NotNull String asString(@NotNull String translateKey) {
        return asString(translateKey, LOCAL);
    }

    public static @NotNull String asString(@NotNull String translateKey, @NotNull Locale locale) {
        return asString(translate(translateKey, locale));
    }

    public static @NotNull String asString(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return asString(translate(component, locale));
    }

    private static @NotNull Locale parseLocale(@NotNull String locale) {
        var parts = locale.split("_");
        if (parts.length == 1) {
            return new Locale(parts[0]);
        } else if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        } else {
            throw new UnsupportedOperationException("Invalid i18n.locales format: " + locale);
        }
    }

}
