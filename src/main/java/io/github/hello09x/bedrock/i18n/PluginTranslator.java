package io.github.hello09x.bedrock.i18n;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

public class PluginTranslator implements Translator {

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final TranslationRegistry registry;

    @NotNull
    private final TranslatableComponentRenderer<Locale> renderer;

    public PluginTranslator(@NotNull Plugin plugin, @NotNull TranslationRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
        this.renderer = TranslatableComponentRenderer.usingTranslationSource(this);
    }


    @Override
    public @NotNull Key name() {
        return new NamespacedKey(plugin.getName().toLowerCase(Locale.ROOT), "translator");
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return registry.translate(key, locale);
    }

    public @NotNull Component render(@NotNull Component component, @NotNull Locale locale) {
        return renderer.render(component, locale);
    }

}
