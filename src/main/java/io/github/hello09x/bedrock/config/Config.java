package io.github.hello09x.bedrock.config;

import com.google.common.base.Throwables;
import io.github.hello09x.bedrock.io.IOUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Config<T extends Config<T>> {

    protected final String version;

    protected final Plugin plugin;

    private final Set<Consumer<T>> listeners = new LinkedHashSet<>();

    public Config(@NotNull Plugin plugin, @Nullable String version) {
        this.plugin = plugin;
        this.version = version;
    }

    public void reload(boolean event) {
        var folder = plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalStateException("Failed to create data folder for plugin: " + this.plugin.getName());
        }

        var file = new File(folder, "config.yml");
        if (!file.exists()) {
            var resource = plugin.getClass().getClassLoader().getResource("config.yml");
            if (resource != null) {
                try (var in = resource.openStream()) {
                    IOUtil.copy(new InputStreamReader(in, StandardCharsets.UTF_8), new FileWriter(file, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    throw new IllegalStateException(
                            "Failed to read default config file in jar for plugin: " + plugin.getName(),
                            e
                    );
                }
            }
        }

        plugin.reloadConfig();
        if (this.version != null && !Objects.equals(plugin.getConfig().getString("version"), version)) {
            plugin.getLogger().warning("配置文件版本不匹配, 请备份并删除原有的配置文件并重新生成");
        }

        this.reload(plugin.getConfig());
        if (event && !this.listeners.isEmpty()) {
            @SuppressWarnings("unchecked")
            var typedThis = (T) this;
            for (var listener : this.listeners) {
                try {
                    listener.accept(typedThis);
                } catch (Throwable e) {
                    this.plugin.getLogger().severe(Throwables.getStackTraceAsString(e));
                }
            }
        }
    }

    public void addListener(@NotNull Consumer<T> listener) {
        this.listeners.add(listener);
    }

    protected abstract void reload(@NotNull FileConfiguration file);


}
