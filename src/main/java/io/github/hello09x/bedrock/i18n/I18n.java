package io.github.hello09x.bedrock.i18n;

import io.github.hello09x.bedrock.util.Components;
import lombok.SneakyThrows;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    @NotNull
    private final PluginTranslator translator;

    @NotNull
    private final Locale defaultLocale;

    @SneakyThrows
    public <P extends JavaPlugin & I18nSupported> I18n(@NotNull P plugin, @NotNull String bundleName) {
        var locale = parseLocale(plugin.getConfig().getString("i18n.locale", "zh"));
        var dataLoader = new URLClassLoader(new URL[]{
                plugin.getDataFolder().toURI().toURL(),
        });
        var pluginLoader = plugin.classLoader();

        var registry = TranslationRegistry.create(Key.key(plugin.getName().toLowerCase(Locale.ROOT)));
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle(bundleName, locale, dataLoader, new UTF8ResourceBundleControl());
        } catch (Throwable e) {
            rb = ResourceBundle.getBundle(bundleName, locale, pluginLoader, new UTF8ResourceBundleControl());
        }
        registry.registerAll(locale, rb, false);

        this.translator = new PluginTranslator(plugin, registry);
        this.defaultLocale = locale;
    }

    public @NotNull Component translate(@NotNull String translateKey) {
        return translate(Component.translatable(translateKey));
    }

    public @NotNull Component translate(@NotNull String translateKey, @NotNull TextColor color) {
        return translate(Component.translatable(translateKey, color));
    }

    public @NotNull Component translate(@NotNull String translateKey, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        return translate(Component.translatable(translateKey, color, decoration));
    }

    public @NotNull Component translate(@NotNull Component component) {
        return translator.render(component, defaultLocale);
    }

    public @NotNull Component translate(@NotNull Translatable translateKey) {
        return translate(translateKey.translationKey());
    }

    public @NotNull Component translate(@NotNull Translatable translateKey, @NotNull TextColor color) {
        return translate(translateKey.translationKey(), color);
    }

    public @NotNull Component translate(@NotNull Translatable translateKey, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        return translate(translateKey.translationKey(), color, decoration);
    }

    public @NotNull String asString(@NotNull String translateKey) {
        return Components.asString(translate(translateKey));
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
